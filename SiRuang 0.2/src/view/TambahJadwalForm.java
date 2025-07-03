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
import util.AutoCompleteTextField;
import util.SesiUtil;

public class TambahJadwalForm extends Stage {

    public TambahJadwalForm(Runnable onSukses) {
        setTitle("Tambah Jadwal Baru");

        ComboBox<String> hariCB = new ComboBox<>();
        ComboBox<Integer> sesiCB = new ComboBox<>();
        ComboBox<String> sksCB = new ComboBox<>();
        Label waktuLbl = new Label("Waktu: -");

        TextField matkulTF = new TextField();
        ComboBox<String> semesterCB = new ComboBox<>();
        ComboBox<String> kelasCB = new ComboBox<>();
        TextField dosenTF = new TextField();
        TextField ruangTF = new TextField();
        TextField fakultasTF = new TextField();
        TextField prodiTF = new TextField();

        hariCB.setPromptText("Pilih Hari");
        hariCB.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat");
        sesiCB.setPromptText("Pilih Sesi");
        sesiCB.getItems().addAll(SesiUtil.getPilihanSesiMulai());
        sksCB.setPromptText("Pilih SKS");
        sksCB.getItems().addAll("1", "2", "3", "4", "5", "6");
        semesterCB.setPromptText("Pilih Semester");
        semesterCB.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8");
        kelasCB.setPromptText("Pilih Kelas");
        kelasCB.getItems().addAll("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N");

        sesiCB.setOnAction(e -> updateWaktuLabel(sesiCB, sksCB, waktuLbl));
        sksCB.setOnAction(e -> updateWaktuLabel(sesiCB, sksCB, waktuLbl));

        // ✅ Autocomplete fields
        AutoCompleteTextField.attachTo(matkulTF, new MatkulDB().load());
        AutoCompleteTextField.attachTo(dosenTF, new DosenDB().load());
        AutoCompleteTextField.attachTo(ruangTF, new RuangDB().load().stream().map(r -> r.getNama()).toList());
        AutoCompleteTextField.attachTo(fakultasTF, new FakultasDB().load());
        AutoCompleteTextField.attachTo(prodiTF, new ProdiDB().load());

        Button simpanBtn = new Button("Simpan");
        Button kembaliBtn = new Button("Kembali");
        kembaliBtn.setOnAction(e -> close());

        simpanBtn.setOnAction(e -> {
            Integer sesi = sesiCB.getValue();
            String sksStr = sksCB.getValue();
            String jam = (sesi != null && sksStr != null) ? SesiUtil.getRentangWaktu(sesi, Integer.parseInt(sksStr)) : null;

            // Simpan jika data baru (✅ saveIfNew)
            String matkul = matkulTF.getText().trim();
            String dosen = dosenTF.getText().trim();
            String ruang = ruangTF.getText().trim();
            String fakultas = fakultasTF.getText().trim();
            String prodi = prodiTF.getText().trim();

            new MatkulDB().saveIfNew(matkul);
            new DosenDB().saveIfNew(dosen);
            new FakultasDB().saveIfNew(fakultas);
            new ProdiDB().saveIfNew(prodi);


            // RuangDB tidak punya saveIfNew karena menggunakan objek

            Jadwal j = new Jadwal(
                    hariCB.getValue(),
                    jam,
                    matkul,
                    semesterCB.getValue(),
                    sksStr,
                    kelasCB.getValue(),
                    dosen,
                    ruang,
                    fakultas,
                    prodi
            );

            JadwalController.tambahJadwal(j, () -> {
                onSukses.run();
                close();
            });
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Hari:"), 0, 0); grid.add(hariCB, 1, 0);
        grid.add(new Label("Sesi Mulai:"), 0, 1); grid.add(sesiCB, 1, 1);
        grid.add(new Label("SKS:"), 0, 2); grid.add(sksCB, 1, 2);
        grid.add(new Label("Waktu:"), 0, 3); grid.add(waktuLbl, 1, 3);
        grid.add(new Label("Mata Kuliah:"), 0, 4); grid.add(matkulTF, 1, 4);
        grid.add(new Label("Semester:"), 0, 5); grid.add(semesterCB, 1, 5);
        grid.add(new Label("Kelas:"), 0, 6); grid.add(kelasCB, 1, 6);
        grid.add(new Label("Dosen:"), 0, 7); grid.add(dosenTF, 1, 7);
        grid.add(new Label("Ruang:"), 0, 8); grid.add(ruangTF, 1, 8);
        grid.add(new Label("Fakultas:"), 0, 9); grid.add(fakultasTF, 1, 9);
        grid.add(new Label("Prodi:"), 0, 10); grid.add(prodiTF, 1, 10);

        HBox tombolBox = new HBox(10, simpanBtn, kembaliBtn);
        grid.add(tombolBox, 1, 11);

        setScene(new Scene(grid));
    }

    private void updateWaktuLabel(ComboBox<Integer> sesiCB, ComboBox<String> sksCB, Label lbl) {
        Integer sesi = sesiCB.getValue();
        String sksStr = sksCB.getValue();
        if (sesi != null && sksStr != null) {
            int sks = Integer.parseInt(sksStr);
            if (!SesiUtil.sesiMelebihiBatas(sesi, sks)) {
                lbl.setText("Waktu: " + SesiUtil.getRentangWaktu(sesi, sks));
            } else {
                lbl.setText("Waktu: -");
            }
        }
    }
}
