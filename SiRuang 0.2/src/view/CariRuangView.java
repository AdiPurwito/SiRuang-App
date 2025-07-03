package view;

import database.RuangDB;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Ruang;
import util.AlertUtil;
import util.SesiUtil;
import util.StatusUtil;
import util.AutoCompleteTextField;

import java.util.*;

public class CariRuangView extends BorderPane {
    private final String username;
    private TableView<Ruang> table;
    private ObservableList<Ruang> ruangList;
    private ComboBox<String> filterGedung;
    private ComboBox<String> filterStatus;

    public CariRuangView(Stage stage, String username) {
        this.username = username;

        setPadding(new Insets(15));
        Label title = new Label("Cari Ruang Kosong");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button kembali = new Button("Kembali");
        kembali.setOnAction(e -> stage.setScene(new Scene(new MahasiswaDashboard(stage, username), 800, 600)));

        filterGedung = new ComboBox<>();
        filterGedung.getItems().addAll("GKB 1", "GKB 2", "GKB 3", "GKB 4");
        filterGedung.setPromptText("Pilih Gedung");

        filterStatus = new ComboBox<>();
        filterStatus.getItems().addAll("Semua", "Kosong", "Menunggu", "Terbooking", "Terpakai Jadwal");
        filterStatus.setPromptText("Pilih Status");

        filterGedung.setOnAction(e -> applyFilters());
        filterStatus.setOnAction(e -> applyFilters());

        HBox topBar = new HBox(10, kembali, filterGedung, filterStatus);
        topBar.setAlignment(Pos.CENTER_LEFT);

        table = new TableView<>();
        setupTable();

        VBox header = new VBox(10, title, topBar);
        header.setPadding(new Insets(10, 0, 10, 0));

        // === Form Booking ===
        ComboBox<String> gedungCB = new ComboBox<>();
        gedungCB.getItems().addAll("GKB 1", "GKB 2", "GKB 3", "GKB 4");
        gedungCB.setPromptText("Gedung");

        TextField ruangField = new TextField();
        ruangField.setPromptText("Ruang (cth: R101)");

        ComboBox<Integer> sesiCB = new ComboBox<>();
        sesiCB.getItems().addAll(SesiUtil.getPilihanSesiMulai());
        sesiCB.setPromptText("Sesi Mulai");

        ComboBox<String> sksCB = new ComboBox<>();
        sksCB.getItems().addAll("1", "2", "3", "4");
        sksCB.setPromptText("SKS");

        Label labelWaktu = new Label("Waktu: -");

        List<String> semuaRuang = RuangDB.loadRuang().stream()
                .map(Ruang::getNama)
                .distinct()
                .toList();
        AutoCompleteTextField.attachTo(ruangField, semuaRuang);

        gedungCB.setOnAction(e -> {
            String selectedGedung = gedungCB.getValue();
            List<String> ruangFiltered;

            if (selectedGedung == null || selectedGedung.isEmpty()) {
                ruangFiltered = semuaRuang;
            } else {
                ruangFiltered = RuangDB.loadRuang().stream()
                        .filter(r -> r.getGedung().equalsIgnoreCase(selectedGedung))
                        .map(Ruang::getNama)
                        .distinct()
                        .toList();
            }

            AutoCompleteTextField.attachTo(ruangField, ruangFiltered);
        });

        sesiCB.setOnAction(e -> updateWaktuLabel(sesiCB, sksCB, labelWaktu));
        sksCB.setOnAction(e -> updateWaktuLabel(sesiCB, sksCB, labelWaktu));

        Button bookingBtn = new Button("Booking");

        bookingBtn.setOnAction(e -> {
            String ruang = ruangField.getText().trim();
            Integer sesiInt = sesiCB.getValue();

            if (ruang.isEmpty() || sesiInt == null || sksCB.getValue() == null) {
                AlertUtil.error("Lengkapi semua kolom untuk booking.");
                return;
            }

            int sksInt = Integer.parseInt(sksCB.getValue());

            if (SesiUtil.sesiMelebihiBatas(sesiInt, sksInt)) {
                AlertUtil.error("Sesi melebihi batas maksimum (maks 14).\nSilakan pilih sesi lebih awal.");
                return;
            }

            controller.CariRuangController.prosesBooking(
                    username, ruang, sesiInt, sksInt, () -> {
                        ruangField.clear();
                        sesiCB.setValue(null);
                        sksCB.setValue(null);
                        labelWaktu.setText("Waktu: -");
                        refreshTable();
                    });
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Gedung:"), 0, 0); form.add(gedungCB, 1, 0);
        form.add(new Label("Ruang:"), 0, 1); form.add(ruangField, 1, 1);
        form.add(new Label("Sesi Mulai:"), 0, 2); form.add(sesiCB, 1, 2);
        form.add(new Label("SKS:"), 0, 3); form.add(sksCB, 1, 3);
        form.add(new Label("Waktu:"), 0, 4); form.add(labelWaktu, 1, 4);
        form.add(bookingBtn, 1, 5);

        VBox content = new VBox(10, table, new Separator(), new Label("Form Booking Ruangan"), form);
        content.setPadding(new Insets(10));

        setTop(header);
        setCenter(content);
        refreshTable();
        startAutoRefresh();
    }

    private void updateWaktuLabel(ComboBox<Integer> sesiCB, ComboBox<String> sksCB, Label label) {
        if (sesiCB.getValue() != null && sksCB.getValue() != null) {
            int sesi = sesiCB.getValue();
            int sks = Integer.parseInt(sksCB.getValue());
            label.setText("Waktu: " + SesiUtil.getRentangWaktu(sesi, sks));
        }
    }

    private void setupTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(1000);
        table.setPrefHeight(450);

        TableColumn<Ruang, String> nama = new TableColumn<>("Ruang");
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Ruang, String> gedung = new TableColumn<>("Gedung");
        gedung.setCellValueFactory(new PropertyValueFactory<>("gedung"));

        TableColumn<Ruang, Integer> kapasitas = new TableColumn<>("Kapasitas");
        kapasitas.setCellValueFactory(new PropertyValueFactory<>("kapasitas"));

        TableColumn<Ruang, Integer> sesi = new TableColumn<>("Sesi Tersisa");
        sesi.setCellValueFactory(new PropertyValueFactory<>("jumlahSesi"));

        TableColumn<Ruang, String> status = new TableColumn<>("Status");
        status.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Ruang r = getTableView().getItems().get(getIndex());
                    String s = StatusUtil.statusRuangNow(r.getNama());
                    setText(s);
                    setStyle("-fx-background-color: " + switch (s) {
                        case "Terpakai Jadwal" -> "#dc3545";
                        case "Terbooking" -> "#007bff";
                        case "Menunggu" -> "#ffc107";
                        case "Di Luar Sesi" -> "#6c757d";
                        default -> "#28a745";
                    } + "; -fx-text-fill: white;");
                }
            }
        });

        table.getColumns().addAll(nama, gedung, kapasitas, sesi, status);
    }

    private void refreshTable() {
        ruangList = FXCollections.observableArrayList(RuangDB.loadRuang());
        applyFilters();
    }

    private void applyFilters() {
        List<Ruang> all = RuangDB.loadRuang();
        String gedung = filterGedung.getValue();
        String status = filterStatus.getValue();

        List<Ruang> filtered = all.stream().filter(r -> {
            boolean matchGedung = gedung == null || r.getGedung().equalsIgnoreCase(gedung);
            boolean matchStatus = true;
            if (status != null && !"Semua".equalsIgnoreCase(status)) {
                String s = StatusUtil.statusRuangNow(r.getNama());
                matchStatus = s.equalsIgnoreCase(status);
            }
            return matchGedung && matchStatus;
        }).toList();

        table.setItems(FXCollections.observableArrayList(filtered));
    }

    private void startAutoRefresh() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> refreshTable()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
