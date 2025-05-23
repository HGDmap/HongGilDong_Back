package hongik.map.honggildong.domain.member.service;

import hongik.map.honggildong.domain.member.dto.MemberRequestDTO;
import hongik.map.honggildong.domain.member.entity.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    Member getMemberByUserDetails(UserDetails userDetails);

    Member updateProfile(Member member, MemberRequestDTO.UpdateProfile request);

    void deleteMember(Member member);
}
