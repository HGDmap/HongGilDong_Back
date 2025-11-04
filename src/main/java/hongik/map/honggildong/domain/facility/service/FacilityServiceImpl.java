package hongik.map.honggildong.domain.facility.service;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {
    private final FacilityRepository facilityRepository;


    @Override
    public Facility getFacilityById(Long facilityId) {

        return facilityRepository.findById(facilityId).orElseThrow(()->new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));
    }
}
