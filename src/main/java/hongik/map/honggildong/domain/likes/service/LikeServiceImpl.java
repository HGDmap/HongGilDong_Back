package hongik.map.honggildong.domain.likes.service;

import hongik.map.honggildong.domain.likes.converter.LikeConverter;
import hongik.map.honggildong.domain.likes.entity.Likes;
import hongik.map.honggildong.domain.likes.repository.LikeRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.converter.ReviewConverter;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import hongik.map.honggildong.domain.review.repository.ReviewRepository;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;


    @Override
    public ReviewResponseDTO.MyLikedGeneralPage getLikedReviewListOf(Member member, Pageable pageable) {

        Page<Likes> likes = likeRepository.findAllLikedReviewsByMember(member,pageable);


        return ReviewConverter.toMyLikedGeneralPageDTO(likes, member.getId());
    }

    @Override
    @Transactional
    public Boolean createOrDeleteLikeOf(Member member, Review review) {

        Optional<Likes> optionalLike = likeRepository.findByMemberIdAndReviewId(member.getId(),review.getId());

        Boolean finalResult;
        //상태가 어떻든 좋아요 이력 조회가 되면
        if(optionalLike.isPresent()){
            Likes like = optionalLike.get();
            //현재 상태와 반대되는 상태로 업데이트
            finalResult = like.setStatusOpposite();
            System.out.println("허출");
        }else{
            Likes like = LikeConverter.toLikes(member,review,true);
            finalResult = likeRepository.save(like).getStatus();
        }

        review.updateLikedCnt(finalResult);

        return finalResult;
    }
}
