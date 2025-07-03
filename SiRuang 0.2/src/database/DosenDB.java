package database;

import interfaces.StaticDataHandler;

import java.io.*;
import java.util.*;

public class DosenDB implements StaticDataHandler {
    private static final String FILE = System.getProperty("user.dir") + "/data/dosen.txt";

    @Override
    public List<String> load() {
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

    @Override
    public void saveIfNew(String dosen) {
        List<String> existing = load();
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
