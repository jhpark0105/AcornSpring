package com.erp.process.branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.NoticeDto;
import com.erp.dto.NoticeNoOnly;
import com.erp.entity.Notice;
import com.erp.repository.NoticeRepository;

@Service
public class NoticeProcess {
	@Autowired
	private NoticeRepository noticeRepository;

	@Autowired
	private ImageProcess imageProces;

	// 상단에 고정할 중요 공지 구분하여 페이징처리
	public Page<NoticeDto> selectAll(Pageable pageable){
		// 중요공지 전부 가져옴
		List<Notice> checkedNotice = noticeRepository.findByNoticeCheckOrderByNoticeNoDesc(true);
		// 중요 공지 수 계산하여 할당
		int checkedNoticeCount = checkedNotice.size();

		// 페이지 크기 설정 (고정 + 일반)
		int pageSize = 10;

		// 일반 공지 페이징 계산
		int normalNoticesPerPage = pageSize - checkedNoticeCount;
		int pageNo = pageable.getPageNumber();

		// 일반공지를 페이징하여 가져옴
		Page<Notice> normalNotice = noticeRepository.findByNoticeCheckOrderByNoticeNoDesc(
				false, PageRequest.of(pageNo, normalNoticesPerPage));

		// 일반 공지 수 계산
		int normalNoticeCount = (int)normalNotice.getTotalElements();
		// 총 페이지 수 계산
		int totalPages = (int)Math.ceil((double)normalNoticeCount / normalNoticesPerPage);
		// 페이징처리를 위한 totalItems 계산
		int totalItems = normalNoticeCount + checkedNoticeCount * totalPages;

		// 중요/일반 공지를 함께 담는 배열객체 생성
		List<NoticeDto> joinedNotice = new ArrayList<>();

		if(pageNo >= totalPages) {
			// 페이지번호 요청한계 초과 시: 빈 페이지 반환
			return new PageImpl<NoticeDto>(joinedNotice, pageable, 0);
		} else {
			// 페이지번호 정상: 중요 및 일반공지를 DTO로 변환하여 내용을 리스트에 추가
			joinedNotice.addAll(checkedNotice.stream()
					.map(Notice::toDto).collect(Collectors.toList()));
			joinedNotice.addAll(normalNotice.getContent().stream()
					.map(Notice::toDto).collect(Collectors.toList()));

			// 결합된 리스트와 페이징정보를 포함한 PageImpl객체 반환
			return new PageImpl<NoticeDto>(joinedNotice, PageRequest.of(
					pageNo, pageSize), totalItems);
		}
	}

	// 해당 번호 공지 읽기
	public NoticeDto selectOne(int noticeNo) {
		Notice currentNotice = noticeRepository.findById(noticeNo)
				.orElseThrow(() -> new NoSuchElementException("해당 번호와 일치하는 공지사항이 없습니다."));

		// 이전/이후버튼에 해당하는 공지번호
		NoticeNoOnly prevNotice = noticeRepository.findFirstByNoticeNoLessThanOrderByNoticeNoDesc(noticeNo);
		NoticeNoOnly nextNotice = noticeRepository.findFirstByNoticeNoGreaterThanOrderByNoticeNoAsc(noticeNo);

		// 이전/이후 공지가 존재하면 해당하는 번호 할당, 없으면 null 할당
		return NoticeDto.builder()
				.noticeNo(currentNotice.getNoticeNo())
				.noticeTitle(currentNotice.getNoticeTitle())
				.noticeContent(currentNotice.getNoticeContent())
				.noticeImagePath(currentNotice.getNoticeImagePath())
				.noticeReg(currentNotice.getNoticeReg())
				.prevNo(prevNotice != null ? prevNotice.getNoticeNo() : null)
				.nextNo(nextNotice != null ? nextNotice.getNoticeNo() : null)
				.build();
	}

	// 검색결과 페이징
	public Page<NoticeDto> selectSearched(String keyword, Pageable pageable){
		int pageSize = 10; // 한 페이지당 공지 수 설정
		return noticeRepository.findByNoticeTitleContainingOrderByNoticeNoDesc(
						keyword, PageRequest.of(pageable.getPageNumber(), pageSize))
				.map(Notice::toDto);
	}

	// 중요 공지 목록 읽기
	public List<NoticeDto> getCheckedNoticeList() {
		return noticeRepository.getCheckedNoticeList()
				.stream().map(Notice::toDto).collect(Collectors.toList());
	}

	// 공지 등록
	public Map<String, Object> insert(NoticeDto dto, MultipartFile image) {
		Map<String, Object> response = new HashMap<>();
		try {
			// 중요 공지 제한 검사
			if (dto.isNoticeCheck() && noticeRepository.countNoticesWithNoticeCheck() >= 5) {
				response.put("isSuccess", false);
				response.put("message", "중요 공지는 5개를 넘을 수 없습니다.");
				return response;
			}

			// 새 공지 생성
			Notice newData = Notice.of(dto);
			newData.setNoticeNo(noticeRepository.findMaxNoticeNo() != null
					? noticeRepository.findMaxNoticeNo() + 1 : 1);

			// 이미지 업로드 처리 (Cloudinary 이용)
			if (image != null && !image.isEmpty()) {
				String imageUrl = imageProces.uploadImage(image);
				newData.setNoticeImagePath(imageUrl); // Cloudinary에서 받은 URL 저장
			}

			// 공지 저장
			noticeRepository.save(newData);

			response.put("isSuccess", true);
			response.put("message", "공지를 작성하였습니다.");
		} catch (Exception e) {
			response.put("isSuccess", false);
			response.put("message", "공지 작성 중 오류 발생: " + e.getMessage());
		}
		return response;
	}

	// 방금 작성한 공지번호 가져오기
	public Map<String, Object> selectLatestNo() {
		return Map.of("noticeNo", noticeRepository.findFirstByOrderByNoticeNoDesc().getNoticeNo());
	}

	// 공지 삭제
	@Transactional
	public String deleteNotice(int noticeNo) {
		if (!noticeRepository.existsById(noticeNo)) {
			return "해당 번호의 공지가 존재하지 않습니다.";
		}

		// 공지 삭제 (이미지 삭제는 Cloudinary 관리 도구 사용 권장)
		noticeRepository.deleteById(noticeNo);

		return "isSuccess";
	}
}