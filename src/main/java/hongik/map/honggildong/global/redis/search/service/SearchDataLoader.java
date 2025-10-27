package hongik.map.honggildong.global.redis.search.service;

import com.redis.lettucemod.api.sync.RedisModulesCommands;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.building.repository.BuildingRepository;
import hongik.map.honggildong.domain.event.entity.Event;
import hongik.map.honggildong.domain.event.repository.EventRepository;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.global.common.BaseEntity;
import hongik.map.honggildong.global.redis.search.repository.SearchRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchDataLoader {

    private final BuildingRepository buildingRepository;
    private final FacilityRepository facilityRepository;
    private final EventRepository eventRepository;
    private final RedisModulesCommands<String, String> commands;
    private final SearchRepository searchRepository;


    public Integer loadAll() {

        return loadFromRepo(searchRepository.indexingAll());

    }

    private Integer loadFromRepo(List<Object[]> entities) {

        int buildingSize = 0;
        int facilitySize = 0;
        int eventSize = 0;

        for (Object[] entity : entities) {
            String type = entity[0].toString();
            String id = entity[1].toString();
            String name = entity[2].toString();

            String alias = "";
            if(entity[3]!=null){
                alias = entity[3].toString();
            }
            String nodeId = entity[4].toString();
            commands.hset("doc:" + type + ":" + id, Map.of(
                    "name", name,
                    "alias", alias.replaceAll("\\s+", " "),
                    "type", type,
                    "ref_id", id,
                    "node_id", nodeId
            ));

            if(type.equals("FACILITY")) {
                facilitySize++;
            }else if(type.equals("BUILDING")) {
                buildingSize++;
            }else {
                eventSize++;
            }
        }

        System.out.printf("✅ 빌딩 데이터 %d개 인덱싱 완료%n", buildingSize);
        System.out.printf("✅ 시설 데이터 %d개 인덱싱 완료%n", facilitySize);
        System.out.printf("✅ 이벤트 데이터 %d개 인덱싱 완료%n", eventSize);

        return buildingSize+facilitySize+eventSize;
    }

}


