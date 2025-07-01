package controller;

import gui.AdminDashboard;
import gui.MahasiswaDashboard;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {
    public static void login(String username, String password, Stage stage) {
        if (username.equals("admin") && password.equals("123")) {
            stage.setScene(new Scene(new AdminDashboard(stage), 800, 600));
        } else if (username.startsWith("mhs") && password.equals("123")) {
            stage.setScene(new Scene(new MahasiswaDashboard(stage, username), 800, 600));
        } else {
            System.out.println("Login gagal");
        }
    }
}
