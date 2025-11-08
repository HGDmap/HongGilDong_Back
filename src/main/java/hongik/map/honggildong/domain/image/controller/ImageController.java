package hongik.map.honggildong.domain.image.controller;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.image.dto.ImageRequestDTO;
import hongik.map.honggildong.domain.image.dto.ImageResponseDTO;
import hongik.map.honggildong.domain.image.service.ImageService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;
    private final FacilityRepository facilityRepository;

    @PostMapping("/presigned-url")
    @Operation(summary = "s3 업로드 용 링크 받기", description = "여기서 받은 presignedUrl로 put 요청 보낼것, imageUrl들을 리뷰 등록 api에서 복붙하여 사용")
    public ApiResponse<List<ImageResponseDTO.PresignedDTO>> getPresignedUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestBody ImageRequestDTO.UploadImageDTO request){
        List<ImageResponseDTO.PresignedDTO> body = imageService
                .uploadReviewImages(request.getFacilityId(),
                        request.getFileNames(),
                        userDetails.getMember());

        return ApiResponse.onSuccess(body);
    }
}
