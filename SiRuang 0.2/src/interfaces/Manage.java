package interfaces;

import java.util.List;

public interface Manage<T> {
    void tambah(T data);
    void hapus(T data);
    void ubah(T data);
    List<T> getAll();
}