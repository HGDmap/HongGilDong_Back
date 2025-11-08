package hongik.map.honggildong.domain.image.service;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.image.converter.ImageConverter;
import hongik.map.honggildong.domain.image.dto.ImageResponseDTO;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private final FacilityRepository facilityRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");


    @Override
    @Transactional
    public List<ImageResponseDTO.PresignedDTO> uploadReviewImages(Long facilityId, List<String> fileNames, Member member) {


        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(()->new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));

        Long buildingId = facility.getBuilding().getId();
        Long memberId = member.getId();

        List<ImageResponseDTO.PresignedDTO> urls = new ArrayList<>();

        for(String fileName : fileNames) {
            //fileName : "image.png", "image.jpg" ...
            String baseName = fileName.substring(0, fileName.lastIndexOf('.')); //image
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1); // png
            // 현재 시각 + UUID 포함한 키 생성
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String uniqueKey = timestamp + "_" + UUID.randomUUID() + "-" + baseName + "." + extension;

            //image/review/building/2/facility/3/user/13/랜덤숫자/image.png
            String fullPath = "image/review/building/" + buildingId + "/facility/" + facilityId+ "/user/"+memberId+"/"+uniqueKey + "-" + baseName + "." + extension;

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fullPath)
                    .contentType("image/"+extension)
                    .build();
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5))
                    .putObjectRequest(objectRequest)
                    .build();
            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            String imageUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fullPath;

            urls.add(ImageConverter.toDTO(presignedRequest.url(),imageUrl));
        }

        return urls;
    }

    /**
     * for문 돌면서 하기에 네트워크 낭비가 심한것같아서 일단 사용안하는걸로 함
     * 혹시 나중에 사용할 일 있을까봐 일단 남겨둠
     */
    @Override
    public List<String> getTopNImageOfFacility(Facility facility, Integer size) {
        List<String> topNImages = new ArrayList<>();

        Long buildingId = facility.getBuilding().getId();
        Long facilityId = facility.getId();

        String prefix = "image/review/building/" + buildingId + "/facility/" + facilityId + "/";

        if(size >0){
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .prefix(prefix)
                    .maxKeys(size)
                    .build();
            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            if (response.contents() == null || response.contents().isEmpty()) return topNImages;

            // 2. 조회된 객체에서 최근(LastModified 기준) 순으로 정렬 후 limit개 추출
            List<S3Object> objects = response.contents().stream()
                    .sorted(Comparator.comparing(S3Object::lastModified).reversed())
                    .toList();

            // 3. S3 객체 키를 실제 접근 가능한 URL로 변환
            objects.forEach(obj->topNImages.add("https://" + bucket + ".s3.amazonaws.com/" + obj.key()));
        }

        return topNImages;
    }

    @Override
    public ImageResponseDTO.ImagePage getPhotoPageOfFacility(Facility facility, String continuationToken, int pageSize) {

        String prefix = "image/review/building/" + facility.getBuilding().getId() + "/facility/" + facility.getId() + "/";


        ListObjectsV2Request request =ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .maxKeys(pageSize)
                .continuationToken(continuationToken)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        List<String> urls = response.contents().stream()
                .map(S3Object::key) // 객체 키 가져오기
                .map(key -> "https://" + bucket + ".s3.amazonaws.com/" + key) // URL 변환
                .collect(Collectors.toList());

        return ImageResponseDTO.ImagePage.builder()
                .imageList(urls)
                .continuationToken(response.nextContinuationToken())
                .isFirst(continuationToken == null)
                .isLast(!response.isTruncated())
                .pageSize(pageSize)
                .build();
    }
}
