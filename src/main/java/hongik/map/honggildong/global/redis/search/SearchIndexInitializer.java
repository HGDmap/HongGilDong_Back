package hongik.map.honggildong.global.redis.search;

import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.CreateOptions;
import com.redis.lettucemod.search.Field;
import hongik.map.honggildong.global.redis.search.service.SearchDataLoader;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.CommandOutput;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.ProtocolKeyword;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchIndexInitializer {

    private final RedisModulesCommands<String, String> commands;
    private final SearchDataLoader searchDataLoader;
    @PostConstruct
    public void init() {

        try {
            Field<String> name = Field.text("name").sortable(true).build();

            commands.ftCreate("idx:search",
                    CreateOptions.<String, String>builder().on(CreateOptions.DataType.HASH)
                            .prefix("doc:")
                            .build(),
                    Field.text("name").sortable(true).build(),
                    Field.text("alias").build(),
                    Field.tag("type").build(),
                    Field.numeric("ref_id").sortable(true).build()
            );

            searchDataLoader.loadAll();

            System.out.println("✅ RediSearch 인덱스 생성 완료");
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("ℹ️ 인덱스가 이미 존재하거나 예외가 일어났습니다");
        }


    }
}
