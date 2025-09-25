package hongik.map.honggildong.domain.member.converter;

import hongik.map.honggildong.domain.member.dto.MemberResponseDTO;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.security.dto.AuthRequestDTO;

public class MemberConverter {
    public static MemberResponseDTO.General toGeneralDTO(Member member) {
        return null;
    }
    public static Member toMember(String realName, String nickname, String email, String encodedPW) {
        return Member.builder()
                .nickname(nickname)
                .password(encodedPW)
                .name(realName)
                .email(email)
                .build();
    }
}
