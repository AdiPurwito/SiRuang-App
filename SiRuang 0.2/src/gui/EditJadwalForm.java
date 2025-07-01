package gui;

import database.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Jadwal;
import util.SesiUtil;

public class EditJadwalForm extends VBox {
    public EditJadwalForm(Stage stage, Jadwal lama) {
        setSpacing(10);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Edit Jadwal");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<String> hari = new ComboBox<>();
        hari.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat");
        hari.setValue(lama.getHari());

        int[] sesiRange = SesiUtil.getRangeSesi(lama.getJam());
        int sesiAwalLama = sesiRange != null ? sesiRange[0] : 1;

        ComboBox<Integer> sesiMulai = new ComboBox<>();
        sesiMulai.getItems().addAll(SesiUtil.getPilihanSesiMulai());
        sesiMulai.setValue(sesiAwalLama);

        ComboBox<String> sks = new ComboBox<>();
        sks.getItems().addAll("2", "3", "4", "6");
        sks.setValue(lama.getSks());

        Label labelWaktu = new Label("Waktu: " + lama.getJam());

        Button lihatSesi = new Button("? Daftar Sesi");
        lihatSesi.setOnAction(e -> {
            Alert sesiDialog = new Alert(Alert.AlertType.INFORMATION);
            sesiDialog.setTitle("Daftar Sesi Kuliah");
            sesiDialog.setHeaderText("Keterangan Waktu Tiap Sesi:");
            sesiDialog.setContentText(SesiUtil.getDaftarSesi());
            sesiDialog.showAndWait();
        });

        ComboBox<String> matkulAuto = new ComboBox<>();
        matkulAuto.getItems().addAll(MatkulDB.loadMatkul());
        matkulAuto.setEditable(true);
        matkulAuto.setValue(lama.getMatkul());

        ComboBox<String> semester = new ComboBox<>();
        semester.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8");
        semester.setValue(lama.getSemester());

        TextField kelas = new TextField(lama.getKelas());

        ComboBox<String> dosenAuto = new ComboBox<>();
        dosenAuto.setEditable(true);
        dosenAuto.getItems().addAll(DosenDB.loadDosen());
        dosenAuto.setValue(lama.getDosen());

        TextField ruang = new TextField(lama.getRuang());

        ComboBox<String> fakultasAuto = new ComboBox<>();
        fakultasAuto.setEditable(true);
        fakultasAuto.getItems().addAll(FakultasDB.load());
        fakultasAuto.setValue(lama.getFakultas());

        ComboBox<String> prodiAuto = new ComboBox<>();
        prodiAuto.setEditable(true);
        prodiAuto.getItems().addAll(ProdiDB.load());
        prodiAuto.setValue(lama.getProdi());

        Button simpan = new Button("Simpan");
        Button kembali = new Button("Kembali");

        sesiMulai.setOnAction(e -> updateWaktu(labelWaktu, sesiMulai, sks));
        sks.setOnAction(e -> updateWaktu(labelWaktu, sesiMulai, sks));

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);
        form.add(new Label("Hari:"), 0, 0); form.add(hari, 1, 0);
        form.add(new Label("Sesi Mulai:"), 0, 1); form.add(sesiMulai, 1, 1); form.add(lihatSesi, 2, 1);
        form.add(new Label("SKS:"), 0, 2); form.add(sks, 1, 2);
        form.add(new Label("Waktu:"), 0, 3); form.add(labelWaktu, 1, 3);
        form.add(new Label("Mata Kuliah:"), 0, 4); form.add(matkulAuto, 1, 4);
        form.add(new Label("Semester:"), 0, 5); form.add(semester, 1, 5);
        form.add(new Label("Kelas:"), 0, 6); form.add(kelas, 1, 6);
        form.add(new Label("Dosen:"), 0, 7); form.add(dosenAuto, 1, 7);
        form.add(new Label("Ruang:"), 0, 8); form.add(ruang, 1, 8);
        form.add(new Label("Fakultas:"), 0, 9); form.add(fakultasAuto, 1, 9);
        form.add(new Label("Prodi:"), 0, 10); form.add(prodiAuto, 1, 10);

        HBox btnBar = new HBox(10, simpan, kembali);
        btnBar.setAlignment(Pos.CENTER_RIGHT);

        simpan.setOnAction(e -> {
            if (hari.getValue() == null || sesiMulai.getValue() == null || sks.getValue() == null ||
                    matkulAuto.getValue() == null || semester.getValue() == null || kelas.getText().isEmpty() ||
                    dosenAuto.getValue().isEmpty() || ruang.getText().isEmpty() ||
                    fakultasAuto.getValue() == null || prodiAuto.getValue() == null) {
                showAlert("Semua field harus diisi!");
                return;
            }

            int sesi = sesiMulai.getValue();
            int jumlahSks = Integer.parseInt(sks.getValue());

            if (SesiUtil.sesiMelebihiBatas(sesi, jumlahSks)) {
                showAlert("Sesi melebihi batas maksimum sesi ke-14.");
                return;
            }

            String waktuGabung = SesiUtil.getRentangWaktu(sesi, jumlahSks);

            Jadwal baru = new Jadwal(
                    hari.getValue(), waktuGabung, matkulAuto.getValue(), semester.getValue(),
                    sks.getValue(), kelas.getText(), dosenAuto.getValue(), ruang.getText(),
                    fakultasAuto.getValue(), prodiAuto.getValue()
            );

            if (JadwalDB.isJadwalBentrok(baru) && !JadwalDB.isSame(lama, baru)) {
                showAlert("Jadwal bentrok dengan jadwal lain!");
                return;
            }

            JadwalDB.updateJadwal(lama, baru);
            DosenDB.saveIfNew(dosenAuto.getValue());
            MatkulDB.saveIfNew(matkulAuto.getValue());
            FakultasDB.saveIfNew(fakultasAuto.getValue());
            ProdiDB.saveIfNew(prodiAuto.getValue());

            showAlert("Jadwal berhasil diperbarui!");
            stage.setScene(new Scene(new KelolaJadwalView(stage), 800, 600));
        });

        kembali.setOnAction(e -> stage.setScene(new Scene(new KelolaJadwalView(stage), 800, 600)));

        getChildren().addAll(title, form, btnBar);
    }

    private void updateWaktu(Label label, ComboBox<Integer> sesiMulai, ComboBox<String> sks) {
        if (sesiMulai.getValue() != null && sks.getValue() != null) {
            int sesi = sesiMulai.getValue();
            int jumlahSks = Integer.parseInt(sks.getValue());
            label.setText("Waktu: " + SesiUtil.getRentangWaktu(sesi, jumlahSks));
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }
}
