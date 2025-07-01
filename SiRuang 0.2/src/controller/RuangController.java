package controller;

import database.RuangDB;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Ruang;

import java.util.List;

public class RuangController {

    public static void tambahRuang(Ruang baru, Runnable onSuccess) {
        List<Ruang> semua = RuangDB.loadRuang();
        for (Ruang r : semua) {
            if (r.getNama().equalsIgnoreCase(baru.getNama())) {
                alert("Nama ruang sudah digunakan!");
                return;
            }
        }
        semua.add(baru);
        RuangDB.saveAll(semua);
        alert("Ruang berhasil ditambahkan.");
        onSuccess.run();
    }

    public static void updateRuang(Ruang lama, Ruang baru, Runnable onSuccess) {
        List<Ruang> semua = RuangDB.loadRuang();
        semua.removeIf(r -> r.equals(lama));
        semua.add(baru);
        RuangDB.saveAll(semua);
        alert("Ruang berhasil diperbarui.");
        onSuccess.run();
    }

    public static void hapusRuang(Ruang r, Runnable onSuccess) {
        List<Ruang> semua = RuangDB.loadRuang();
        semua.removeIf(x -> x.equals(r));
        RuangDB.saveAll(semua);
        alert("Ruang berhasil dihapus.");
        onSuccess.run();
    }

    private static void alert(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
            a.showAndWait();
        });
    }
}
