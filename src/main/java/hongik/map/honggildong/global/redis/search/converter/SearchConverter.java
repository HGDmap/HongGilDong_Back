package hongik.map.honggildong.global.redis.search.converter;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class SearchConverter {

    public static SearchResultDTO.Result toResultDTO(Object[] raw){
        return SearchResultDTO.Result.builder()
                .type((String)raw[0])
                .id((Long)raw[1])
                .name((String)raw[2])
                .description((String) raw[3])
                .latitude((Double)raw[5])
                .longitude((Double)raw[6])
                .nodeId((Long)raw[7])
                .build();
    }

    public static SearchResultDTO.Result toResultDTO(Facility facility){

        List<String> photoList = new ArrayList<>();
        photoList.add(facility.getMainImg());
        photoList.add(facility.getMainImg2());
        photoList.add(facility.getMainImg3());
        Node node = facility.getNode();

        return SearchResultDTO.Result.builder()
                .photoList(photoList)
                .id(facility.getId())
                .type("FACILITY")
                .longitude(node.getLongitude())
                .latitude(node.getLatitude())
                .nodeId(node.getId())
                .name(facility.getName())
                .build();
    }

    public static SearchResultDTO.ResultList toResultListDTO(List<SearchResultDTO.Result> content){
        return SearchResultDTO.ResultList.builder()
                .listSize(content.size())
                .resultList(content)
                .build();
    }
}
