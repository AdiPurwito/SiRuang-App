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

        Label title = new Label("SIRuang");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label welcome = new Label("Dashboard Mahasiswa - " + username);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnJadwal = new Button("Jadwal Kuliah");
        Button btnCariRuang = new Button("Cari Ruang");
        Button btnRiwayat = new Button("Riwayat Booking");
        Button logout = new Button("Logout");

        btnJadwal.setPrefWidth(200);
        btnCariRuang.setPrefWidth(200);
        btnRiwayat.setPrefWidth(200);

        logout.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 400, 300)));
        btnJadwal.setOnAction(e -> stage.setScene(new Scene(new JadwalKuliahView(stage), 800, 600)));
        btnCariRuang.setOnAction(e -> stage.setScene(new Scene(new CariRuangView(stage, username), 800, 600)));
        btnRiwayat.setOnAction(e -> stage.setScene(new Scene(new RiwayatBookingView(stage, username), 800, 600)));

        VBox menu = new VBox(15, title, btnJadwal, btnCariRuang, btnRiwayat, logout);
        menu.setAlignment(Pos.CENTER);

        setCenter(menu);
    }
}
