package hongik.map.honggildong.domain.member.entity;

import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.likes.entity.Likes;
import hongik.map.honggildong.domain.review.entity.Review;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password; //hash값으로 저장
    //전공 강의동 나중에 Enum으로 할건지 의논 필요
    private String nickname;
    private String profilePic;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkFolder> bookmarkFolders;

}
