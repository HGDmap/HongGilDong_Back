package hongik.map.honggildong.domain.building.entity;

import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Building extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double latitude;
    private Double longitude;

    private String mainImg;

    @OneToOne
    private Node mainNode;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookmarkFolder bookmarkFolder;
}
