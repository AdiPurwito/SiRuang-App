// service/AuthService.java
package service;

import model.Admin;
import model.Mahasiswa;
import model.User;

import java.io.*;

public class AutenService {
    private static final String FILE = "data/user.txt";

    public static User login(String username, String password) {
        // 1. Cek file user.txt
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String u = parts[0].trim();
                    String p = parts[1].trim();
                    String role = parts[2].trim().toUpperCase();

                    if (u.equalsIgnoreCase(username) && p.equals(password)) {
                        return switch (role) {
                            case "ADMIN" -> new Admin(username);
                            case "MAHASISWA" -> new Mahasiswa(username);
                            default -> null;
                        };
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca user.txt: " + e.getMessage());
        }

        // 2. Jika file kosong/gagal, fallback default login.css
        if ("admin".equalsIgnoreCase(username) && "admin".equals(password)) {
            return new Admin(username);
        }
        if ("mhs".equalsIgnoreCase(username) && "mhs".equals(password)) {
            return new Mahasiswa(username);
        }

        return null;
    }
}
