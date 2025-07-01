package database;

import java.io.*;
import java.util.*;

public class DosenDB {
    private static final String FILE = System.getProperty("user.dir") + "/data/dosen.txt";


    public static List<String> loadDosen() {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveIfNew(String dosen) {
        List<String> existing = loadDosen();
        if (!existing.contains(dosen)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
                bw.write(dosen);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
