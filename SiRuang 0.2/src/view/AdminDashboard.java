package view;

import database.RuangDB;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboard extends BorderPane {

    public AdminDashboard(Stage stage) {
        RuangDB.perbaikiSemuaSesi();
        setPadding(new Insets(20));

        Label title = new Label("SIRuang");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label welcome = new Label("Selamat datang di Dashboard Admin");
        welcome.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: montserrat");

        Button kelolaJadwalBtn = new Button("Kelola Jadwal");
        Button kelolaRuangBtn = new Button("Kelola Ruang");
        Button kelolaBookingBtn = new Button("Kelola Booking");
        Button logoutBtn = new Button("Logout");

        kelolaJadwalBtn.setPrefWidth(200);
        kelolaRuangBtn.setPrefWidth(200);
        kelolaBookingBtn.setPrefWidth(200);

        kelolaJadwalBtn.setOnAction(e -> {
            Scene scene = new Scene(new KelolaJadwalView(stage));
            stage.setScene(scene);
            stage.setMaximized(true);
        });

        kelolaRuangBtn.setOnAction(e -> {
            Scene scene = new Scene(new KelolaRuangView(stage));
            stage.setScene(scene);
            stage.setMaximized(true);
        });

        kelolaBookingBtn.setOnAction(e -> {
            Scene scene = new Scene(new KelolaBookingView(stage));
            stage.setScene(scene);
            stage.setMaximized(true);
        });
        kelolaJadwalBtn.setOnAction(e -> {
            Scene scene = new Scene(new KelolaJadwalView(stage));
            stage.setScene(scene);
            stage.setMaximized(true);
        });

        kelolaRuangBtn.setOnAction(e -> {
            Scene scene = new Scene(new KelolaRuangView(stage));
            stage.setScene(scene);
            stage.setMaximized(true);
        });

        kelolaBookingBtn.setOnAction(e -> {
            Scene scene = new Scene(new KelolaBookingView(stage));
            stage.setScene(scene);
            stage.setMaximized(true);
        });

        logoutBtn.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 400, 300)));

        VBox menu = new VBox(10, title, welcome, kelolaJadwalBtn, kelolaRuangBtn, kelolaBookingBtn, logoutBtn);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        setCenter(menu);
    }
}
