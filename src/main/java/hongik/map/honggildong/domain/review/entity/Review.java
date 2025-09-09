package hongik.map.honggildong.domain.review.entity;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    private String content;

    private Integer rating;

    //이미지 엔티티 or elementCollection 결정 필요
}
