package hongik.map.honggildong.domain.bookmarkFolder.entity;

import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity @Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkFolder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "bookmarkFolder", fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks = new ArrayList<>();
}
