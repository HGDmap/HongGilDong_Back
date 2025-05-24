package hongik.map.honggildong.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class MemberResponseDTO {
    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General{
        private Long id;
    }
}
