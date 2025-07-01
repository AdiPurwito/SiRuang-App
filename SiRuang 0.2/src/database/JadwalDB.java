package database;

import model.Jadwal;

import java.io.*;
import java.util.*;

public class JadwalDB {
    private static final String FILE = System.getProperty("user.dir") + "/data/jadwal.txt";


    public static List<Jadwal> loadJadwal() {
        List<Jadwal> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] p = line.split("\\|");
                if (p.length >= 10) {
                    list.add(new Jadwal(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8], p[9]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void saveJadwal(Jadwal j) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(String.join("|", j.getHari(), j.getJam(), j.getMatkul(), j.getSemester(),
                    j.getSks(), j.getKelas(), j.getDosen(), j.getRuang(), j.getFakultas(), j.getProdi()));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateJadwal(Jadwal lama, Jadwal baru) {
        List<Jadwal> semua = loadJadwal();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Jadwal j : semua) {
                if (isSame(j, lama)) {
                    bw.write(toLine(baru));
                } else {
                    bw.write(toLine(j));
                }
                bw.newLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteJadwal(Jadwal target) {
        List<Jadwal> semua = loadJadwal();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Jadwal j : semua) {
                if (!isSame(j, target)) {
                    bw.write(toLine(j));
                    bw.newLine();
                } else {
                    // kembalikan sesi ruang
                    RuangDB.tambahSesi(j.getRuang());
                }
                bw.write("# hari|jam|matkul|semester|sks|kelas|dosen|ruang|fakultas|prodi");
                bw.newLine();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String toLine(Jadwal j) {
        return String.join("|", j.getHari(), j.getJam(), j.getMatkul(), j.getSemester(),
                j.getSks(), j.getKelas(), j.getDosen(), j.getRuang(), j.getFakultas(), j.getProdi());
    }

    public static boolean isSame(Jadwal a, Jadwal b) {
        return a.getHari().equals(b.getHari()) &&
                a.getJam().equals(b.getJam()) &&
                a.getMatkul().equals(b.getMatkul()) &&
                a.getSemester().equals(b.getSemester()) &&
                a.getSks().equals(b.getSks()) &&
                a.getKelas().equals(b.getKelas()) &&
                a.getDosen().equals(b.getDosen()) &&
                a.getRuang().equals(b.getRuang());
    }

    public static boolean isJadwalBentrok(Jadwal baru) {
        List<Jadwal> semua = loadJadwal();
        for (Jadwal j : semua) {
            if (j.getHari().equalsIgnoreCase(baru.getHari()) &&
                    j.getRuang().equalsIgnoreCase(baru.getRuang()) &&
                    jamTumpangTindih(j.getJam(), baru.getJam())) {
                return true;
            }
        }
        return false;
    }

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
