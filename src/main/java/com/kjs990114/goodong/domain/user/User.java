package com.kjs990114.goodong.domain.user;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import com.kjs990114.goodong.domain.post.Comment;
import com.kjs990114.goodong.domain.post.Like;
import com.kjs990114.goodong.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String profileImage;

    @Builder.Default
    private Role role = Role.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes =  new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Contribution> contributions = new ArrayList<>();

    //내가 팔로우하고있는 사람들이니까, follow 테이블에서 follower 를 FK로 가지면된다.
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Follow> followings = new HashSet<>();

    //나의 팔로워니까, following 테이블에서 , following 을 FK로 가지면된다.
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Follow> followers = new HashSet<>();

    public enum Role {
        USER,
        ADMIN
    }
    //내가 팔로우하는 사람 추가
    public void follow(Follow follow) {
        this.followings.add(follow);
    }
    //내가 팔로우하는 사람 삭제
    public void unfollow(Follow follow) {
        this.followings.remove(follow);
    }
    //나의 팔로워 추가
    public void addFollower(Follow follower) {
        this.followers.add(follower);
    }
    //나의 팔로워 삭제
    public void deleteFollower(Follow follower) {
        this.followers.remove(follower);
    }
    //댓글 추가
    public void addComment(Comment comment) {
        comments.add(comment);
    }
    // 댓글 삭제
    public void removeComment(Comment comment) {
        comments.remove(comment);
    }
    // 좋아요 추가
    public void addLike(Like like) {
        this.likes.add(like);  // 좋아요 리스트에 추가
    }
    //좋아요 삭제
    public void deleteLike(Like like) {
        this.likes.remove(like);
    }
    //: 유저 정보 업데이트
    public void updateProfile(String nickname, String profileImage) {
        if(nickname != null) this.nickname = nickname;
        if(profileImage != null) this.profileImage = profileImage;
    }
    // 비밀번호 변경
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    //Contribution 추가 또는 count 증가
    public void updateContribution(Contribution contribution) {
        Contribution existedCont = contributions.stream().filter(cont -> cont.getDate().equals(contribution.getDate())).findFirst().orElse(null);

        if(existedCont != null) {
            existedCont.setCount((short)(existedCont.getCount() + 1));
        }else{
            contributions.add(contribution);
        }

    }

}
