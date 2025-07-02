package database;

import java.io.*;
import java.util.*;

public class MatkulDB {
    private static final String FILE = System.getProperty("user.dir") + "/data/matkul.txt";

    public static List<String> loadMatkul() {
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


    public static void saveIfNew(String matkul) {
        List<String> existing = loadMatkul();
        if (!existing.contains(matkul)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
                bw.write(matkul);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
