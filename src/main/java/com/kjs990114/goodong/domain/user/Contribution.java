package com.kjs990114.goodong.domain.user;

import com.kjs990114.goodong.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "contribution")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"contId", "date"})})
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contId;

    private LocalDate date = LocalDate.now();

    @ManyToOne
    private User user;

    private Short count = 1;

}
