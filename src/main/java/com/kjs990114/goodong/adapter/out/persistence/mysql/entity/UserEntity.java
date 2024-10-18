package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "is_available"}),
                @UniqueConstraint(columnNames = {"nickname", "is_available"})
        })
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profileImage;

    @Builder.Default
    private Role role = Role.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContributionEntity> contributions = new ArrayList<>();

    public enum Role {
        USER,
        ADMIN
    }

    public static UserEntity of(Long userId) {
        return UserEntity.builder().userId(userId).build();
    }
}