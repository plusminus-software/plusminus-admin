package software.plusminus.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.admin.models.ErrorEntity;
import software.plusminus.admin.models.TestEntity;
import software.plusminus.type.ParseService;
import software.plusminus.type.model.Type;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public Map<String, AdminTypeConfig> types(ParseService parseService) {
        Type testEntity = parseService.parse(TestEntity.class);
        Type errorEntity = parseService.parse(ErrorEntity.class);
        return Stream.of(testEntity, errorEntity)
                .map(type -> AdminTypeConfig.builder(type).build())
                .collect(Collectors.toMap(typeConfig -> typeConfig.getType().getName(), Function.identity()));
    }
}
