package hongik.map.honggildong.domain.review.dto.jpql;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class JPQLReviewAndWriter {

    private Long reviewId;
    private String Content;
    private Long WriterId;
    private String WriterNickname;
    private String WriterProfilePic;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
    //private List<String> PhotoList;



}
