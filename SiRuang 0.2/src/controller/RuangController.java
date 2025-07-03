package controller;

import database.RuangDB;
import model.Ruang;
import util.AlertUtil;

import java.util.List;

public class RuangController {

    public static void tambahRuang(Ruang baru, Runnable onSuccess) {
        List<Ruang> semua = RuangDB.loadRuang();
        for (Ruang r : semua) {
            if (r.getNama().equalsIgnoreCase(baru.getNama()) &&
                    r.getGedung().equalsIgnoreCase(baru.getGedung())) {
                AlertUtil.show("Ruang dengan nama dan gedung tersebut sudah ada!");
                return;
            }
        }
        semua.add(baru);
        new RuangDB().saveAll(semua);
        AlertUtil.success("Ruang berhasil ditambahkan.");
        onSuccess.run();
    }

    public static void updateRuang(Ruang lama, Ruang baru, Runnable onSuccess) {
        List<Ruang> semua = RuangDB.loadRuang();

        for (Ruang r : semua) {
            if (r.getNama().equalsIgnoreCase(baru.getNama()) &&
                    r.getGedung().equalsIgnoreCase(baru.getGedung()) &&
                    !r.equals(lama)) {
                AlertUtil.show("Ruang dengan nama dan gedung tersebut sudah ada!");
                return;
            }
        }

        semua.removeIf(r -> r.equals(lama));
        semua.add(baru);
        new RuangDB().saveAll(semua);
        AlertUtil.success("Ruang berhasil diperbarui.");
        onSuccess.run();
    }

    public static void hapusRuang(Ruang r, Runnable onSuccess) {
        List<Ruang> semua = RuangDB.loadRuang();
        semua.removeIf(x -> x.equals(r));
        new RuangDB().saveAll(semua);
        AlertUtil.success("Ruang berhasil dihapus.");
        onSuccess.run();
    }
}
