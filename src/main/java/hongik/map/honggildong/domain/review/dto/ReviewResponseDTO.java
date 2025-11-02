package hongik.map.honggildong.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDTO {
    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General {
        private Long id;
        private String content;
        private Long writerId;
        private String writerNickname;
        private String writerProfilePic;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<String> photoList;
        private Boolean isLiked;
    }

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneralPage {
        private List<General> reviewList;
        private int totalPages;
        private int size;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;

    }

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyGeneral {
        private Long id;
        private String title;
        private String content;
        private Integer rating;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<String> photoList;
        private Boolean isLiked;
        private Long likedCnt;
    }

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyGeneralPage {
        private List<MyGeneral> reviewList;
        private int totalPages;
        private int size;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;

    }



}
