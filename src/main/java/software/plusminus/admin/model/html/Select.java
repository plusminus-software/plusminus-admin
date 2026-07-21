package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Select extends AbstractInput {
    private boolean multiple;
    private List<Option> options;
}
