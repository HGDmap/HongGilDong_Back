package hongik.map.honggildong.domain.member.service;

import hongik.map.honggildong.domain.member.dto.MemberRequestDTO;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.member.repository.MemberRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberByUserDetails(CustomUserDetails userDetails) {
        return memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(()->new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public Member updateProfile(Member member, MemberRequestDTO.UpdateProfile request) {
        return null;
    }

    @Override
    public void deleteMember(Member member) {

    }
}
