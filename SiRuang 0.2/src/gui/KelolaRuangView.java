package gui;

import database.RuangDB;
import gui.TambahRuangForm;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Ruang;

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
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        setTop(header);
        setCenter(table);
        refreshTable();
    }

    private void setupTable(Stage stage) {
        String[] headers = {"Nama", "Gedung", "Kapasitas", "Sesi Tersisa"};
        String[] props = {"nama", "gedung", "kapasitas", "jumlahSesi"};
        int[] widths = {120, 120, 120, 120};

        for (int i = 0; i < headers.length; i++) {
            TableColumn<Ruang, String> col = new TableColumn<>(headers[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(props[i]));
            col.setPrefWidth(widths[i]);
            col.setResizable(false);
            table.getColumns().add(col);
        }

        TableColumn<Ruang, String> sesiTerpakaiCol = new TableColumn<>("Sesi Terpakai");
        sesiTerpakaiCol.setResizable(false);
        sesiTerpakaiCol.setPrefWidth(120);
        sesiTerpakaiCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Ruang r = getTableView().getItems().get(getIndex());
                    int terpakai = 150 - r.getJumlahSesi();
                    setText(String.valueOf(terpakai));
                }
            }
        });
        table.getColumns().add(sesiTerpakaiCol);

        TableColumn<Ruang, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setPrefWidth(150);
        aksiCol.setResizable(false);
        aksiCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button delBtn = new Button("Hapus");

            {
                editBtn.setStyle("-fx-background-color: #ffc107;");
                delBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                editBtn.setOnAction(e -> {
                    Ruang r = getTableView().getItems().get(getIndex());
                    stage.setScene(new Scene(new EditRuangForm(stage, r), 500, 400));
                });
                delBtn.setOnAction(e -> {
                    Ruang r = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Hapus ruang ini?", ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(res -> {
                        if (res == ButtonType.YES) {
                            RuangDB.deleteRuang(r);
                            refreshTable();
                        }
                    });
                });
            }

            private final HBox pane = new HBox(5, editBtn, delBtn);
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        table.getColumns().add(aksiCol);
    }

    private void refreshTable() {
        ObservableList<Ruang> data = FXCollections.observableArrayList(RuangDB.loadRuang());
        table.setItems(data);
    }
}
