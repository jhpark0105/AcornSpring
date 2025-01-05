package com.erp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "comment_no")
    private int commentNo; //댓글 고유 번호

    @Column(name = "notice_no", nullable = false)
    private int noticeNo; //공지 번호

    @Column(name = "parent_no")
    private Integer parentNo; //부모 댓글 번호

    @Enumerated(EnumType.STRING)
    @Column(name = "author_type", nullable = false)
    private AuthorType authorType; //작성자 유형(관리자/고객)

    @Column(name = "author_id", nullable = false)
    private int authorId; //작성자 ID

    @Column(name = "content", nullable = false, length = 1000)
    private String content; //댓글 내용

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; //댓글 작성 시간

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //댓글 수정 시간
}