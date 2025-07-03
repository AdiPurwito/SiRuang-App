package database;

import interfaces.DatabaseHandler;
import model.Booking;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class BookingDB implements DatabaseHandler<Booking> {
    private static final String FILE = System.getProperty("user.dir") + "/data/booking.txt";

    @Override
    public List<Booking> load() {
        return loadAll();
    }

    @Override
    public void saveAll(List<Booking> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Booking b : list) {
                bw.write(String.join("|",
                        b.getPemesan(),
                        b.getRuang(),
                        b.getHari(),
                        b.getJam(),
                        b.getStatus(),
                        b.getTanggal() == null ? LocalDate.now().toString() : b.getTanggal()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Booking> loadAll() {
        List<Booking> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] p = line.split("\\|");
                if (p.length >= 6) {
                    list.add(new Booking(p[0], p[1], p[2], p[3], p[4], p[5]));
                } else if (p.length == 5) {
                    Booking b = new Booking(p[0], p[1], p[2], p[3], p[4]);
                    b.setTanggal(LocalDate.now().toString());
                    list.add(b);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateStatus(Booking target, String newStatus) {
        List<Booking> list = loadAll();
        for (Booking b : list) {
            if (isSame(b, target)) {
                b.setStatus(newStatus);
                break;
            }
        }
        new BookingDB().saveAll(list);
    }

    private static boolean isSame(Booking a, Booking b) {
        return a.getPemesan().equals(b.getPemesan()) &&
                a.getRuang().equals(b.getRuang()) &&
                a.getHari().equals(b.getHari()) &&
                a.getJam().equals(b.getJam());
    }
}
