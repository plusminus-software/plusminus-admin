package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaglistEmbedded extends AbstractInput {

    private static final boolean TAGLIST_EMBEDDED = true;

    private Taglist taglist;
    private Accordion accordion;

}
