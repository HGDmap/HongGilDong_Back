package hongik.map.honggildong.domain.facility.entity;

import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity @Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Facility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Node node;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookmarkFolder bookmarkFolder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    private String name;
    private String alias;
    private String locationDetail;

    @Enumerated(EnumType.STRING)
    private FacilityType type;
    @Enumerated(EnumType.STRING)
    private HashTag hashTag;
    //건물 내 층수
    @Enumerated(EnumType.STRING)
    private Floor floor;

    private String phone;

    private String openInfo;

    private String mainImg;

}
