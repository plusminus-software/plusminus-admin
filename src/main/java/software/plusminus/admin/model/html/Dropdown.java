package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.List;

@Data
public class Dropdown extends Element {

    public static final boolean DROPDOWN = true;

    private String name;
    private boolean multiple;
    private List<Option> options;

}
