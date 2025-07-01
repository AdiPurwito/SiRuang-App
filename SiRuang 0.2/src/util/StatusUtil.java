package util;

import database.BookingDB;
import database.JadwalDB;
import model.Booking;
import model.Jadwal;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class StatusUtil {
    public static String statusRuangNow(String ruangNama) {
        String hariIni = getHariIni();
        LocalTime now = LocalTime.now();

        List<Jadwal> semuaJadwal = JadwalDB.loadJadwal();
        for (Jadwal j : semuaJadwal) {
            if (j.getHari().equalsIgnoreCase(hariIni) &&
                    j.getRuang().equalsIgnoreCase(ruangNama)) {
                String[] jam = j.getJam().replace(" ", "").split("-");
                if (jam.length == 2 && waktuSekarangMasukRange(jam[0], jam[1], now)) {
                    return "Terpakai Jadwal";
                }
            }
        }

        List<Booking> semuaBooking = BookingDB.loadAll();

        for (Booking b : semuaBooking) {
            if (b.getStatus().equalsIgnoreCase("menunggu") &&
                    b.getHari().equalsIgnoreCase(hariIni) &&
                    b.getRuang().equalsIgnoreCase(ruangNama)) {
                String[] jam = b.getJam().replace(" ", "").split("-");
                if (jam.length == 2 && waktuSekarangMasukRange(jam[0], jam[1], now)) {
                    return "Menunggu";
                }
            }
        }

        for (Booking b : semuaBooking) {
            if (b.getStatus().equalsIgnoreCase("diterima") &&
                    b.getHari().equalsIgnoreCase(hariIni) &&
                    b.getRuang().equalsIgnoreCase(ruangNama)) {
                String[] jam = b.getJam().replace(" ", "").split("-");
                if (jam.length == 2 && waktuSekarangMasukRange(jam[0], jam[1], now)) {
                    return "Terbooking";
                }
            }
        }

        return "Kosong";
    }

    private static boolean waktuSekarangMasukRange(String startStr, String endStr, LocalTime now) {
        try {
            LocalTime start = parseJamFlexible(startStr);
            LocalTime end = parseJamFlexible(endStr);
            return !now.isBefore(start) && now.isBefore(end);
        } catch (Exception e) {
            return false;
        }
    }

    private static LocalTime parseJamFlexible(String jam) {
        jam = jam.trim().replace(":", "."); // normalisasi ke format 15.15
        String[] parts = jam.split("\\.");
        int hour = Integer.parseInt(parts[0]);
        int minute = (parts.length > 1) ? Integer.parseInt(parts[1]) : 0;
        return LocalTime.of(hour, minute);
    }

    private static String getHariIni() {
        return switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> "Senin";
            case Calendar.TUESDAY -> "Selasa";
            case Calendar.WEDNESDAY -> "Rabu";
            case Calendar.THURSDAY -> "Kamis";
            case Calendar.FRIDAY -> "Jumat";
            default -> "Senin"; // fallback
        };
    }
}
