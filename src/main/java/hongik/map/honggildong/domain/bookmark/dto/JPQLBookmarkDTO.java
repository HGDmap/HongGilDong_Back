package hongik.map.honggildong.domain.bookmark.dto;

public class JPQLBookmarkDTO {

    //검색된 결과에 함께 반환되는 북마크된 빌딩/시설 id 리스트
    public interface SearchResult{
        String getType();
        Long getId();
    }
}
