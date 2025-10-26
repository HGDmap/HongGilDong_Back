package hongik.map.honggildong.global.redis.search.service;

import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.SearchResults;
import hongik.map.honggildong.domain.bookmarkFolder.dto.JPQLBookmarkDTO;
import hongik.map.honggildong.domain.bookmarkFolder.repository.BookmarkFolderRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;
import hongik.map.honggildong.global.redis.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{

    private final SearchRepository searchRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final RedisModulesCommands<String, String> commands;

    public List<SearchResultDTO.AutoCompleteGeneral> autoComplete(String keyword) {
        String query = String.format("(@name:%s*) | (@alias:%s*)", keyword, keyword);
        SearchResults<String, String> results = commands.ftSearch("idx:search", query);


        return results.stream().map(doc -> SearchResultDTO.AutoCompleteGeneral
                .builder()
                .name(doc.get("name"))
                .type(doc.get("type"))
                .id(Long.valueOf(doc.get("ref_id")))
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
    public SearchResultDTO.resultList search(String query, Member member) {

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

        System.out.println(buildingIds.size()+"시설:"+facilityIds.size());

        //interface(native query 프로젝션)->set
        List<JPQLBookmarkDTO.SearchResult> bookmarkedList = new ArrayList<>();
        if(member!=null){
            bookmarkedList = bookmarkFolderRepository.findAllBookmarksFromSearch(member.getId(), buildingIds, facilityIds);
        }

        Set<Pair<String, Long>> bookmarkedSet = new HashSet<>();
        for(JPQLBookmarkDTO.SearchResult b : bookmarkedList){
            System.out.println("타입:"+b.getType()+"id:"+b.getId());
            bookmarkedSet.add(Pair.of(b.getType(),b.getId()));
        }

        List<SearchResultDTO.result> body = rawResult.stream().map(raw->{
            //북마크 된 리스트에 존재하는 아이디라면 true, 비로그인자는 항상 false
            SearchResultDTO.result finalResult = SearchResultDTO.result.builder()
                    .type((String)raw[0])
                    .id((Long)raw[1])
                    .name((String)raw[2])
                    .description((String) raw[3])
                    .latitude((Double)raw[5])
                    .longitude((Double)raw[6])
                    .build();
            if(member!=null){
                if(bookmarkedSet.contains(Pair.of(finalResult.getType(), finalResult.getId())))
                    finalResult.setIsBookmarkedTrue();
            }
            finalResult.getPhotoList().add((String)raw[4]);

            /**
             * 나중에 S3에서 해당 시설 폴더에 있는 사진 2개 가져오기
             */

            return finalResult;
        }).toList();


        return SearchResultDTO.resultList.builder().listSize(body.size()).resultList(body).build();
    }

}
