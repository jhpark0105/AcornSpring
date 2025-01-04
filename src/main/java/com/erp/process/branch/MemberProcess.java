package com.erp.process.branch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.erp.dto.MemberDto;
import com.erp.entity.Member;
import com.erp.repository.MemberRepository;

@Repository
public class MemberProcess {
	@Autowired
	private MemberRepository repository;
	
	public List<MemberDto> getAllList() {
		return repository.findAll()
				.stream()
				.map(MemberDto :: fromEntity)
				.collect(Collectors.toList());
	}
	
	@Transactional
	public Map<String, Object> insert(MemberDto dto) {
		// 입력한 번호 중복 확인 
		 Map<String, Object> response = new HashMap<String, Object>() ;
		try {
			repository.findById(dto.getMemberId()).get();
			response.put("Success", false);
			response.put("message", "이미 등록된 사번입니다.");
			
			return response;
		} catch(Exception e) {
			//  findById의 결과가 error인 경우 (등록가능)
			try {
				Member newData = Member.fromDto(dto);
				repository.save(newData);
				response.put("Success", true);
				response.put("message", "직원을 등록하였습니다.");
				
				return response;
			} catch (Exception e2) {
				response.put("Success", false);
				response.put("message", "입력자료 오류 입니다."+e2);
				return response;
			}
		}
	}
	
	// 수정 삭제를 위한 데이터 1개 읽기
	public Member getData(String memberId) {
		Member member = repository.findByMemberId(memberId);
		return member;
	}
	
	// 수정
	@Transactional
	public String update(MemberDto dto) {
		try {
			Member member = new Member(dto.getMemberId(),
					dto.getMemberName(),
					dto.getMemberPassword(),
					dto.getMemberJob(),
					dto.getMemberDate(),
					dto.getMemberTel(),
					dto.getMemberCnt(),
					dto.getBranchCode());
			repository.save(member);
			return "Success";
		}
		catch(Exception e) {
			return "수정 자료 오류입니다 " +e.getMessage();
		}
	}
	
	// 삭제
	@Transactional
	public String delete(String memberId) {
		try {
			repository.deleteById(memberId);
			return "Success";
		} catch (Exception e) {
			return "삭제 작업 오류입니다" + e.getMessage();
		}
	}
	
	
}




