package hongik.map.honggildong.domain.edge.entity;

import hongik.map.honggildong.domain.node.entity.Node;
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
public class Edge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_node_id")
    private Node startNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_node_id")
    private Node endNode;

    //출발 노드와 도착 노드의 위도/경도/고도 값 비교하여 가중치 계산
    @Builder.Default
    private Double cost = 0.0;
}
