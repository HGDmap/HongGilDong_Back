package hongik.map.honggildong.domain.facility.entity;

import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Facility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Node node;

    private String name;
    private String locationDetail;

    @Enumerated(EnumType.STRING)
    private FacilityType type;
    @Enumerated(EnumType.STRING)
    private HashTag hashTag;

    private String phone;

    private String openInfo;

    private String mainImg;

}
