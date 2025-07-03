package view;

import controller.RuangController;
import database.RuangDB;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Ruang;
import util.AutoCompleteTextField;

public class TambahRuangForm extends VBox {
    private final Stage stage;

    public TambahRuangForm(Stage stage) {
        this.stage = stage;
        setSpacing(10);
        setPadding(new Insets(20));

        Label title = new Label("Tambah Ruang");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // === Input Nama Ruang (Autocomplete) ===
        Label namaLabel = new Label("Nama Ruang:");
        TextField namaField = new TextField();
        namaField.setPromptText("Contoh: R101");
        AutoCompleteTextField.attachTo(namaField,
                RuangDB.loadRuang().stream().map(Ruang::getNama).distinct().toList()
        );

        // === Gedung ===
        Label gedungLabel = new Label("Gedung:");
        ComboBox<String> gedungCB = new ComboBox<>();
        gedungCB.getItems().addAll("GKB 1", "GKB 2", "GKB 3", "GKB 4");
        gedungCB.setPromptText("Pilih Gedung");

        // === Kapasitas ===
        Label kapasitasLabel = new Label("Kapasitas:");
        TextField kapasitasField = new TextField();
        kapasitasField.setPromptText("Masukkan angka");

        // === Tombol Simpan & Kembali ===
        Button simpan = new Button("Simpan");
        Button kembali = new Button("Kembali");

        simpan.setOnAction(e -> {
            String nama = namaField.getText().trim();
            String gedung = gedungCB.getValue();
            String kapasitas = kapasitasField.getText().trim();

            if (nama.isEmpty() || gedung == null || kapasitas.isEmpty()) {
                showAlert("Semua field harus diisi.");
                return;
            }

            try {
                int kap = Integer.parseInt(kapasitas);
                Ruang ruang = new Ruang(nama, gedung, kap, 150); // default 150 sesi

                // 🟢 Gunakan controller dengan validasi
                RuangController.tambahRuang(ruang, () -> {
                    stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600));
                });

            } catch (NumberFormatException ex) {
                showAlert("Kapasitas harus berupa angka.");
            }
        });

        kembali.setOnAction(e -> stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600)));

        // === Susun Form ===
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        form.add(namaLabel, 0, 0); form.add(namaField, 1, 0);
        form.add(gedungLabel, 0, 1); form.add(gedungCB, 1, 1);
        form.add(kapasitasLabel, 0, 2); form.add(kapasitasField, 1, 2);
        form.add(new HBox(10, simpan, kembali), 1, 3);

        getChildren().addAll(title, form);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }
}
