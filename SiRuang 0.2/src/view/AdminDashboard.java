package view;

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
        setPadding(new Insets(20));

        Label title = new Label("SIRuang");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label welcome = new Label("Selamat datang di Dashboard Admin");
        welcome.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button kelolaJadwalBtn = new Button("Kelola Jadwal");
        Button kelolaRuangBtn = new Button("Kelola Ruang");
        Button kelolaBookingBtn = new Button("Kelola Booking");
        Button logoutBtn = new Button("Logout");

        kelolaJadwalBtn.setPrefWidth(200);
        kelolaRuangBtn.setPrefWidth(200);
        kelolaBookingBtn.setPrefWidth(200);

        kelolaJadwalBtn.setOnAction(e -> stage.setScene(new Scene(new KelolaJadwalView(stage), 800, 600)));
        kelolaRuangBtn.setOnAction(e -> stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600)));
        kelolaBookingBtn.setOnAction(e -> stage.setScene(new Scene(new KelolaBookingView(stage), 800, 600)));
        logoutBtn.setOnAction(e -> stage.setScene(new Scene(new LoginView(stage), 400, 300)));

        VBox menu = new VBox(10, title, welcome, kelolaJadwalBtn, kelolaRuangBtn, kelolaBookingBtn, logoutBtn);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        setCenter(menu);
    }
}
