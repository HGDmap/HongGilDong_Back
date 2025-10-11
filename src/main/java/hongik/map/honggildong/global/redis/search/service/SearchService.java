package hongik.map.honggildong.global.redis.search.service;

import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;

import java.util.List;

public interface SearchService {
    List<SearchResultDTO.General> search(String keyword);

    List<String> findAllIndexedData();
}
