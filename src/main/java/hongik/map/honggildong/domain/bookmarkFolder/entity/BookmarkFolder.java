package hongik.map.honggildong.domain.bookmarkFolder.entity;

import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity @Getter
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

    //북마크 시설 추가? 양방향으로? 아님 굳이 필요X?
}
