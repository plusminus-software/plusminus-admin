package software.plusminus.admin.model.settings;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TableSettings {

    private List<String> fields;

}
