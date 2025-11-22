package hongik.map.honggildong.domain.member.dto;

import lombok.Getter;

public class MemberRequestDTO {
    @Getter
    public static class Join{

    }

    @Getter
    public static class UpdateProfile{
        private String nickname;
        private String profilePic;
    }
}
