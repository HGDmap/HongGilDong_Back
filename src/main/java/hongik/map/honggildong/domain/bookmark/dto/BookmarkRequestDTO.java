package hongik.map.honggildong.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkRequestDTO {

    public static class General {
        private String folderName;
        private String folderColor;
    }
}
