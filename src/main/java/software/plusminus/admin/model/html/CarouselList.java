package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarouselList extends AbstractInput {

    private static final boolean CAROUSEL_LIST = true;

    private Upload upload;
    private boolean single;

}
