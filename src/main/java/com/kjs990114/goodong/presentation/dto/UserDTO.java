package com.kjs990114.goodong.presentation.dto;

import com.kjs990114.goodong.domain.user.Contribution;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class UserDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Register {
        @Email(message = "Invalid email format")  // 이메일 형식 검사
        @NotBlank(message = "Email cannot be blank")  // 빈 값 허용하지 않음
        private String email;

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")  // 비밀번호 최소 길이 설정
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$",  // 소문자와 숫자 필수, 대문자와 특수문자는 선택
                message = "Password must contain at least one lowercase letter and one digit"
        )
        private String password;

        @NotBlank(message = "Nickname cannot be blank")
        private String nickname;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserSummary {
        private Long userId;
        private String email;
        private String nickname;
        private String profileImage;
    }
    //유저 상세 정보
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class UserDetail {
        private Long userId;
        private String email;
        private String nickname;
        private String profileImage;
        @Builder.Default
        private int followerCount = 0;
        @Builder.Default
        private int followingCount = 0;
        @Builder.Default
        private Boolean followed = false;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserContributionList {
        List<Contribution> contributions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserContribution {
        LocalDate date;
        int count;
    }

    @Getter
    @Setter
    public static class UpdateUser {
        private MultipartFile profileImage;
        private String nickname;
    }

    @Getter
    @Setter
    public static class Password{
        private String password;
    }

}
