package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.List;

@Data
public class Checkbox extends AbstractInput {

    private static final boolean CHECKBOX = true;

    private List<Option> options;
}
