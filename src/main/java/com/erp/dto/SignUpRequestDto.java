package com.erp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// SignUpRequestDto : 회원가입 시 클라이언트로부터 데이터를 받아오는 DTO
@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {
	
	@NotBlank // null, 공백 허용 안함
	@Pattern(regexp ="^[A-Za-z][A-Za-z0-9]{2,20}$") // 2자 이상 20자 이하, 영대소문자와 숫자만 포함
	private String adminId;
	
	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,20}$") // 최소 8자 이상 최대 20자 이하, 숫자, 특수문자, 영문자가 포함
	private String adminPw;
	
	private String adminRole = "ROLE_ADMIN"; // 기본값 설정
	
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z가-힣]{2,20}$") // 2자 이상 20자 이하, 한글과 영어만 입력
	private String adminName;
	
	@NotBlank
//	@Past // 과거 날짜만 허용
	@Pattern(regexp = "^\\d{8}$") // 숫자 8자리로 입력
	private String adminBirth;
	
	@NotBlank
	@Email
	//@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") // @Email 때문에 필요없을 듯, 혹시 몰라 남겨둡니다.
	private String adminEmail;
	
	@NotBlank
	@Pattern(regexp ="^[0-9]{11}$") // 숫자 11자리만 허용
	private String adminPhone;
	
	@NotBlank
	private String adminPostcode;
	
	@NotBlank
	private String adminAddress1;
	
	@NotBlank
	private String adminAddress2;
	
	@NotBlank
	private String adminTerm1;
	
	@NotBlank
	private String adminTerm2;
	
	private String adminTerm3;
}
