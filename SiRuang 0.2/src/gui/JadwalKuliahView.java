package gui;

import database.FakultasDB;
import database.JadwalDB;
import database.ProdiDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Jadwal;

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

        filterFakultas.getItems().addAll(FakultasDB.load());
        filterProdi.getItems().addAll(ProdiDB.load());

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

        String[] headers = {"Jam", "Mata Kuliah", "Semester", "SKS", "Kelas", "Dosen", "Ruang"};
        String[] props = {"jam", "matkul", "semester", "sks", "kelas", "dosen", "ruang"};
        int[] widths = {120, 150, 80, 60, 60, 350, 80};

        table.getColumns().add(hariCol);

        for (int i = 0; i < headers.length; i++) {
            TableColumn<Jadwal, String> col = new TableColumn<>(headers[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(props[i]));
            col.setMinWidth(widths[i]);
            col.setResizable(false);
            col.setReorderable(false);
            table.getColumns().add(col);
        }
    }

    private void applyFilter(ObservableList<Jadwal> all, ComboBox<String> f, ComboBox<String> p, ObservableList<Jadwal> target) {
        String fakultas = f.getValue();
        String prodi = p.getValue();
        target.setAll(all.filtered(j ->
                (fakultas == null || j.getFakultas().equalsIgnoreCase(fakultas)) &&
                        (prodi == null || j.getProdi().equalsIgnoreCase(prodi))
        ));
    }
}
