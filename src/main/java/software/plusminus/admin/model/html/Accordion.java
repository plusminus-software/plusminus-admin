package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Accordion extends Element {

    private String title;
    private List<? extends Element> elements;

}
