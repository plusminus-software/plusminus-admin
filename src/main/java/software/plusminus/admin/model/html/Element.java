package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@Data
public abstract class Element {

    /* Drives the element.mustache dispatch: a section like {{#is.INPUT}} renders
       only when the element's class matches the key ({{#is.TAGLIST_EMBEDDED}} for
       TaglistEmbedded etc.); missing keys make the section silently skipped */
    public Map<String, Boolean> getIs() {
        String key = getClass().getSimpleName()
                .replaceAll("(?<=.)([A-Z])", "_$1")
                .toUpperCase(Locale.ROOT);
        return Collections.singletonMap(key, Boolean.TRUE);
    }
}
