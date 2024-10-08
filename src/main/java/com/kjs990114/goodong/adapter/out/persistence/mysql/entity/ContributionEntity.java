package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "contribution")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"cont_id", "date"})})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContributionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contId;

    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Integer count = 1;

}
