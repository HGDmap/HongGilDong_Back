package hongik.map.honggildong.domain.member.service;

import hongik.map.honggildong.domain.member.dto.MemberRequestDTO;
import hongik.map.honggildong.domain.member.entity.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Override
    public Member getMemberByUserDetails(UserDetails userDetails) {
        return null;
    }

    @Override
    public Member updateProfile(Member member, MemberRequestDTO.UpdateProfile request) {
        return null;
    }

    @Override
    public void deleteMember(Member member) {

    }
}
