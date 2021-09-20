package software.plusminus.admin.model.settings;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ModalSettings {

    private List<String> fields;

}
