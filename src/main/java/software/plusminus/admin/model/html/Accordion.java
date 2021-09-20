package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.List;

@Data
public class Accordion extends Element {

    private static final boolean ACCORDION = true;

    private String title;
    private List<? extends Element> elements;

}
