package view;

import database.FakultasDB;
import database.JadwalDB;
import database.ProdiDB;
import database.RuangDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Jadwal;
import model.Ruang;

import java.util.Comparator;

public class JadwalKuliahView extends BorderPane {

    public JadwalKuliahView(Stage stage) {
        setPadding(new Insets(15));

        Label title = new Label("Jadwal Kuliah");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button back = new Button("Kembali");
        back.setOnAction(e -> stage.setScene(new Scene(new MahasiswaDashboard(stage, "mhs"), 800, 600)));

        ObservableList<Jadwal> allData = FXCollections.observableArrayList(JadwalDB.loadJadwal());
        allData.sort(Comparator.comparing(Jadwal::getHari));
        ObservableList<Jadwal> tableData = FXCollections.observableArrayList(allData);

        TableView<Jadwal> table = new TableView<>();
        setupTable(table);
        table.setItems(tableData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(500);
        table.setMaxHeight(Double.MAX_VALUE);

        ComboBox<String> filterFakultas = new ComboBox<>();
        ComboBox<String> filterProdi = new ComboBox<>();
        filterFakultas.setPromptText("Fakultas");
        filterProdi.setPromptText("Prodi");

        filterFakultas.getItems().add("Semua");
        filterFakultas.getItems().addAll(new FakultasDB().load());
        filterFakultas.setValue("Semua"); // set default

        filterProdi.getItems().add("Semua");
        filterProdi.getItems().addAll(new ProdiDB().load());
        filterProdi.setValue("Semua"); // set default


        filterFakultas.setOnAction(e -> applyFilter(allData, filterFakultas, filterProdi, tableData));
        filterProdi.setOnAction(e -> applyFilter(allData, filterFakultas, filterProdi, tableData));

        HBox filterBar = new HBox(10, new Label("Filter:"), filterFakultas, filterProdi);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(5, 0, 10, 0));

        VBox header = new VBox(10, title, filterBar, back);
        header.setPadding(new Insets(10, 0, 10, 0));

        setTop(header);
        setCenter(table);
    }

    private void setupTable(TableView<Jadwal> table) {
        // Kolom Hari dengan penggabungan
        TableColumn<Jadwal, String> hariCol = new TableColumn<>("Hari");
        hariCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Jadwal current = getTableView().getItems().get(getIndex());
                    int index = getIndex();
                    if (index == 0 || !current.getHari().equals(getTableView().getItems().get(index - 1).getHari())) {
                        setText(current.getHari());
                    } else {
                        setText("");
                    }
                }
            }
        });
        hariCol.setMinWidth(100);
        hariCol.setReorderable(false);
        hariCol.setResizable(false);

        // Kolom Jam
        TableColumn<Jadwal, String> jamCol = new TableColumn<>("Jam");
        jamCol.setCellValueFactory(new PropertyValueFactory<>("jam"));
        jamCol.setMinWidth(120);

        // Kolom GKB (dari ruang)
        TableColumn<Jadwal, String> gkbCol = new TableColumn<>("GKB");
        gkbCol.setCellValueFactory(data -> {
            String gedung = RuangDB.getGedungByRuang(data.getValue().getRuang());
            return new SimpleStringProperty(gedung);

        });
        gkbCol.setMinWidth(80);

        // Kolom Ruang
        TableColumn<Jadwal, String> ruangCol = new TableColumn<>("Ruang");
        ruangCol.setCellValueFactory(new PropertyValueFactory<>("ruang"));
        ruangCol.setMinWidth(80);

        // Kolom lain
        TableColumn<Jadwal, String> matkulCol = new TableColumn<>("Mata Kuliah");
        matkulCol.setCellValueFactory(new PropertyValueFactory<>("matkul"));
        matkulCol.setMinWidth(150);

        TableColumn<Jadwal, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        semesterCol.setMinWidth(80);

        TableColumn<Jadwal, String> kelasCol = new TableColumn<>("Kelas");
        kelasCol.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        kelasCol.setMinWidth(60);

        TableColumn<Jadwal, String> sksCol = new TableColumn<>("SKS");
        sksCol.setCellValueFactory(new PropertyValueFactory<>("sks"));
        sksCol.setMinWidth(60);

        TableColumn<Jadwal, String> dosenCol = new TableColumn<>("Dosen");
        dosenCol.setCellValueFactory(new PropertyValueFactory<>("dosen"));
        dosenCol.setMinWidth(300);

        // Tambahkan semua kolom sesuai urutan baru
        table.getColumns().addAll(hariCol, jamCol, gkbCol, ruangCol, matkulCol, semesterCol, kelasCol, sksCol, dosenCol);
    }

    private void applyFilter(ObservableList<Jadwal> all, ComboBox<String> f, ComboBox<String> p, ObservableList<Jadwal> target) {
        String fakultas = f.getValue();
        String prodi = p.getValue();

        target.setAll(all.filtered(j ->
                ("Semua".equalsIgnoreCase(fakultas) || fakultas == null || j.getFakultas().equalsIgnoreCase(fakultas)) &&
                        ("Semua".equalsIgnoreCase(prodi) || prodi == null || j.getProdi().equalsIgnoreCase(prodi))
        ));
    }

}
