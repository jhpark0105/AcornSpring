package com.erp.controller.branch;

import com.erp.dto.CommentDto;
import com.erp.process.branch.CommentProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentProcess commentProcess;

    // 특정 공지사항의 댓글 트리 조회
    @GetMapping("/{noticeNo}")
    public ResponseEntity<List<CommentDto>> getCommentsTreeByNotice(@PathVariable int noticeNo) {
        List<CommentDto> commentsTree = commentProcess.getCommentsTreeByNotice(noticeNo);
        return ResponseEntity.ok(commentsTree);
    }

    // 특정 댓글 조회
    @GetMapping("/detail/{commentNo}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable int commentNo) {
        return ResponseEntity.ok(commentProcess.getCommentDtoById(commentNo));
    }

    // 댓글 작성 및 답글 작성
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentProcess.createComment(commentDto));
    }

    // 댓글 수정
    @PutMapping("/{commentNo}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable int commentNo,
            @RequestBody CommentDto updatedCommentDto
    ) {
        return ResponseEntity.ok(commentProcess.updateComment(commentNo, updatedCommentDto.getContent()));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentNo) {
        commentProcess.deleteComment(commentNo);
        return ResponseEntity.noContent().build();
    }
}