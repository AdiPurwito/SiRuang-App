package gui;

import database.RuangDB;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Ruang;

public class EditRuangForm extends VBox {
    public EditRuangForm(Stage stage, Ruang lama) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label title = new Label("Edit Ruang " + lama.getNama());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField namaField = new TextField(lama.getNama()); // Sekarang bisa diedit

        ComboBox<String> gedungCB = new ComboBox<>();
        gedungCB.getItems().addAll("GKB 1", "GKB 2", "GKB 3", "GKB 4");
        gedungCB.setValue(lama.getGedung());

        TextField kapasitasField = new TextField(String.valueOf(lama.getKapasitas()));

        Button simpan = new Button("Simpan");
        Button kembali = new Button("Kembali");

        simpan.setOnAction(e -> {
            try {
                String namaBaru = namaField.getText();
                String gedungBaru = gedungCB.getValue();
                int kapasitasBaru = Integer.parseInt(kapasitasField.getText());

                Ruang baru = new Ruang(namaBaru, gedungBaru, kapasitasBaru, lama.getJumlahSesi());

                RuangDB.updateRuang(lama, baru);

                alert("Ruang berhasil diperbarui.");
                stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600));
            } catch (Exception ex) {
                alert("Input tidak valid. Pastikan kapasitas berupa angka.");
            }
        });

        kembali.setOnAction(e -> stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600)));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER_LEFT);

        form.add(new Label("Nama Ruang:"), 0, 0); form.add(namaField, 1, 0);
        form.add(new Label("Gedung:"), 0, 1); form.add(gedungCB, 1, 1);
        form.add(new Label("Kapasitas:"), 0, 2); form.add(kapasitasField, 1, 2);

        HBox tombol = new HBox(10, simpan, kembali);
        tombol.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(title, form, tombol);
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }
}
