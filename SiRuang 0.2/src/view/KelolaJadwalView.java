package view;

import controller.JadwalController;
import database.JadwalDB;
import database.ProdiDB;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Jadwal;
import util.AlertUtil;

import java.util.List;
import java.util.stream.Collectors;

public class KelolaJadwalView extends BorderPane {
    private TableView<Jadwal> table;
    private ComboBox<String> filterProdiCB;

    public KelolaJadwalView(Stage stage) {
        setPadding(new Insets(15));
        Label title = new Label("Kelola Jadwal");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button kembali = new Button("Kembali");
        Button tambah = new Button("Tambah Jadwal");

        kembali.setOnAction(e -> stage.setScene(new Scene(new AdminDashboard(stage), 800, 600)));
        tambah.setOnAction(e -> new TambahJadwalForm(this::refreshTable).show());

        filterProdiCB = new ComboBox<>();
        filterProdiCB.getItems().add("Semua");
        filterProdiCB.getItems().addAll(ProdiDB.load());
        filterProdiCB.setValue("Semua");
        filterProdiCB.setOnAction(e -> refreshTable());

        HBox headerBtn = new HBox(10, kembali, tambah, new Label("Filter Prodi:"), filterProdiCB);
        headerBtn.setAlignment(Pos.CENTER_LEFT);

        VBox header = new VBox(10, title, headerBtn);
        header.setPadding(new Insets(10, 0, 10, 0));

        table = new TableView<>();
        setupTable(stage);

        setTop(header);
        setCenter(table);
        refreshTable();
    }

    private void setupTable(Stage stage) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        String[] headers = {"Hari", "Jam", "Matkul", "Semester", "SKS", "Kelas", "Dosen", "Ruang", "Fakultas", "Prodi"};
        String[] props = {"hari", "jam", "matkul", "semester", "sks", "kelas", "dosen", "ruang", "fakultas", "prodi"};

        for (int i = 0; i < headers.length; i++) {
            TableColumn<Jadwal, String> col = new TableColumn<>(headers[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(props[i]));
            col.setPrefWidth(120);
            col.setResizable(false);
            table.getColumns().add(col);
        }

        TableColumn<Jadwal, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setMinWidth(150);
        aksiCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button hapusBtn = new Button("Hapus");

            {
                editBtn.setStyle("-fx-background-color: #ffc107;");
                hapusBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                editBtn.setOnAction(e -> {
                    Jadwal j = getTableView().getItems().get(getIndex());
                    new EditJadwalForm(j, KelolaJadwalView.this::refreshTable).show();
                });

                hapusBtn.setOnAction(e -> {
                    Jadwal j = getTableView().getItems().get(getIndex());
                    AlertUtil.confirm("Hapus jadwal ini?", () -> {
                        JadwalController.hapusJadwal(j, KelolaJadwalView.this::refreshTable);
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

        table.getColumns().add(aksiCol);
    }

    private void refreshTable() {
        List<Jadwal> semua = JadwalDB.loadJadwal();
        String selectedProdi = filterProdiCB.getValue();

        if (selectedProdi != null && !selectedProdi.equals("Semua")) {
            semua = semua.stream()
                    .filter(j -> selectedProdi.equalsIgnoreCase(j.getProdi()))
                    .collect(Collectors.toList());
        }

        table.setItems(FXCollections.observableArrayList(semua));
    }
}
