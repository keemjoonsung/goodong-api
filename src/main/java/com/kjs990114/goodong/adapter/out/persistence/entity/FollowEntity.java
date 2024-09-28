package com.kjs990114.goodong.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "follow")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id", "followee_id"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "followee_id")
    private UserEntity followee;
}