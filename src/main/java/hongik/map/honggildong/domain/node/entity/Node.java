package hongik.map.honggildong.domain.node.entity;

import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Node extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @Enumerated(EnumType.STRING)
    private NodeCode code;

    private Double latitude;
    private Double longitude;
    //상대 고도
    private Long height;

    @Nullable
    private String mainImg;

}
