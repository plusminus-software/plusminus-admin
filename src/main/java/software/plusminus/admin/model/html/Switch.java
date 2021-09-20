package software.plusminus.admin.model.html;

import lombok.Data;

@Data
public class Switch extends AbstractInput {

    private static final boolean SWITCH = true;

    private String text;

}
