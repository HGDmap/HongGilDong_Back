package hongik.map.honggildong.global.redis.search;

import com.redis.lettucemod.api.sync.RedisModulesCommands;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchIndexCleaner {

    private final RedisModulesCommands<String, String> commands;

    @PreDestroy
    public void cleanUp() {
        try {
            commands.ftDropindexDeleteDocs("idx:search"); // 인덱스 + 데이터 삭제
            System.out.println("✅ RediSearch 인덱스와 데이터 삭제 완료");
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("ℹ️ 삭제할 인덱스가 없습니다.");
        }
    }

}
