package com.kjs990114.goodong.presentation.dto;

import com.kjs990114.goodong.domain.user.Contribution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class UserDTO {

    @Getter
    @Setter
    public static class Register {
        private String email;
        private String password;
        private String nickname;
    }
    //간단한 유저 정보
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Summary {
        private Long userId;
        private String email;
        private String nickname;
    }
    //유저 상세 정보
    @Getter
    @Builder
    public static class Detail {
        private Long userId;
        private String email;
        private String nickname;
        private String profileImage;
        private int followerCount;
        private int followingCount;
        private List<Contribution> contributions;
    }

    @Getter
    @Setter
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Contribution {
        LocalDate date;
        int count;
    }

    @Getter
    @Setter
    public static class Update {
        private String nickname;
        private String profileImage;
    }

    @Getter
    @Setter
    public static class Password{
        private String password;
    }

}
