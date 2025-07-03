package interfaces;

import java.util.List;

public interface DatabaseHandler<T> {
    List<T> load();
    void saveAll(List<T> list);

    default void save(T data) {
        throw new UnsupportedOperationException("save() not implemented.");
    }

    default void delete(T data) {
        throw new UnsupportedOperationException("delete() not implemented.");
    }
}
