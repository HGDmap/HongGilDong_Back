package hongik.map.honggildong.domain.building.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BuildingResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail{
        private Long id;
        private String name;
        private String type;
        private Long nodeId;
        private String nodeName;
        private String open;
        private String phone;
        private String link;
        private String description;
        private Double latitude;
        private Double longitude;
        private List<String> photoList;
        private Boolean isBookmarked;

    }
}
