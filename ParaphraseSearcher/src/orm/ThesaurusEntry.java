package orm;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThesaurusEntry implements Serializable {
    private static final long serialVersionUID = -9191514647572042565L;
    public static final Type contentsType = new TypeToken<Map<String, Map<String, List<String>>>>() {
    }.getType();

    private Map<String, Map<String, List<String>>> contents;
    private List<String> flatContents = new ArrayList<>();

    public ThesaurusEntry() {

    }

    public ThesaurusEntry(Map<String, Map<String, List<String>>> input) {
        this.contents = input;
    }

    public Map<String, Map<String, List<String>>> getContents() {
        return contents;
    }

    public void setContents(Map<String, Map<String, List<String>>> contents) {
        this.contents = contents;
        flatContents.clear();
        contents.values().forEach(m -> m.values().forEach(flatContents::addAll));
    }

    public List<String> getFlatContents() {
        return flatContents;
    }
}
