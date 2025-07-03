package view;

import database.BookingDB;
import database.RuangDB;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Booking;
import util.SesiUtil;

import java.util.List;
import java.util.stream.Collectors;

public class RiwayatBookingView extends BorderPane {
    private final String username;
    private TableView<Booking> table;
    private ObservableList<Booking> data;

    public RiwayatBookingView(Stage stage, String username) {
        this.username = username;
        setPadding(new Insets(15));

        Label title = new Label("Riwayat Booking - " + username);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button kembali = new Button("Kembali");
        kembali.setOnAction(e -> stage.setScene(new Scene(new MahasiswaDashboard(stage, username), 800, 600)));

        HBox buttonBar = new HBox(10, kembali);
        VBox header = new VBox(10, title, buttonBar);
        header.setPadding(new Insets(10, 0, 10, 0));

        table = new TableView<>();
        setupTable();
        refreshTable();

        setTop(header);
        setCenter(table);

        startAutoRefresh(); // 🔄 Aktifkan auto refresh
    }

    private void setupTable() {
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(500);
        table.setMaxHeight(Double.MAX_VALUE);

        TableColumn<Booking, String> gedungCol = new TableColumn<>("Gedung");
        gedungCol.setMinWidth(120);
        gedungCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Booking b = getTableView().getItems().get(getIndex());
                    String gedung = RuangDB.getGedungByRuang(b.getRuang());
                    setText(gedung);
                }
            }
        });
        gedungCol.setReorderable(false);
        gedungCol.setResizable(false);

        TableColumn<Booking, String> ruangCol = new TableColumn<>("Ruang");
        ruangCol.setCellValueFactory(new PropertyValueFactory<>("ruang"));
        ruangCol.setMinWidth(100);
        ruangCol.setReorderable(false);
        ruangCol.setResizable(false);

        TableColumn<Booking, String> hariCol = new TableColumn<>("Hari");
        hariCol.setCellValueFactory(new PropertyValueFactory<>("hari"));
        hariCol.setMinWidth(80);
        hariCol.setReorderable(false);
        hariCol.setResizable(false);

        TableColumn<Booking, String> jamCol = new TableColumn<>("Jam");
        jamCol.setCellValueFactory(new PropertyValueFactory<>("jam"));
        jamCol.setMinWidth(100);
        jamCol.setReorderable(false);
        jamCol.setResizable(false);

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setMinWidth(100);
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    setStyle("-fx-background-color: " + switch (status.toLowerCase()) {
                        case "menunggu" -> "#ffc107";
                        case "diterima" -> "#28a745";
                        case "ditolak" -> "#dc3545";
                        default -> "#e0e0e0";
                    } + "; -fx-text-fill: white;");
                }
            }
        });
        statusCol.setReorderable(false);
        statusCol.setResizable(false);

        TableColumn<Booking, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setMinWidth(100);
        aksiCol.setReorderable(false);
        aksiCol.setResizable(false);
        aksiCol.setCellFactory(param -> new TableCell<>() {
            private final Button batal = new Button("Batalkan");

            {
                batal.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                batal.setOnAction(e -> {
                    Booking b = getTableView().getItems().get(getIndex());
                    if (b.getStatus().equalsIgnoreCase("menunggu")) {
                        List<Booking> all = BookingDB.loadAll();
                        all.removeIf(old -> old.getPemesan().equals(b.getPemesan()) &&
                                old.getRuang().equals(b.getRuang()) &&
                                old.getHari().equals(b.getHari()) &&
                                old.getJam().equals(b.getJam()));
                        new BookingDB().saveAll(all);

                        int[] sesi = SesiUtil.ekstrakSesiDanSKS(b.getJam());
                        RuangDB.tambahSesi(b.getRuang(), sesi[1]);

                        refreshTable();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Booking berhasil dibatalkan.");
                        alert.showAndWait();
                    }
                });
            }

            private final HBox pane = new HBox(5, batal);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Booking b = getTableView().getItems().get(getIndex());
                    if (b.getStatus().equalsIgnoreCase("menunggu")) {
                        setGraphic(pane);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        table.getColumns().setAll(gedungCol, ruangCol, hariCol, jamCol, statusCol, aksiCol);
    }

    private void refreshTable() {
        List<Booking> all = BookingDB.loadAll();
        List<Booking> userBookings = all.stream()
                .filter(b -> b.getPemesan().equalsIgnoreCase(username))
                .collect(Collectors.toList());
        data = FXCollections.observableArrayList(userBookings);
        table.setItems(data);
    }

    private void startAutoRefresh() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> refreshTable())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
