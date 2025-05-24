package hongik.map.honggildong.domain.facility.converter;

import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;

public class FacilityConverter {

    public static FacilityResponseDTO.Detail toDetailDTO(Facility facility) {
        return null;
    }

    //추후에 북마크 converter 로 옮기는 것 고려
    public static FacilityResponseDTO.BookmarkStatus toBookmarkStatusDTO(Bookmark bookmark) {
        return null;
    }
}
