package hongik.map.honggildong.global.redis.search.controller;


import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.redis.search.SearchIndexCleaner;
import hongik.map.honggildong.global.redis.search.SearchIndexInitializer;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;
import hongik.map.honggildong.global.redis.search.service.SearchDataLoader;
import hongik.map.honggildong.global.redis.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final SearchIndexInitializer initializer;
    private final SearchIndexCleaner cleaner;

    @GetMapping("/list/{query}")
    public ApiResponse<List<SearchResultDTO.General>> search(@PathVariable("query") String query) {

        List<SearchResultDTO.General> body = searchService.search(query);

        return ApiResponse.onSuccess(body);
    }

    //검색어 데이터 추가는 시작 시 자동으로
    @GetMapping("/load")
    public ApiResponse<String> load() {

        cleaner.cleanUp();
        initializer.init();

        return ApiResponse.onSuccess("인덱스 등록 완료");
    }

    @GetMapping("/indexed-data")
    public ApiResponse<List<String>> findAllIndexedData() {

        List<String> body = searchService.findAllIndexedData();

        return ApiResponse.onSuccess(body);
    }
}
