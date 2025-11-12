package hongik.map.honggildong.global.redis.search.controller;


import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.redis.search.SearchIndexCleaner;
import hongik.map.honggildong.global.redis.search.SearchIndexInitializer;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;
import hongik.map.honggildong.global.redis.search.service.SearchService;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@Tag(name = "검색")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final SearchIndexInitializer initializer;
    private final SearchIndexCleaner cleaner;

    @GetMapping("/list/{query}")
    @Operation(summary = "검색 시 자동완성 리스트 제공")
    public ApiResponse<List<SearchResultDTO.AutoCompleteGeneral>> searchList(@PathVariable("query") String query) {

        List<SearchResultDTO.AutoCompleteGeneral> body = searchService.autoComplete(query);

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("/{query}")
    @Operation(summary = "검색 버튼 눌렀을 때", description = "특정 자동완성 안 누르고 바로 검색 버튼 누를 시")
    public ApiResponse<SearchResultDTO.resultList> search(@PathVariable("query") String query,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        Member member=null;

        if(userDetails != null){
            member = userDetails.getMember(); //주의: 현재 Member는 컨텍스트에 등록된 객체가 아닌 POJO
        }
        SearchResultDTO.resultList body = searchService.search(query, member);

        return ApiResponse.onSuccess(body);
    }

    //검색어 데이터 추가는 시작 시 자동으로
    @GetMapping("/load")
    @Operation(summary = "인덱스 삭제 후 인덱스 생성, 데이터까지 등록")
    public ApiResponse<String> load() {

        cleaner.cleanUp();
        initializer.init();

        return ApiResponse.onSuccess("인덱스 등록 완료");
    }

    @GetMapping("/indexed-data")
    @Operation(summary = "인덱싱 된 데이터 확인 용")
    public ApiResponse<List<String>> findAllIndexedData() {

        List<String> body = searchService.findAllIndexedData();

        return ApiResponse.onSuccess(body);
    }
}
