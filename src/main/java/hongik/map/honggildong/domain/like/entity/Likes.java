package hongik.map.honggildong.domain.like.entity;

import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Likes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
