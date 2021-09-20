package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.List;

@Data
public class Select extends AbstractInput {

    private static final boolean SELECT = true;

    private boolean multiple;
    private List<Option> options;
}
