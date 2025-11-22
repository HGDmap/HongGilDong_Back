package hongik.map.honggildong.domain.image.service;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.image.dto.ImageResponseDTO;
import hongik.map.honggildong.domain.image.dto.RequestType;
import hongik.map.honggildong.domain.member.entity.Member;

import java.util.List;

public interface ImageService {

    List<ImageResponseDTO.PresignedDTO> uploadReviewImages(Long facilityId, List<String> fileNames, Member member);

    List<String> getTopNImageOfFacility(Facility facility, Integer size);

    ImageResponseDTO.ImagePage getPhotoPageOfFacility(Facility facility, String continuationToken, int size);

    void deleteImages(List<String> images);

    List<ImageResponseDTO.PresignedDTO> uploadGeneralImages(RequestType type, Long id, List<String> fileNames);

    void deleteOneImage(String image);
}
