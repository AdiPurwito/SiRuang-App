package interfaces;

import java.util.List;

public interface StaticDataHandler {
    List<String> load();
    void saveIfNew(String item);
}
