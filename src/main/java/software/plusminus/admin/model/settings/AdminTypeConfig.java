package software.plusminus.admin.model.settings;

import lombok.Builder;
import lombok.Data;
import software.plusminus.type.model.Type;

@Data
@Builder
public class AdminTypeConfig {

    private Type type;
    private TableSettings tableSettings;
    private ModalSettings modalSettings;
    private int order;

    private static AdminTypeConfigBuilder builder() {
        return new AdminTypeConfigBuilder();
    }

    public static AdminTypeConfigBuilder builder(Type type) {
        return builder().type(type)
                .tableSettings(TableSettings.builder().build())
                .modalSettings(ModalSettings.builder().build());
    }

    /* Declared explicitly so that javadoc, which runs without Lombok,
       can resolve the builder methods above; Lombok fills in the members */
    public static class AdminTypeConfigBuilder {
    }
}
