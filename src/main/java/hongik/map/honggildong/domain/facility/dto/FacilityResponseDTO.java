package hongik.map.honggildong.domain.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FacilityResponseDTO {
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private Long id;
        //상세 정보 조회 시 요청자가 bookmark 했는지 여부를 나타내는 필드도 필요
    }

    //추후에 bookmark dto 로 옮기는 것 고려
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkStatus {
        private Long facilityId;
        private Boolean isBookMarked;
    }
}
