package com.kjs990114.goodong.domain.user;


import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity.Role;
import com.kjs990114.goodong.domain.social.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long userId;
    private String nickname;
    private String email;
    private String password;
    private String profileImage;
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private List<Contribution> contributions = new ArrayList<>();

    public static User of(Long userId){
        return User.builder().userId(userId).build();
    }

    // 닉네임 변경
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 이미지 변경
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // 비밀번호 변경
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 기여도 추가 또는 카운트 증가
    public void updateContribution() {
        Contribution existedContribution = contributions.stream()
                .filter(cont -> cont.getDate().equals(LocalDate.now()))
                .findFirst()
                .orElse(null);

        if (existedContribution != null) {
            existedContribution.setCount(existedContribution.getCount() + 1);
        } else {
            contributions.add(
                    Contribution.builder()
                            .user(this)
                            .date(LocalDate.now())
                            .count(1)
                            .build()
            );
        }
    }
}