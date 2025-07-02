package view;

import controller.JadwalController;
import database.DosenDB;
import database.MatkulDB;
import database.RuangDB;
import database.FakultasDB;
import database.ProdiDB;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Jadwal;
import util.SesiUtil;

public class TambahJadwalForm extends Stage {

    public TambahJadwalForm(Runnable onSukses) {
        setTitle("Tambah Jadwal Baru");

        ComboBox<String> hariCB = new ComboBox<>();
        ComboBox<Integer> sesiCB = new ComboBox<>();
        ComboBox<String> sksCB = new ComboBox<>();
        Label waktuLbl = new Label("Waktu: -");

        ComboBox<String> matkulCB = new ComboBox<>();
        ComboBox<String> semesterCB = new ComboBox<>();
        TextField kelasTF = new TextField();
        ComboBox<String> dosenCB = new ComboBox<>();
        ComboBox<String> ruangCB = new ComboBox<>();
        ComboBox<String> fakultasCB = new ComboBox<>();
        ComboBox<String> prodiCB = new ComboBox<>();

        hariCB.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat");
        sesiCB.getItems().addAll(SesiUtil.getPilihanSesiMulai());
        sksCB.getItems().addAll("1", "2", "3", "4");

        matkulCB.setEditable(true);
        matkulCB.getItems().addAll(MatkulDB.loadMatkul());

        semesterCB.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8");
        dosenCB.setEditable(true);
        dosenCB.getItems().addAll(DosenDB.loadDosen());

        ruangCB.setEditable(true);
        ruangCB.getItems().addAll(RuangDB.loadRuang().stream().map(r -> r.getNama()).toList());

        fakultasCB.getItems().addAll(FakultasDB.load());
        prodiCB.getItems().addAll(ProdiDB.load());

        sesiCB.setOnAction(e -> updateWaktuLabel(sesiCB, sksCB, waktuLbl));
        sksCB.setOnAction(e -> updateWaktuLabel(sesiCB, sksCB, waktuLbl));

        Button simpanBtn = new Button("Simpan");
        Button kembaliBtn = new Button("Kembali");
        kembaliBtn.setOnAction(e -> close());

        simpanBtn.setOnAction(e -> {
            Integer sesi = sesiCB.getValue();
            String sksStr = sksCB.getValue();
            String jam = (sesi != null && sksStr != null) ? SesiUtil.getRentangWaktu(sesi, Integer.parseInt(sksStr)) : null;

            Jadwal j = new Jadwal(
                    hariCB.getValue(),
                    jam,
                    matkulCB.getEditor().getText().trim(),
                    semesterCB.getValue(),
                    sksStr,
                    kelasTF.getText().trim(),
                    dosenCB.getEditor().getText().trim(),
                    ruangCB.getEditor().getText().trim(),
                    fakultasCB.getValue(),
                    prodiCB.getValue()
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
        grid.add(new Label("Mata Kuliah:"), 0, 4); grid.add(matkulCB, 1, 4);
        grid.add(new Label("Semester:"), 0, 5); grid.add(semesterCB, 1, 5);
        grid.add(new Label("Kelas:"), 0, 6); grid.add(kelasTF, 1, 6);
        grid.add(new Label("Dosen:"), 0, 7); grid.add(dosenCB, 1, 7);
        grid.add(new Label("Ruang:"), 0, 8); grid.add(ruangCB, 1, 8);
        grid.add(new Label("Fakultas:"), 0, 9); grid.add(fakultasCB, 1, 9);
        grid.add(new Label("Prodi:"), 0, 10); grid.add(prodiCB, 1, 10);

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
