package com.erp.controller.branch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.NoticeDto;
import com.erp.process.branch.NoticeProcess;

@RestController
@RequestMapping("/notice")
@CrossOrigin(origins = "http://localhost:3000")
public class NoticeController {
	@Autowired
	private NoticeProcess noticeProcess;
	
	// 전체 페이징 및 조회
	@GetMapping
	public Page<NoticeDto> readAll(Pageable pageable){
		return noticeProcess.selectAll(pageable);
	}
	
	// 공지 상세
	@GetMapping("/{no}")
	public NoticeDto readOne(@PathVariable("no")int noticeNo) {
		return noticeProcess.selectOne(noticeNo);
	}
	
	// 검색결과 페이징 및 조회
	@GetMapping("/search")
	public Page<NoticeDto> readSearched(
		@RequestParam(required = false, value = "keyword") String noticeTitle, Pageable pageable) {
		if(noticeTitle == null || noticeTitle.isEmpty()) { // 검색어를 입력하지 않았을 경우
			return noticeProcess.selectAll(pageable); // 전체 공지 페이징
		} else { // 검색어를 입력했을 경우
			return noticeProcess.selectSearched(noticeTitle, pageable); // 검색결과 페이징
		}
	}
}
