package util;

import java.util.*;

public class SesiUtil {
    private static final String[][] SESI = {
            {"07.00", "07.50"}, // 1
            {"07.50", "08.40"}, // 2
            {"08.40", "09.30"}, // 3
            {"09.30", "10.20"}, // 4
            {"10.20", "11.10"}, // 5
            {"12.10", "13.00"}, // 6 (setelah Zuhur)
            {"13.00", "13.50"}, // 7
            {"13.50", "14.40"}, // 8
            {"15.15", "16.05"}, // 9 (setelah Ashar)
            {"16.05", "16.55"}, // 10
            {"16.55", "17.45"}, // 11
            {"18.15", "19.05"}, // 12 (setelah Maghrib)
            {"19.05", "19.55"}, // 13
            {"19.55", "20.45"}  // 14
    };

    public static List<Integer> getPilihanSesiMulai() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= SESI.length; i++) list.add(i);
        return list;
    }

    public static String getRentangWaktu(int sesiMulai, int sks) {
        if (sesiMulai < 1 || sesiMulai + sks - 1 > SESI.length) return "-";
        String awal = SESI[sesiMulai - 1][0];
        String akhir = SESI[sesiMulai + sks - 1 - 1][1];
        return awal + " - " + akhir;
    }

    public static boolean sesiMelebihiBatas(int sesiMulai, int sks) {
        return (sesiMulai < 1 || sesiMulai + sks - 1 > SESI.length);
    }

    public static String getDaftarSesi() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SESI.length; i++) {
            sb.append("Sesi ").append(i + 1).append(": ")
                    .append(SESI[i][0]).append("–").append(SESI[i][1]).append("\n");
        }
        return sb.toString();
    }

    public static String getWaktu(int sesi) {
        if (sesi < 1 || sesi > SESI.length) return "-";
        return SESI[sesi - 1][0] + " – " + SESI[sesi - 1][1];
    }


    public static int[] getRangeSesi(String waktu) {
        // Format waktu = "07.00 - 08.40" → cari sesi mulai dan sesi akhir
        String[] parts = waktu.split(" - ");
        int start = -1, end = -1;
        for (int i = 0; i < SESI.length; i++) {
            if (SESI[i][0].equals(parts[0])) start = i + 1;
            if (SESI[i][1].equals(parts[1])) end = i + 1;
        }
        return (start > 0 && end > 0) ? new int[]{start, end} : null;
    }

    public static int[] ekstrakSesiDanSKS(String jam) {
        // Format jam: "07.00 - 08.40"
        int[] range = getRangeSesi(jam);
        if (range == null || range.length != 2) return new int[]{1, 1}; // default fallback
        int sesiMulai = range[0];
        int sesiAkhir = range[1];
        int sks = sesiAkhir - sesiMulai + 1;
        return new int[]{sesiMulai, sks};
    }

}
