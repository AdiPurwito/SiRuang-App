package view;

import controller.RuangController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Ruang;
import util.AlertUtil;

import java.util.Arrays;

public class EditRuangForm extends Stage {

    public EditRuangForm(Stage stage, Ruang ruangLama, Runnable onSukses) {
        setTitle("Edit Ruang");

        TextField namaField = new TextField(ruangLama.getNama());

        ComboBox<String> gedungCB = new ComboBox<>();
        gedungCB.getItems().addAll(Arrays.asList("GKB 1", "GKB 2", "GKB 3", "GKB 4"));
        gedungCB.setValue(ruangLama.getGedung());

        TextField kapasitasField = new TextField(String.valueOf(ruangLama.getKapasitas()));
        TextField sesiField = new TextField(String.valueOf(ruangLama.getJumlahSesi()));

        Button simpanBtn = new Button("Simpan");
        simpanBtn.setOnAction(e -> {
            String nama = namaField.getText().trim();
            String gedung = gedungCB.getEditor().getText().trim();
            String kapasitasStr = kapasitasField.getText().trim();
            String sesiStr = sesiField.getText().trim();

            if (nama.isEmpty() || gedung.isEmpty() || kapasitasStr.isEmpty() || sesiStr.isEmpty()) {
                alert("Lengkapi semua isian.");
                return;
            }

            try {
                int kapasitas = Integer.parseInt(kapasitasStr);
                int sesi = Integer.parseInt(sesiStr);
                Ruang baru = new Ruang(nama, gedung, kapasitas, sesi);

                RuangController.updateRuang(ruangLama, baru, () -> {
                    onSukses.run();
                    close();
                    stage.setScene(new Scene(new KelolaRuangView(stage), 800, 600));
                });
            } catch (NumberFormatException ex) {
                AlertUtil.error("Kapasitas dan Sesi harus berupa angka.");
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nama Ruang:"), 0, 0); grid.add(namaField, 1, 0);
        grid.add(new Label("Gedung:"), 0, 1); grid.add(gedungCB, 1, 1);
        grid.add(new Label("Kapasitas:"), 0, 2); grid.add(kapasitasField, 1, 2);
        grid.add(new Label("Jumlah Sesi:"), 0, 3); grid.add(sesiField, 1, 3);
        grid.add(simpanBtn, 1, 4);

        setScene(new Scene(grid));
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }
}
