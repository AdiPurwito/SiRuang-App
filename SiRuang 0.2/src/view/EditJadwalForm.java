package view;

import controller.JadwalController;
import database.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Jadwal;
import util.AlertUtil;
import util.AutoCompleteTextField;
import util.SesiUtil;

import java.util.Arrays;

public class EditJadwalForm extends Stage {

    public EditJadwalForm(Jadwal jadwalLama, Runnable onSukses) {
        setTitle("Edit Jadwal");

        TextField matkulTF = new TextField();
        TextField dosenTF = new TextField();
        TextField ruangTF = new TextField();
        ComboBox<String> hariCB = new ComboBox<>();
        ComboBox<Integer> sesiCB = new ComboBox<>();
        ComboBox<String> sksCB = new ComboBox<>();
        ComboBox<String> kelasCB = new ComboBox<>();
        TextField fakultasTF = new TextField();
        TextField prodiTF = new TextField();

        AutoCompleteTextField.attachTo(matkulTF, new MatkulDB().load());
        matkulTF.setText(jadwalLama.getMatkul());

        AutoCompleteTextField.attachTo(dosenTF, new DosenDB().load());
        dosenTF.setText(jadwalLama.getDosen());

        AutoCompleteTextField.attachTo(ruangTF, RuangDB.loadRuang().stream().map(r -> r.getNama()).toList());
        ruangTF.setText(jadwalLama.getRuang());

        hariCB.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat");
        hariCB.setValue(jadwalLama.getHari());

        sesiCB.getItems().addAll(SesiUtil.getPilihanSesiMulai());
        sksCB.getItems().addAll("1", "2", "3", "4");

        int[] sesiSKS = SesiUtil.ekstrakSesiDanSKS(jadwalLama.getJam());
        sesiCB.setValue(sesiSKS[0]);
        sksCB.setValue(String.valueOf(sesiSKS[1]));

        kelasCB.getItems().addAll(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"));
        kelasCB.setValue(jadwalLama.getKelas());

        AutoCompleteTextField.attachTo(fakultasTF, new FakultasDB().load());
        fakultasTF.setText(jadwalLama.getFakultas());

        AutoCompleteTextField.attachTo(prodiTF, new ProdiDB().load());
        prodiTF.setText(jadwalLama.getProdi());

        // Tombol
        Button simpanBtn = new Button("Simpan");
        Button kembaliBtn = new Button("Kembali");

        simpanBtn.setOnAction(e -> {
            String matkul = matkulTF.getText().trim();
            String dosen = dosenTF.getText().trim();
            String ruang = ruangTF.getText().trim();
            String hari = hariCB.getValue();
            Integer sesi = sesiCB.getValue();
            String sksStr = sksCB.getValue();
            String kelas = kelasCB.getValue();
            String fakultas = fakultasTF.getText().trim();
            String prodi = prodiTF.getText().trim();

            if (matkul.isEmpty() || dosen.isEmpty() || ruang.isEmpty() ||
                    hari == null || sesi == null || sksStr == null ||
                    kelas == null || fakultas.isEmpty() || prodi.isEmpty()) {
                AlertUtil.error("Lengkapi semua isian.");
                return;
            }

            int sks = Integer.parseInt(sksStr);
            if (SesiUtil.sesiMelebihiBatas(sesi, sks)) {
                alert("Sesi melebihi batas maksimum (14).");
                return;
            }

            // Simpan jika data baru
            new MatkulDB().saveIfNew(matkul);
            new DosenDB().saveIfNew(dosen);
            new FakultasDB().saveIfNew(fakultas);
            new ProdiDB().saveIfNew(prodi);


            String jam = SesiUtil.getRentangWaktu(sesi, sks);
            Jadwal baru = new Jadwal(hari, jam, matkul, jadwalLama.getSemester(), sksStr, kelas, dosen, ruang, fakultas, prodi);

            JadwalController.updateJadwal(jadwalLama, baru, () -> {
                onSukses.run();
                close();
            });
        });

        kembaliBtn.setOnAction(e -> close());

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Mata Kuliah:"), 0, 0); grid.add(matkulTF, 1, 0);
        grid.add(new Label("Dosen:"), 0, 1); grid.add(dosenTF, 1, 1);
        grid.add(new Label("Ruang:"), 0, 2); grid.add(ruangTF, 1, 2);
        grid.add(new Label("Hari:"), 0, 3); grid.add(hariCB, 1, 3);
        grid.add(new Label("Sesi Mulai:"), 0, 4); grid.add(sesiCB, 1, 4);
        grid.add(new Label("SKS:"), 0, 5); grid.add(sksCB, 1, 5);
        grid.add(new Label("Kelas:"), 0, 6); grid.add(kelasCB, 1, 6);
        grid.add(new Label("Fakultas:"), 0, 7); grid.add(fakultasTF, 1, 7);
        grid.add(new Label("Prodi:"), 0, 8); grid.add(prodiTF, 1, 8);

        HBox tombolBox = new HBox(10, simpanBtn, kembaliBtn);
        grid.add(tombolBox, 1, 9);

        setScene(new Scene(grid));
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }
}
