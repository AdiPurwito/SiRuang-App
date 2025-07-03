package util;

import database.BookingDB;
import database.JadwalDB;
import model.Booking;
import model.Jadwal;

import java.util.Calendar;
import java.util.List;

public class StatusUtil {

    public static String statusRuangNow(String ruangNama) {
        String hariIni = getHariIni();
        int sesiSekarang = SesiUtil.getSesiSekarang();

        // Booking "Menunggu" langsung tampil
        List<Booking> semuaBooking = BookingDB.loadAll();
        for (Booking b : semuaBooking) {
            if (!b.getHari().equalsIgnoreCase(hariIni)) continue;
            if (!b.getRuang().equalsIgnoreCase(ruangNama)) continue;

            String status = b.getStatus().trim().toLowerCase();
            int[] sesi = SesiUtil.ekstrakSesiDanSKS(b.getJam());
            int mulai = sesi[0], akhir = mulai + sesi[1] - 1;

            if (status.equals("menunggu")) {
                return "Menunggu"; // tampil selalu
            } else if ((status.equals("diterima") || status.equals("disetujui"))
                    && sesiSekarang >= mulai && sesiSekarang <= akhir) {
                return "Terbooking"; // hanya jika sekarang berada dalam range sesi
            }
        }

        // Jadwal tetap dari admin
        List<Jadwal> semuaJadwal = JadwalDB.loadJadwal();
        for (Jadwal j : semuaJadwal) {
            if (!j.getHari().equalsIgnoreCase(hariIni)) continue;
            if (!j.getRuang().equalsIgnoreCase(ruangNama)) continue;

            int mulai = SesiUtil.getSesiMulaiDariJam(j.getJam());
            int akhir = mulai + SesiUtil.getSksDariJam(j.getJam()) - 1;

            if (sesiSekarang >= mulai && sesiSekarang <= akhir) {
                return "Terpakai Jadwal";
            }
        }

        return sesiSekarang == -1 ? "Di Luar Sesi" : "Kosong";
    }

    public static String getHariIni() {
        return switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> "Senin";
            case Calendar.TUESDAY -> "Selasa";
            case Calendar.WEDNESDAY -> "Rabu";
            case Calendar.THURSDAY -> "Kamis";
            case Calendar.FRIDAY -> "Jumat";
            case Calendar.SATURDAY -> "Sabtu";
            case Calendar.SUNDAY -> "Minggu";
            default -> "Senin";
        };
    }
}
