package hongik.map.honggildong.domain.member.service;

import hongik.map.honggildong.domain.member.dto.MemberRequestDTO;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.security.service.CustomUserDetails;

public interface MemberService {
    Member getMemberByUserDetails(CustomUserDetails userDetails);

    Member updateProfile(Member member, MemberRequestDTO.UpdateProfile request);

    void deleteMember(Member member);
}
