package hongik.map.honggildong.domain.facility.entity;

import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.node.entity.Floor;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Entity @Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Facility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Node node;

    @OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    private String name;
    private String alias;
    private String locationDetail;

    @Enumerated(EnumType.STRING)
    private FacilityType type;
    @Enumerated(EnumType.STRING)
    @Nullable
    private HashTag hashTag;

    @Nullable
    private String phone;

    @Nullable
    private String link;

    @Nullable
    private String openInfo;

    @Nullable
    private String mainImg;
    @Nullable
    private String mainImg2;
    @Nullable
    private String mainImg3;

    @Builder.Default
    private Long restCnt = 0L;

    @Builder.Default
    private Long studyCnt = 0L;

    @Builder.Default
    private Long viewCnt = 0L;

    @Builder.Default
    private Long meetingCnt = 0L;

    @Builder.Default
    private Long foodCnt = 0L;

    public Facility addRecommendCnt(HashTag hashTag) {
        switch (hashTag){
            case FOOD : foodCnt++; break;
            case MEETING : meetingCnt++; break;
            case STUDY : studyCnt++; break;
            case REST : restCnt++; break;
            case VIEW : viewCnt++; break;
        }

        return this;
    }



}
