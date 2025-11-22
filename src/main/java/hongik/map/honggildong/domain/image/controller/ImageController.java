package hongik.map.honggildong.domain.image.controller;

import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.image.dto.ImageRequestDTO;
import hongik.map.honggildong.domain.image.dto.ImageResponseDTO;
import hongik.map.honggildong.domain.image.dto.RequestType;
import hongik.map.honggildong.domain.image.service.ImageService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "S3 이미지")
public class ImageController {
    private final ImageService imageService;
    private final FacilityRepository facilityRepository;

    @PostMapping("/review/presigned-url")
    @Operation(summary = "리뷰 용 s3 업로드 링크 받기", description = "여기서 받은 presignedUrl로 put 요청 보낼것, imageUrl들을 리뷰 등록 api에서 복붙하여 사용")
    public ApiResponse<List<ImageResponseDTO.PresignedDTO>> getReviewPresignedUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestBody ImageRequestDTO.UploadReviewImageDTO request){
        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        List<ImageResponseDTO.PresignedDTO> body = imageService
                .uploadReviewImages(request.getFacilityId(),
                        request.getFileNames(),
                        userDetails.getMember());

        return ApiResponse.onSuccess(body);
    }

    @PostMapping("/presigned-url/{type}")
    @Operation(summary = "리뷰 외 다른 용도의 s3 업로드 링크 받기", description = "여기서 받은 presignedUrl로 put 요청 보낼것, imageUrl들을 리뷰 등록 api에서 복붙하여 사용")
    public ApiResponse<List<ImageResponseDTO.PresignedDTO>> getOtherPresignedUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @PathVariable("type") RequestType type,
                                                                            @RequestBody ImageRequestDTO.UploadOtherImageDTO request){

        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }
        List<ImageResponseDTO.PresignedDTO> body = imageService
                .uploadGeneralImages(type,
                        request.getId(),
                        request.getFileNames());

        return ApiResponse.onSuccess(body);
    }
}
