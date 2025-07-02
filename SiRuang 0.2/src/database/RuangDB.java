package database;

import model.Ruang;

import java.io.*;
import java.util.*;

public class RuangDB {
    private static final String FILE = System.getProperty("user.dir") + "/data/ruang.txt";

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


    public static void saveAll(List<Ruang> ruangList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Ruang r : ruangList) {
                bw.write(r.getNama() + "|" + r.getGedung() + "|" + r.getKapasitas() + "|" + r.getJumlahSesi());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRuang(Ruang ruang) {
        List<Ruang> ruangList = loadRuang();
        ruangList.add(ruang);
        saveAll(ruangList);
    }

    public static void deleteRuang(String nama) {
        List<Ruang> ruangList = loadRuang();
        ruangList.removeIf(r -> r.getNama().equalsIgnoreCase(nama));
        saveAll(ruangList);
    }

    public static void deleteRuang(Ruang ruang) {
        deleteRuang(ruang.getNama());
    }

    public static void resetSemuaSesi() {
        List<Ruang> list = loadRuang();
        for (Ruang r : list) {
            r.setJumlahSesi(150);
        }
        saveAll(list);
    }

    public static void kurangiSesi(String ruangNama) {
        List<Ruang> ruangList = loadRuang();
        for (Ruang r : ruangList) {
            if (r.getNama().equalsIgnoreCase(ruangNama)) {
                r.setJumlahSesi(r.getJumlahSesi() - 1);
                break;
            }
        }
        saveAll(ruangList);
    }

    public static void tambahSesi(String ruangNama) {
        List<Ruang> ruangList = loadRuang();
        for (Ruang r : ruangList) {
            if (r.getNama().equalsIgnoreCase(ruangNama)) {
                r.setJumlahSesi(r.getJumlahSesi() + 1);
                break;
            }
        }
        saveAll(ruangList);
    }

    public static String getGedungByRuang(String ruangNama) {
        for (Ruang r : loadRuang()) {
            if (r.getNama().equalsIgnoreCase(ruangNama)) {
                return r.getGedung();
            }
        }
        return "-";
    }
}
