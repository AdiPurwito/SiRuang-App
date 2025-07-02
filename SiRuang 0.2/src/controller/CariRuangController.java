package controller;

import database.BookingDB;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Booking;
import util.AlertUtil;
import util.BentrokUtil;

import java.util.Calendar;
import java.util.List;

public class CariRuangController {


    public static void prosesBooking(String username, String ruang, int sesiMulai, int sks, Runnable onSuccess) {
        String hari = getHariIni();
        String jamFinal = util.SesiUtil.getRentangWaktu(sesiMulai, sks);
        Booking baru = new Booking(username, ruang, hari, jamFinal, "menunggu");

        if (BentrokUtil.isBentrokTotal(hari, ruang, jamFinal, true)) {
            AlertUtil.error("Ruang tidak tersedia pada sesi tersebut.");
            return;
        }

        List<Booking> semua = BookingDB.loadAll();
        semua.add(baru);
        BookingDB.saveAll(semua);

        AlertUtil.show("Booking berhasil dikirim.");
        onSuccess.run();
    }

    public static String getStatusRuang(String ruang) {
        return util.StatusUtil.statusRuangNow(ruang);
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
