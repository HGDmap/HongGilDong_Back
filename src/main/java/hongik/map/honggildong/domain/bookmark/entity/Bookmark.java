package hongik.map.honggildong.domain.bookmark.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //시설 관계 추가

    @ManyToOne
    @JoinColumn(name = "bookmakr_group_id")
    private BookmarkGroup bookmarkGroup;
}
