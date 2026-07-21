package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaglistRelation extends AbstractInput {

    private static final boolean TAGLIST_RELATION = true;

    private Taglist taglist;
    private Table table;

}
