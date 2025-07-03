package database;

import interfaces.DatabaseHandler;
import model.Ruang;

import java.io.*;
import java.util.*;

public class RuangDB implements DatabaseHandler<Ruang> {
    private static final String FILE = System.getProperty("user.dir") + "/data/ruang.txt";

    @Override
    public List<Ruang> load() {
        return loadRuang();
    }

    @Override
    public void saveAll(List<Ruang> ruangList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Ruang r : ruangList) {
                bw.write(r.getNama() + "|" + r.getGedung() + "|" + r.getKapasitas() + "|" + r.getJumlahSesi());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Ruang> loadRuang() {
        List<Ruang> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split("\\|");
                if (data.length != 4) continue;

                try {
                    String nama = data[0];
                    String gedung = data[1];
                    int kapasitas = Integer.parseInt(data[2]);
                    int sesi = Integer.parseInt(data[3]);

                    // ✅ Validasi nilai sesi
                    if (sesi < 0 || sesi > 300) {
                        System.err.println("Jumlah sesi tidak valid untuk ruang " + nama + ": " + sesi + ". Diset ke 0.");
                        sesi = 0;
                    }

                    list.add(new Ruang(nama, gedung, kapasitas, sesi));
                } catch (NumberFormatException e) {
                    System.err.println("Data ruang tidak valid (abaikan): " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void resetSemuaSesi() {
        List<Ruang> list = loadRuang();
        for (Ruang r : list) {
            r.setJumlahSesi(300);
        }
        new RuangDB().saveAll(list);
    }

    public static void kurangiSesi(String ruangNama, int sks) {
        List<Ruang> ruangList = loadRuang();
        for (Ruang r : ruangList) {
            if (r.getNama().equalsIgnoreCase(ruangNama)) {
                int baru = r.getJumlahSesi() - sks;
                r.setJumlahSesi(Math.max(0, baru));
                break;
            }
        }
        new RuangDB().saveAll(ruangList);
    }

    public static void tambahSesi(String ruangNama, int sks) {
        List<Ruang> ruangList = loadRuang();
        for (Ruang r : ruangList) {
            if (r.getNama().equalsIgnoreCase(ruangNama)) {
                int hasil = r.getJumlahSesi() + sks;
                r.setJumlahSesi(Math.min(300, hasil)); // ✅ Batasi maksimal 300
                break;
            }
        }
        new RuangDB().saveAll(ruangList);
    }


    public static String getGedungByRuang(String ruangNama) {
        for (Ruang r : loadRuang()) {
            if (r.getNama().equalsIgnoreCase(ruangNama)) {
                return r.getGedung();
            }
        }
        return "-";
    }

    public static void perbaikiSemuaSesi() {
        List<Ruang> list = loadRuang();
        for (Ruang r : list) {
            if (r.getJumlahSesi() < 0) r.setJumlahSesi(0);
            if (r.getJumlahSesi() > 300) r.setJumlahSesi(300);
        }
        new RuangDB().saveAll(list);
    }

}
