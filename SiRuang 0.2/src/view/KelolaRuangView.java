package view;

import controller.RuangController;
import database.RuangDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Ruang;
import util.AlertUtil;

public class KelolaRuangView extends BorderPane {
    private TableView<Ruang> table;

    public KelolaRuangView(Stage stage) {
        setPadding(new Insets(15));

        Label title = new Label("Kelola Ruang");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button kembali = new Button("Kembali");
        Button tambah = new Button("Tambah Ruang");
        Button resetSesi = new Button("Reset Semua Sesi");

        kembali.setOnAction(e -> stage.setScene(new Scene(new AdminDashboard(stage), 800, 600)));
        tambah.setOnAction(e -> stage.setScene(new Scene(new TambahRuangForm(stage), 500, 400)));
        resetSesi.setOnAction(e -> {
            RuangDB.resetSemuaSesi();
            refreshTable();
        });

        HBox topBar = new HBox(10, kembali, tambah, resetSesi);
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox header = new VBox(10, title, topBar);
        header.setPadding(new Insets(10, 0, 10, 0));

        table = new TableView<>();
        setupTable(stage);

        setTop(header);
        setCenter(table);
        refreshTable();
    }

    private void setupTable(Stage stage) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Ruang, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Ruang, String> gedungCol = new TableColumn<>("Gedung");
        gedungCol.setCellValueFactory(new PropertyValueFactory<>("gedung"));

        TableColumn<Ruang, Integer> kapasitasCol = new TableColumn<>("Kapasitas");
        kapasitasCol.setCellValueFactory(new PropertyValueFactory<>("kapasitas"));

        TableColumn<Ruang, Integer> sesiTersisaCol = new TableColumn<>("Sesi Tersisa");
        sesiTersisaCol.setCellValueFactory(new PropertyValueFactory<>("jumlahSesi"));

        TableColumn<Ruang, String> sesiTerpakaiCol = new TableColumn<>("Sesi Terpakai");
        sesiTerpakaiCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty) {
                    setText(null);
                } else {
                    Ruang r = getTableView().getItems().get(getIndex());
                    int terpakai = 150 - r.getJumlahSesi();
                    setText(String.valueOf(terpakai));
                }
            }
        });

        TableColumn<Ruang, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button hapusBtn = new Button("Hapus");

            {
                editBtn.setStyle("-fx-background-color: #ffc107;");
                hapusBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                editBtn.setOnAction(e -> {
                    Ruang r = getTableView().getItems().get(getIndex());
                    new EditRuangForm(stage, r, KelolaRuangView.this::refreshTable).show();
                });

                hapusBtn.setOnAction(e -> {
                    Ruang r = getTableView().getItems().get(getIndex());
                    AlertUtil.confirm("Yakin ingin menghapus ruang ini?", () -> {
                        RuangController.hapusRuang(r, KelolaRuangView.this::refreshTable);
                    });
                });

            }

            private final HBox pane = new HBox(5, editBtn, hapusBtn);
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        table.getColumns().addAll(namaCol, gedungCol, kapasitasCol, sesiTersisaCol, sesiTerpakaiCol, aksiCol);
    }

    private void refreshTable() {
        ObservableList<Ruang> ruangList = FXCollections.observableArrayList(RuangDB.loadRuang());
        table.setItems(ruangList);
    }
}
