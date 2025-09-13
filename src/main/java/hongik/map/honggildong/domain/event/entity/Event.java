package hongik.map.honggildong.domain.event.entity;

import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Node node;

    private String name;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
