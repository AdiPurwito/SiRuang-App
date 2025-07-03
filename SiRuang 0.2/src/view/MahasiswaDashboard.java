package view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MahasiswaDashboard extends BorderPane {
    private final String username;

    public MahasiswaDashboard(Stage stage, String username) {
        this.username = username;
        setPadding(new Insets(15));

        // Judul utama
        Label title = new Label("SIRuang");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Selamat datang
        Label welcome = new Label("Selamat datang di Dashboard Mahasiswa, " + username);
        welcome.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: montserrat");

        // Tombol menu
        Button btnJadwal = new Button("Jadwal Kuliah");
        Button btnCariRuang = new Button("Cari Ruang");
        Button btnRiwayat = new Button("Riwayat Booking");
        Button logout = new Button("Logout");

        btnJadwal.setPrefWidth(200);
        btnCariRuang.setPrefWidth(200);
        btnRiwayat.setPrefWidth(200);

        // Aksi tombol
        logout.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 400, 300)));
        btnJadwal.setOnAction(e -> {
            Scene scene = new Scene(new JadwalKuliahView(stage));
            stage.setScene(scene);
            stage.setMaximized(true);
        });

        btnCariRuang.setOnAction(e -> {
            Scene scene = new Scene(new CariRuangView(stage, username));
            stage.setScene(scene);
            stage.setMaximized(true);
        });

        btnRiwayat.setOnAction(e -> {
            Scene scene = new Scene(new RiwayatBookingView(stage, username));
            stage.setScene(scene);
            stage.setMaximized(true);
        });


        // Menu dengan teks selamat datang
        VBox menu = new VBox(15, title, welcome, btnJadwal, btnCariRuang, btnRiwayat, logout);
        menu.setAlignment(Pos.CENTER);

        setCenter(menu);
    }
}