package hongik.map.honggildong.domain.review.entity;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.dto.ReviewRequestDTO;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
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

    private String content;

    private Double rating;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "review_image", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    @OrderColumn(name="image_order")
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @Builder.Default
    private Long likedCnt = 0L;

    public Review update(ReviewRequestDTO.create request, List<String> photoList) {
        this.content = request.getContent();
        this.rating = request.getRating();
        this.images = photoList;

        return this;
    }

    public Long updateLikedCnt(Boolean isLiked){
        if(isLiked){
            this.likedCnt++;
        }else {
            this.likedCnt--;
        }
        return this.likedCnt;
    }
}
