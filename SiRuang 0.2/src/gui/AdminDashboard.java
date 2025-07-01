package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminDashboard extends BorderPane {
    private Stage stage;

    public AdminDashboard(Stage stage) {
        this.stage = stage;
        setPadding(new Insets(20));
        setTop(buildHeader());
        setCenter(buildMenu());
    }

    private HBox buildHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(10));
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_RIGHT);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            stage.setScene(new Scene(new LoginView(stage), 400, 300));
        });

        header.getChildren().add(logoutBtn);
        return header;
    }

    private VBox buildMenu() {
        VBox menu = new VBox();
        menu.setSpacing(20);
        menu.setAlignment(Pos.CENTER);

        Button btnKelolaJadwal = new Button("Kelola Jadwal");
        Button btnKelolaRuang = new Button("Kelola Ruang");
        Button btnKelolaBooking = new Button("Kelola Booking");

        btnKelolaJadwal.setPrefWidth(200);
        btnKelolaRuang.setPrefWidth(200);
        btnKelolaBooking.setPrefWidth(200);

        btnKelolaJadwal.setOnAction(e -> {
            stage.setScene(new Scene(new KelolaJadwalView(stage), 800, 600));
        });

        btnKelolaRuang.setOnAction(e -> {
            stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600));
        });

        btnKelolaBooking.setOnAction(e -> {
            stage.setScene(new Scene(new KelolaBookingView(stage), 800, 600));
        });

        menu.getChildren().addAll(btnKelolaJadwal, btnKelolaRuang, btnKelolaBooking);
        return menu;
    }
}
