package hongik.map.honggildong.global.redis.search.service;

import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;

import java.util.List;

public interface SearchService {
    List<SearchResultDTO.AutoCompleteGeneral> autoComplete(String keyword);

    List<String> findAllIndexedData();

    SearchResultDTO.ResultList search(String query, Member member);
}
