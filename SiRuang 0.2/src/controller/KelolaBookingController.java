package controller;

import database.BookingDB;
import database.RuangDB;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Booking;
import model.Ruang;
import util.AlertUtil;
import util.BentrokUtil;

import java.util.List;

public class KelolaBookingController {

    public static void terimaBooking(Booking b, Runnable onSuccess) {
        if (BentrokUtil.isBentrokTotal(b.getHari(), b.getRuang(), b.getJam(), false)) {
            AlertUtil.error("Booking bentrok dengan jadwal/booking lain.");
            return;
        }

        kurangiSesi(b.getRuang());
        BookingDB.updateStatus(b, "diterima");
        AlertUtil.success("Booking berhasil diterima.");
        onSuccess.run();
    }

    public static void tolakBooking(Booking b, Runnable onSuccess) {
        BookingDB.updateStatus(b, "ditolak");
        AlertUtil.show("Booking ditolak.");
        onSuccess.run();
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
