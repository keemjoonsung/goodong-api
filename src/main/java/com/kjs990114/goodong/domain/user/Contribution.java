package com.kjs990114.goodong.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "contribution")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"cont_id", "date"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contId;

    private LocalDate date = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer count = 1;

}
