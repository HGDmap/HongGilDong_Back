package hongik.map.honggildong.global.redis.search.service;

import com.redis.lettucemod.api.sync.RedisModulesCommands;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.building.repository.BuildingRepository;
import hongik.map.honggildong.domain.event.entity.Event;
import hongik.map.honggildong.domain.event.repository.EventRepository;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.global.common.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchDataLoader {

    private final BuildingRepository buildingRepository;
    private final FacilityRepository facilityRepository;
    private final EventRepository eventRepository;
    private final RedisModulesCommands<String, String> commands;


    public Map<String,Integer> loadAll() {
        int buildingSize = loadFromRepo(buildingRepository.findAll(), "BUILDING");
        int facSize = loadFromRepo(facilityRepository.findAll(), "FACILITY");
        int eventSize = loadFromRepo(eventRepository.findAll(), "EVENT");

        Map<String,Integer> map = new HashMap<>();
        map.put("building",buildingSize);
        map.put("facility",facSize);
        map.put("event",eventSize);

        return map;

    }

    private <T extends BaseEntity> int loadFromRepo(List<T> entities, String type) {
        for (T entity : entities) {
            NameAndId nameAndId = getNameAndId(entity);
            String name = nameAndId.getName();
            String id = String.valueOf(nameAndId.getId());
            commands.hset("doc:" + type + ":" + id, Map.of(
                    "name", name,
                    "type", type,
                    "ref_id", id
            ));
        }

        int size = entities.size();
        System.out.printf("✅ %s 데이터 %d개 인덱싱 완료%n", type, size);
        return size;
    }

    @Getter
    private static class NameAndId{
        String name;
        Long id;

        NameAndId(String name, Long id) {
            this.name = name;
            this.id = id;
        }
    }

    private <T> NameAndId getNameAndId(T entity){

        String name = "";
        Long id = 0L;
        if(entity instanceof Building){
            name = ((Building) entity).getName();
            id = ((Building) entity).getId();
        }else if(entity instanceof Facility){
            name = ((Facility) entity).getName();
            id = ((Facility) entity).getId();
        }else if(entity instanceof Event){
            name = ((Event) entity).getName();
            id = ((Event) entity).getId();
        }

        return new NameAndId(name, id);
    }
}


