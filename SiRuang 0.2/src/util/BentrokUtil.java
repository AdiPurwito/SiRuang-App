package util;

import database.BookingDB;
import database.JadwalDB;
import model.Booking;
import model.Jadwal;

public class BentrokUtil {

    /**
     * Mengecek apakah ruang berbenturan dengan jadwal tetap di hari dan jam tertentu.
     */
    public static boolean bentrokDenganJadwal(String hari, String ruang, String jam) {
        for (Jadwal j : JadwalDB.loadJadwal()) {
            if (j.getHari().equalsIgnoreCase(hari) &&
                    j.getRuang().equalsIgnoreCase(ruang) &&
                    jamTumpangTindih(j.getJam(), jam)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mengecek apakah ruang berbenturan dengan booking mahasiswa (diterima atau menunggu).
     */
    public static boolean bentrokDenganBooking(String hari, String ruang, String jam, boolean termasukMenunggu) {
        for (Booking b : BookingDB.loadAll()) {
            boolean statusValid = b.getStatus().equalsIgnoreCase("diterima") ||
                    (termasukMenunggu && b.getStatus().equalsIgnoreCase("menunggu"));
            if (statusValid &&
                    b.getHari().equalsIgnoreCase(hari) &&
                    b.getRuang().equalsIgnoreCase(ruang) &&
                    jamTumpangTindih(b.getJam(), jam)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gabungan bentrok jadwal dan booking.
     */
    public static boolean isBentrokTotal(String hari, String ruang, String jam, boolean termasukMenunggu) {
        return bentrokDenganJadwal(hari, ruang, jam) || bentrokDenganBooking(hari, ruang, jam, termasukMenunggu);
    }

    /**
     * Mengecek apakah dua rentang jam saling tumpang tindih.
     * Format jam: "08.00-09.50"
     */
    public static boolean jamTumpangTindih(String jam1, String jam2) {
        try {
            String[] j1 = jam1.split("-");
            String[] j2 = jam2.split("-");

            int start1 = Integer.parseInt(j1[0].replace(".", ""));
            int end1 = Integer.parseInt(j1[1].replace(".", ""));
            int start2 = Integer.parseInt(j2[0].replace(".", ""));
            int end2 = Integer.parseInt(j2[1].replace(".", ""));

            return start1 < end2 && start2 < end1;
        } catch (Exception e) {
            return false;
        }
    }
}
