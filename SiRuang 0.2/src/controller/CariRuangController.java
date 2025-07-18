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
        String jamFinal = SesiUtil.getRentangWaktu(sesiMulai, sks); // jam seperti "08.00-09.50"

        // ✅ Validasi status ruang berdasar waktu aktual booking
        String statusRuang = StatusUtil.getStatusRuang(ruang, hari, jamFinal);
        if (!statusRuang.equalsIgnoreCase("Kosong")) {
            AlertUtil.error("Ruang tidak bisa dibooking. Status saat itu: " + statusRuang);
            return;
        }

        // ✅ Validasi konflik dengan jadwal atau booking lain
        if (BentrokUtil.isBentrokTotal(hari, ruang, jamFinal, true)) {
            AlertUtil.error("Ruang tidak tersedia pada sesi tersebut (bentrok).");
            return;
        }

        // ✅ Proses simpan booking
        Booking baru = new Booking(username, ruang, hari, jamFinal, "menunggu");
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
