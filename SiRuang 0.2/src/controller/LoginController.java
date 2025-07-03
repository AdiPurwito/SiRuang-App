// controller/LoginController.java
package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import service.AutenService;
import util.AlertUtil;
import view.AdminDashboard;
import view.LoginView;
import view.MahasiswaDashboard;

public class LoginController {

    public static void login(String username, String password, Stage stage) {
        User user = AutenService.login(username, password);
        if (user == null) {
            AlertUtil.error("Username atau password salah!");
            stage.setScene(new Scene(new LoginView(stage), 400, 300));
            return;
        }

        switch (user.getRole()) {
            case ADMIN -> stage.setScene(new Scene(new AdminDashboard(stage), 400, 300));
            case MAHASISWA -> stage.setScene(new Scene(new MahasiswaDashboard(stage, user.getUsername()), 400, 300));
        }
    }
}
