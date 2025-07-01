package controller;

import database.JadwalDB;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Jadwal;
import util.AlertUtil;

import java.util.List;

public class JadwalController {

    public static void tambahJadwal(Jadwal baru, Runnable onSuccess) {
        if (isBentrok(baru)) {
            AlertUtil.error("Jadwal bentrok dengan jadwal lain!");
            return;
        }
        List<Jadwal> semua = JadwalDB.loadJadwal();
        semua.add(baru);
        JadwalDB.saveAll(semua);
        AlertUtil.show("Jadwal berhasil ditambahkan.");
        onSuccess.run();
    }

    public static void updateJadwal(Jadwal lama, Jadwal baru, Runnable onSuccess) {
        List<Jadwal> semua = JadwalDB.loadJadwal();
        semua.removeIf(j -> j.equals(lama));
        if (isBentrok(baru)) {
            alert("Jadwal bentrok dengan jadwal lain!");
            return;
        }
        semua.add(baru);
        JadwalDB.saveAll(semua);
        alert("Jadwal berhasil diperbarui.");
        onSuccess.run();
    }

    public static void hapusJadwal(Jadwal target, Runnable onSuccess) {
        database.JadwalDB.deleteJadwal(target);
        alert("Jadwal berhasil dihapus.");
        onSuccess.run();
    }


    private static boolean isBentrok(Jadwal baru) {
        for (Jadwal j : JadwalDB.loadJadwal()) {
            if (j.getHari().equalsIgnoreCase(baru.getHari()) &&
                    j.getRuang().equalsIgnoreCase(baru.getRuang()) &&
                    JadwalDB.jamTumpangTindih(j.getJam(), baru.getJam())) {
                return true;
            }
        }
        return false;
    }

    private static void alert(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
            a.showAndWait();
        });
    }
}
