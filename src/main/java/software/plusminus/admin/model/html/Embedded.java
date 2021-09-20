package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.List;

@Data
public class Embedded extends Element {

    private static final boolean EMBEDDED = true;

    private String name;
    private List<? extends Element> elements;

}
