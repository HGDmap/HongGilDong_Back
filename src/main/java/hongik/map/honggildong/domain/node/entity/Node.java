package hongik.map.honggildong.domain.node.entity;

import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Node extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    private NodeCode code;

    private Double latitude;
    private Double longitude;
    //상대 고도
    private Long height;
    //건물 내 층수
    private Floor floor;

}
