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
                return "Menunggu";
            } else if ((status.equals("diterima") || status.equals("disetujui"))
                    && sesiSekarang >= mulai && sesiSekarang <= akhir) {
                return "Terbooking";
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

        // Di luar jam kampus
        if (sesiSekarang == -1) {
            LocalTime now = LocalTime.now();
            if (now.isAfter(LocalTime.of(6, 30)) && now.isBefore(LocalTime.of(21, 0))) {
                return "Kosong";
            } else {
                return "Di Luar Jam Kampus";
            }
        }

        return "Kosong";
    }

    public static String getStatusRuang(String ruang, String hari, String jam) {
        // Jadwal tetap
        List<Jadwal> semuaJadwal = JadwalDB.loadJadwal();
        for (Jadwal j : semuaJadwal) {
            if (j.getHari().equalsIgnoreCase(hari)
                    && j.getRuang().equalsIgnoreCase(ruang)
                    && BentrokUtil.jamTumpangTindih(j.getJam(), jam)) {
                return "Terpakai Jadwal";
            }
        }

        // Booking mahasiswa
        List<Booking> semuaBooking = BookingDB.loadAll();
        for (Booking b : semuaBooking) {
            if (b.getHari().equalsIgnoreCase(hari)
                    && b.getRuang().equalsIgnoreCase(ruang)
                    && BentrokUtil.jamTumpangTindih(b.getJam(), jam)) {
                if (b.getStatus().equalsIgnoreCase("menunggu")) return "Menunggu";
                if (b.getStatus().equalsIgnoreCase("diterima") || b.getStatus().equalsIgnoreCase("disetujui"))
                    return "Terbooking";
            }
        }

        return "Kosong";
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
