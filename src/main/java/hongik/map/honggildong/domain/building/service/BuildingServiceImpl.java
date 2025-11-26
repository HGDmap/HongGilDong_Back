package hongik.map.honggildong.domain.building.service;

import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.building.converter.BuildingConverter;
import hongik.map.honggildong.domain.building.dto.BuildingResponseDTO;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.building.repository.BuildingRepository;
import hongik.map.honggildong.domain.facility.converter.FacilityConverter;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.domain.node.repository.NodeRepository;
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
@Transactional(readOnly = true)
public class BuildingServiceImpl implements BuildingService{

    private final BuildingRepository buildingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final NodeRepository nodeRepository;


    @Override
    public BuildingResponseDTO.Detail getBuildingDetails(Long buildingId, CustomUserDetails userDetails) {
        Building building = buildingRepository.findWithFacilities(buildingId)
                .orElseThrow(()->new GeneralException(ErrorStatus.BUILDING_NOT_FOUND));


        //로그인 되어있지 않으면 항상 false
        Boolean isBookmarked = userDetails!=null ? bookmarkRepository.existsByMemberAndBuilding(userDetails.getMember(), building):false;

        List<String> photoList = new ArrayList<>();

        if (building.getMainImg() != null) {
            photoList.add(building.getMainImg());
        }
        //photoList.add(building.getMainImg2());
        //photoList.add(building.getMainImg3());

        return BuildingConverter.toDetailDTO(building, isBookmarked, photoList);
    }
}
