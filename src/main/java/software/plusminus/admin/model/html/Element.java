package software.plusminus.admin.model.html;

import com.google.common.base.CaseFormat;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public abstract class Element {

    private String id;

    /* Drives the element.mustache dispatch: a section like {{#is.INPUT}} renders
       only when the element's class matches the key ({{#is.TAGLIST_EMBEDDED}} for
       TaglistEmbedded etc.); missing keys make the section silently skipped */
    public Map<String, Boolean> getIs() {
        String key = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, getClass().getSimpleName());
        return Collections.singletonMap(key, Boolean.TRUE);
    }
}
