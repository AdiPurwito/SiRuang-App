package util;

import database.BookingDB;
import database.JadwalDB;
import model.Booking;
import model.Jadwal;

public class BentrokUtil {

    public static boolean bentrokDenganJadwal(String hari, String ruang, String jam) {
        for (Jadwal j : JadwalDB.loadJadwal()) {
            if (j.getHari().equalsIgnoreCase(hari) &&
                    j.getRuang().equalsIgnoreCase(ruang) &&
                    JadwalDB.jamTumpangTindih(j.getJam(), jam)) {
                return true;
            }
        }
        return false;
    }

    public static boolean bentrokDenganBooking(String hari, String ruang, String jam, boolean termasukMenunggu) {
        for (Booking b : BookingDB.loadAll()) {
            boolean statusValid = b.getStatus().equalsIgnoreCase("diterima") ||
                    (termasukMenunggu && b.getStatus().equalsIgnoreCase("menunggu"));
            if (statusValid &&
                    b.getHari().equalsIgnoreCase(hari) &&
                    b.getRuang().equalsIgnoreCase(ruang) &&
                    JadwalDB.jamTumpangTindih(b.getJam(), jam)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBentrokTotal(String hari, String ruang, String jam, boolean termasukMenunggu) {
        return bentrokDenganJadwal(hari, ruang, jam) || bentrokDenganBooking(hari, ruang, jam, termasukMenunggu);
    }
}
