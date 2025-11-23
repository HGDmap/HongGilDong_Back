package hongik.map.honggildong.global.redis.search.service;

import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.SearchResults;
import hongik.map.honggildong.domain.bookmark.dto.JPQLBookmarkDTO;
import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.redis.search.converter.SearchConverter;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;
import hongik.map.honggildong.global.redis.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{

    private final SearchRepository searchRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RedisModulesCommands<String, String> commands;
    private final FacilityRepository facilityRepository;

    public List<SearchResultDTO.AutoCompleteGeneral> autoComplete(String keyword) {
        String query = String.format("(@name:%s*) | (@alias:%s*)", keyword, keyword);
        SearchResults<String, String> results = commands.ftSearch("idx:search", query);


        return results.stream().map(doc -> SearchResultDTO.AutoCompleteGeneral
                .builder()
                .name(doc.get("name"))
                .type(doc.get("type"))
                .id(Long.valueOf(doc.get("ref_id")))
                .nodeId(Long.valueOf(doc.get("node_id")))
                .build()).toList();
    }

    /**
     * RediSearch 인덱스에 등록된 모든 문서를 조회합니다.
     */
    @Override
    public List<String> findAllIndexedData() {
        // "*" : 모든 문서 검색
        SearchResults<String, String> results = commands.ftSearch("idx:search", "*");

        // 결과를 List<Map<String, String>> 형태로 변환
        return results.stream().map(doc->doc.get("name")).toList();
    }

    @Override
    public SearchResultDTO.ResultList search(String query, Member member) {

        //빌딩, 시설, 이벤트, 노드 join 해서 search 후
        //각 엔티티의 이름, id, 좌표, 노드이름, 사진 등
        List<Object[]> rawResult = searchRepository.findAllType(query);

        //빌딩, 시설 별 id 모음
        List<Long> buildingIds = rawResult.stream()
                .filter(dto -> dto[0].equals("BUILDING"))
                .map(obj -> ((Number)obj[1]).longValue())
                .toList();

        List<Long> facilityIds = rawResult.stream()
                .filter(dto -> dto[0].equals("FACILITY"))
                .map(obj -> ((Number)obj[1]).longValue())
                .toList();

        List<Facility> facilities = facilityRepository.findAllById(facilityIds);

        Map<Long,Facility> facilityMap = facilities.stream()
                .collect(Collectors.toMap(
                        Facility::getId,
                        facility -> facility
                ));

        //interface(native query 프로젝션)->set
        List<JPQLBookmarkDTO.SearchResult> bookmarkedList = new ArrayList<>();
        if(member!=null){
            bookmarkedList = bookmarkRepository.findAllBookmarksFromSearch(member.getId(), buildingIds, facilityIds);
        }

        Set<Pair<String, Long>> bookmarkedSet = new HashSet<>();
        for(JPQLBookmarkDTO.SearchResult b : bookmarkedList){
            System.out.println("타입:"+b.getType()+"id:"+b.getId());
            bookmarkedSet.add(Pair.of(b.getType(),b.getId()));
        }

        List<SearchResultDTO.Result> body = rawResult.stream().map(raw->{
            //북마크 된 리스트에 존재하는 아이디라면 true, 비로그인자는 항상 false
            SearchResultDTO.Result finalResult = SearchConverter.toResultDTO(raw);
            if(member!=null){
                if(bookmarkedSet.contains(Pair.of(finalResult.getType(), finalResult.getId())))
                    finalResult.setIsBookmarkedTrue();
            }
            //mainImg 넣기
            finalResult.getPhotoList().add(Objects.equals((String) raw[4], "") ? null:((String)raw[4]));

            //시설의 경우 사진 추가
            if(((String)raw[0]).equals("FACILITY")){
                Facility facility = facilityMap.get(((Long)raw[1]));
                finalResult.getPhotoList().add(facility.getMainImg2());
                finalResult.getPhotoList().add(facility.getMainImg3());
            }

            return finalResult;
        }).toList();


        return SearchConverter.toResultListDTO(body);
    }

}
