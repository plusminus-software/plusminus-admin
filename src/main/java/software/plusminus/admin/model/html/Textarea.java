package software.plusminus.admin.model.html;

import lombok.Data;

@Data
public class Textarea extends AbstractInput {

    private static final boolean TEXTAREA = true;

    private String type;
}
