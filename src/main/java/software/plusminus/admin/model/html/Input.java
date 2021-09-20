package software.plusminus.admin.model.html;

import lombok.Data;

@Data
public class Input extends AbstractInput {

    private static final boolean INPUT = true;

    private String type;
    private boolean isNumber;
}
