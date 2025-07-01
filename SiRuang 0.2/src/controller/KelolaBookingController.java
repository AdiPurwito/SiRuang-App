package controller;

import database.BookingDB;
import database.JadwalDB;
import database.RuangDB;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Booking;
import model.Jadwal;
import model.Ruang;
import util.AlertUtil;

import java.util.List;

public class KelolaBookingController {

    public static void terimaBooking(Booking b, Runnable onSuccess) {
        if (isBentrok(b)) {
            AlertUtil.success("Booking berhasil diterima.");
            return;
        }

        kurangiSesi(b.getRuang());
        BookingDB.updateStatus(b, "diterima");
        onSuccess.run();
    }

    public static void tolakBooking(Booking b, Runnable onSuccess) {
        BookingDB.updateStatus(b, "ditolak");
        AlertUtil.show("Booking ditolak.");
        onSuccess.run();
    }

    private static boolean isBentrok(Booking baru) {
        for (Jadwal j : JadwalDB.loadJadwal()) {
            if (j.getHari().equalsIgnoreCase(baru.getHari()) &&
                    j.getRuang().equalsIgnoreCase(baru.getRuang()) &&
                    JadwalDB.jamTumpangTindih(j.getJam(), baru.getJam())) {
                return true;
            }
        }
        for (Booking b : BookingDB.loadAll()) {
            if (b.getStatus().equalsIgnoreCase("diterima") &&
                    b.getHari().equalsIgnoreCase(baru.getHari()) &&
                    b.getRuang().equalsIgnoreCase(baru.getRuang()) &&
                    JadwalDB.jamTumpangTindih(b.getJam(), baru.getJam())) {
                return true;
            }
        }
        return false;
    }

    private static void kurangiSesi(String ruangNama) {
        List<Ruang> ruangList = RuangDB.loadRuang();
        for (Ruang r : ruangList) {
            if (r.getNama().equalsIgnoreCase(ruangNama)) {
                r.setJumlahSesi(r.getJumlahSesi() - 1);
                break;
            }
        }
        RuangDB.saveAll(ruangList);
    }

    private static void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
            a.showAndWait();
        });
    }
}
