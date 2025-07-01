package gui;

import database.RuangDB;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Ruang;

public class TambahRuangForm extends VBox {
    private Stage stage;

    public TambahRuangForm(Stage stage) {
        this.stage = stage;
        setSpacing(10);
        setPadding(new Insets(20));

        Label title = new Label("Tambah Ruang");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField namaField = new TextField();
        namaField.setPromptText("Nama Ruang (contoh: R101)");

        ComboBox<String> gedungCB = new ComboBox<>();
        gedungCB.getItems().addAll("GKB 1", "GKB 2", "GKB 3", "GKB 4");
        gedungCB.setPromptText("Gedung");

        TextField kapasitasField = new TextField();
        kapasitasField.setPromptText("Kapasitas");

        Button simpan = new Button("Simpan");
        Button kembali = new Button("Kembali");

        simpan.setOnAction(e -> {
            System.out.println(">> Tombol Simpan ditekan");

            String nama = namaField.getText();
            String gedung = gedungCB.getValue();
            String kapasitas = kapasitasField.getText();

            if (nama.isEmpty() || gedung == null || kapasitas.isEmpty()) {
                alert("Semua field harus diisi.");
                return;
            }

            try {
                int kap = Integer.parseInt(kapasitas);
                Ruang ruang = new Ruang(nama, gedung, kap, 150); // default sesi 150
                RuangDB.saveRuang(ruang); // gunakan saveRuang (bukan addRuang)
                alert("Ruang berhasil ditambahkan.");
                stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600));
            } catch (NumberFormatException ex) {
                alert("Kapasitas harus berupa angka.");
            }
        });

        kembali.setOnAction(e -> stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600)));

        getChildren().addAll(title, namaField, gedungCB, kapasitasField,
                new HBox(10, simpan, kembali));
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }
}
