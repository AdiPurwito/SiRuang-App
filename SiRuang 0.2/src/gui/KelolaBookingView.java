package gui;

import database.BookingDB;
import database.JadwalDB;
import database.RuangDB;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Booking;
import model.Jadwal;
import model.Ruang;

import java.util.List;
import java.util.stream.Collectors;

public class KelolaBookingView extends BorderPane {
    private TableView<Booking> table;

    public KelolaBookingView(Stage stage) {
        setPadding(new Insets(15));
        Label title = new Label("Kelola Booking");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button kembali = new Button("Kembali");
        kembali.setOnAction(e -> stage.setScene(new Scene(new AdminDashboard(stage), 800, 600)));

        VBox header = new VBox(10, title, kembali);
        header.setPadding(new Insets(10, 0, 10, 0));

        table = new TableView<>();
        setupTable();

        setTop(header);
        setCenter(table);
        refreshTable();
    }

    private void setupTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(500);
        table.setMaxHeight(Double.MAX_VALUE);

        String[] headers = {"Pemesan", "Ruang", "Hari", "Jam", "Status"};
        String[] props = {"pemesan", "ruang", "hari", "jam", "status"};

        for (int i = 0; i < headers.length; i++) {
            TableColumn<Booking, String> col = new TableColumn<>(headers[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(props[i]));
            col.setReorderable(false);
            col.setResizable(false);
            col.setMinWidth(120);
            table.getColumns().add(col);
        }

        TableColumn<Booking, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setMinWidth(200);
        aksiCol.setResizable(false);
        aksiCol.setReorderable(false);
        aksiCol.setCellFactory(param -> new TableCell<>() {
            private final Button terima = new Button("Terima");
            private final Button tolak = new Button("Tolak");
            private final HBox pane = new HBox(5, terima, tolak);

            {
                terima.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                tolak.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                terima.setOnAction(e -> {
                    Booking b = getTableView().getItems().get(getIndex());
                    if (isBentrok(b)) {
                        show("Booking ini bentrok dengan jadwal lain!");
                        return;
                    }
                    kurangiSesi(b.getRuang());
                    BookingDB.updateStatus(b, "diterima");
                    refreshTable(); // refresh setelah aksi
                });

                tolak.setOnAction(e -> {
                    Booking b = getTableView().getItems().get(getIndex());
                    BookingDB.updateStatus(b, "ditolak");
                    refreshTable(); // refresh setelah aksi
                });
            }

            private void show(String msg) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
                a.showAndWait();
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane); // tampilkan tombol hanya jika belum kosong
            }
        });

        table.getColumns().add(aksiCol);
    }

    private boolean isBentrok(Booking baru) {
        for (Jadwal j : JadwalDB.loadJadwal()) {
            if (j.getHari().equalsIgnoreCase(baru.getHari()) &&
                    j.getRuang().equalsIgnoreCase(baru.getRuang()) &&
                    JadwalDB.jamTumpangTindih(j.getJam(), baru.getJam())) {
                return true;
            }
        }
        for (Booking b : BookingDB.loadAll()) {
            if (b.getStatus().equals("diterima") &&
                    b.getHari().equalsIgnoreCase(baru.getHari()) &&
                    b.getRuang().equalsIgnoreCase(baru.getRuang()) &&
                    JadwalDB.jamTumpangTindih(b.getJam(), baru.getJam())) {
                return true;
            }
        }
        return false;
    }

    private void kurangiSesi(String ruangNama) {
        List<Ruang> ruangList = RuangDB.loadRuang();
        for (Ruang r : ruangList) {
            if (r.getNama().equalsIgnoreCase(ruangNama)) {
                r.setJumlahSesi(r.getJumlahSesi() - 1);
                break;
            }
        }
        RuangDB.saveAll(ruangList);
    }

    private void refreshTable() {
        List<Booking> all = BookingDB.loadAll();
        List<Booking> menunggu = all.stream()
                .filter(b -> b.getStatus().equalsIgnoreCase("menunggu"))
                .collect(Collectors.toList());
        table.setItems(FXCollections.observableArrayList(menunggu));
    }
}
