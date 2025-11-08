package hongik.map.honggildong.domain.review.entity;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder @Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    private String title; //건물 이름
    private String content;

    private Integer rating;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "review_image", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    @OrderColumn(name="image_order")
    @Builder.Default
    private List<String> images = new ArrayList<String>();

    @Builder.Default
    private Long likedCnt = 0L;
}
