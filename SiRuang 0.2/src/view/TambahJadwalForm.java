package view;

import controller.JadwalController;
import database.DosenDB;
import database.MatkulDB;
import database.RuangDB;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Jadwal;
import util.SesiUtil;

public class TambahJadwalForm extends Stage {

    public TambahJadwalForm(Runnable onSukses) {
        setTitle("Tambah Jadwal");

        ComboBox<String> matkulCB = new ComboBox<>();
        ComboBox<String> dosenCB = new ComboBox<>();
        ComboBox<String> ruangCB = new ComboBox<>();
        ComboBox<String> hariCB = new ComboBox<>();
        ComboBox<Integer> sesiCB = new ComboBox<>();
        ComboBox<String> sksCB = new ComboBox<>();

        matkulCB.setEditable(true);
        matkulCB.getItems().addAll(MatkulDB.loadMatkul());

        dosenCB.setEditable(true);
        dosenCB.getItems().addAll(DosenDB.loadDosen());

        ruangCB.setEditable(true);
        ruangCB.getItems().addAll(RuangDB.loadRuang().stream().map(r -> r.getNama()).toList());

        hariCB.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat");
        sesiCB.getItems().addAll(SesiUtil.getPilihanSesiMulai());
        sksCB.getItems().addAll("1", "2", "3", "4");

        Button simpanBtn = new Button("Simpan");
        simpanBtn.setOnAction(e -> {
            String matkul = matkulCB.getEditor().getText().trim();
            String dosen = dosenCB.getEditor().getText().trim();
            String ruang = ruangCB.getEditor().getText().trim();
            String hari = hariCB.getValue();
            Integer sesi = sesiCB.getValue();
            String sksStr = sksCB.getValue();

            if (matkul.isEmpty() || dosen.isEmpty() || ruang.isEmpty() ||
                    hari == null || sesi == null || sksStr == null) {
                alert("Lengkapi semua isian.");
                return;
            }

            int sks = Integer.parseInt(sksStr);
            if (SesiUtil.sesiMelebihiBatas(sesi, sks)) {
                alert("Sesi melebihi batas maksimum (14).");
                return;
            }

            String jam = SesiUtil.getRentangWaktu(sesi, sks);
            Jadwal baru = new Jadwal(matkul, dosen, ruang, hari, jam);

            JadwalController.tambahJadwal(baru, () -> {
                onSukses.run();
                close();
            });
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Mata Kuliah:"), 0, 0); grid.add(matkulCB, 1, 0);
        grid.add(new Label("Dosen:"), 0, 1); grid.add(dosenCB, 1, 1);
        grid.add(new Label("Ruang:"), 0, 2); grid.add(ruangCB, 1, 2);
        grid.add(new Label("Hari:"), 0, 3); grid.add(hariCB, 1, 3);
        grid.add(new Label("Sesi Mulai:"), 0, 4); grid.add(sesiCB, 1, 4);
        grid.add(new Label("SKS:"), 0, 5); grid.add(sksCB, 1, 5);
        grid.add(simpanBtn, 1, 6);

        setScene(new Scene(grid));
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }
}
