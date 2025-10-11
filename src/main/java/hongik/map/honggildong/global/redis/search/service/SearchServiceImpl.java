package hongik.map.honggildong.global.redis.search.service;

import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.SearchResults;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final RedisModulesCommands<String, String> commands;
    private final SearchDataLoader loader;

    public List<SearchResultDTO.General> search(String keyword) {
        SearchResults<String, String> results = commands.ftSearch("idx:search", "@name:" + keyword + "*");


        return results.stream().map(doc -> SearchResultDTO.General
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

}
