package controller;

import database.BookingDB;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Booking;
import util.AlertUtil;
import util.BentrokUtil;
import util.SesiUtil;
import util.StatusUtil;

import java.util.Calendar;
import java.util.List;

public class CariRuangController {

    public static void prosesBooking(String username, String ruang, int sesiMulai, int sks, Runnable onSuccess) {
        String hari = getHariIni();
        String jamFinal = SesiUtil.getRentangWaktu(sesiMulai, sks);

        String statusRuang = StatusUtil.statusRuangNow(ruang);
        if (statusRuang.equalsIgnoreCase("Menunggu")) {
            AlertUtil.error("Ruang sedang dalam status MENUNGGU dan tidak dapat dibooking.");
            return;
        }

        Booking baru = new Booking(username, ruang, hari, jamFinal, "menunggu");

        if (util.BentrokUtil.isBentrokTotal(hari, ruang, jamFinal, true)) {
            AlertUtil.error("Ruang tidak tersedia pada sesi tersebut.");
            return;
        }

        BookingDB db = new BookingDB();
        List<Booking> semua = db.load();
        semua.add(baru);
        db.saveAll(semua);

        AlertUtil.show("Booking berhasil dikirim.");
        onSuccess.run();
    }


    public static String getHariIni() {
        return switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> "Senin";
            case Calendar.TUESDAY -> "Selasa";
            case Calendar.WEDNESDAY -> "Rabu";
            case Calendar.THURSDAY -> "Kamis";
            case Calendar.FRIDAY -> "Jumat";
            default -> "Senin";
        };
    }

    private static void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
            a.showAndWait();
        });
    }
}
