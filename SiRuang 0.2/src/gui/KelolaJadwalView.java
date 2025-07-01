package gui;

import database.JadwalDB;
import database.ProdiDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Jadwal;

public class KelolaJadwalView extends BorderPane {
    private TableView<Jadwal> table;
    private ObservableList<Jadwal> allJadwal;
    private ObservableList<Jadwal> filteredJadwal;

    public KelolaJadwalView(Stage stage) {
        setPadding(new Insets(15));
        Label title = new Label("Kelola Jadwal");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button kembali = new Button("Kembali");
        kembali.setOnAction(e -> stage.setScene(new Scene(new AdminDashboard(stage), 800, 600)));

        Button tambah = new Button("Tambah Jadwal");
        tambah.setOnAction(e -> stage.setScene(new Scene(new TambahJadwalForm(stage), 800, 600)));

        ComboBox<String> filterProdi = new ComboBox<>();
        filterProdi.setPromptText("Filter Prodi");
        filterProdi.getItems().addAll(ProdiDB.load());
        filterProdi.setOnAction(e -> applyFilter(filterProdi.getValue()));

        HBox topBar = new HBox(10, kembali, tambah, new Label("Filter Prodi:"), filterProdi);
        topBar.setAlignment(Pos.CENTER_LEFT);
        VBox header = new VBox(10, title, topBar);
        header.setPadding(new Insets(10, 0, 10, 0));

        table = new TableView<>();
        setupTable(stage);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // Fit to width

        setTop(header);
        setCenter(table);

        allJadwal = FXCollections.observableArrayList(JadwalDB.loadJadwal());
        filteredJadwal = FXCollections.observableArrayList(allJadwal);
        table.setItems(filteredJadwal);
    }

    private void applyFilter(String prodi) {
        if (prodi == null || prodi.isEmpty()) {
            filteredJadwal.setAll(allJadwal);
        } else {
            filteredJadwal.setAll(allJadwal.filtered(j -> j.getProdi().equalsIgnoreCase(prodi)));
        }
    }

    private void setupTable(Stage stage) {
        String[] headers = {"Hari", "Jam", "Mata Kuliah", "Semester", "SKS", "Kelas", "Dosen", "Ruang"};
        String[] props = {"hari", "jam", "matkul", "semester", "sks", "kelas", "dosen", "ruang"};
        int[] widths = {90, 150, 200, 90, 70, 80, 350, 100};

        for (int i = 0; i < headers.length; i++) {
            TableColumn<Jadwal, String> col = new TableColumn<>(headers[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(props[i]));
            col.setPrefWidth(widths[i]);
            col.setResizable(false);
            table.getColumns().add(col);
        }

        TableColumn<Jadwal, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setPrefWidth(150);
        aksiCol.setResizable(false);
        aksiCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button delBtn = new Button("Hapus");

            {
                editBtn.setStyle("-fx-background-color: #ffc107;");
                delBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                editBtn.setOnAction(e -> {
                    Jadwal j = getTableView().getItems().get(getIndex());
                    stage.setScene(new Scene(new EditJadwalForm(stage, j), 800, 600));
                });

                delBtn.setOnAction(e -> {
                    Jadwal j = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Hapus jadwal ini?", ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(res -> {
                        if (res == ButtonType.YES) {
                            JadwalDB.deleteJadwal(j);
                            allJadwal.remove(j);
                            filteredJadwal.remove(j);
                        }
                    });
                });
            }

            private final HBox pane = new HBox(5, editBtn, delBtn);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        table.getColumns().add(aksiCol);
    }
}
