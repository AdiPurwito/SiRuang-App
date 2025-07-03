package database;

import interfaces.StaticDataHandler;

import java.io.*;
import java.util.*;

public class ProdiDB implements StaticDataHandler {
    private static final String FILE = System.getProperty("user.dir") + "/data/prodi.txt";

    @Override
    public List<String> load() {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) list.add(line.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void saveIfNew(String nama) {
        List<String> list = load();
        if (!list.contains(nama)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
                bw.write(nama);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
