package com.erp.dto;

import com.erp.entity.AuthorType;
import com.erp.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private int commentNo; //댓글 고유 번호
    private int noticeNo; //공지 번호
    private Integer parentNo; //부모 댓글 번호
    private AuthorType authorType; //작성자 유형(관리자/고객)
    private int authorId; //작성자 ID
    private String content; //댓글 내용
    private LocalDateTime createdAt; //댓글 작성 시간
    private LocalDateTime updatedAt; //댓글 수정 시간
    private List<CommentDto> replies = new ArrayList<>(); // 답글 리스트 > 빈 리스트로 초기화

    // 답글 추가 메서드
    public void addReply(CommentDto reply) {
        if (this.replies == null) {
            this.replies = new ArrayList<>();
        }
        this.replies.add(reply);
    }

    //Entity -> DTO 변환 메서드
    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .commentNo(comment.getCommentNo())
                .noticeNo(comment.getNoticeNo())
                .parentNo(comment.getParentNo())
                .authorType(comment.getAuthorType())
                .authorId(comment.getAuthorId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .replies(new ArrayList<>()) // 반드시 빈 리스트로 초기화
                .build();
    }

    //DTO -> Entity 변환 메서드
    public Comment toEntity() {
        return Comment.builder()
                .commentNo(this.commentNo)
                .noticeNo(this.noticeNo)
                .parentNo(this.parentNo)
                .authorType(this.authorType)
                .authorId(this.authorId)
                .content(this.content)
                .build();
    }
}