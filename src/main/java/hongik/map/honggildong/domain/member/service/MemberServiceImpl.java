package hongik.map.honggildong.domain.member.service;

import hongik.map.honggildong.domain.image.service.ImageService;
import hongik.map.honggildong.domain.member.dto.MemberRequestDTO;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.member.repository.MemberRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ImageService imageService;
    private final MemberRepository memberRepository;

    @Override
    public Member getMemberByUserDetails(CustomUserDetails userDetails) {
        return memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(()->new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public Member updateProfile(Member member, MemberRequestDTO.UpdateProfile request) {

        String originalPic = member.getProfilePic();
        String newPic = request.getProfilePic();
        //현재 프사와 동일하지 않으면 현재 프사 삭제
        if(originalPic!=null&&!newPic.equals(originalPic)) {
            imageService.deleteOneImage(member.getProfilePic());
        }
        //요청이 빈 문자열이 아니면 s3에 등록된 이미지로 교체
        return member.updateProfile(request.getNickname(),request.getProfilePic());
    }

    @Override
    public void deleteMember(Member member) {

    }
}
