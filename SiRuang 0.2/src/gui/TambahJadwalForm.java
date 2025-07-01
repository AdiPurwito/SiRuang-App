package gui;

import database.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Jadwal;
import model.Ruang;
import util.SesiUtil;

import java.util.List;

public class TambahJadwalForm extends VBox {
    public TambahJadwalForm(Stage stage) {
        setSpacing(12);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Tambah Jadwal Baru");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<String> hari = new ComboBox<>();
        hari.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat");

        ComboBox<Integer> sesi = new ComboBox<>();
        sesi.getItems().addAll(SesiUtil.getPilihanSesiMulai());

        ComboBox<String> sks = new ComboBox<>();
        sks.getItems().addAll("2", "3", "4", "6");

        Label waktuLabel = new Label("Waktu: -");

        // ✅ Autocomplete Mata Kuliah
        // MATA KULIAH
        ComboBox<String> matkul = new ComboBox<>();
        matkul.setEditable(true);
        matkul.setPromptText("Mata Kuliah");

        List<String> matkulList = MatkulDB.loadMatkul().stream()
                .distinct()
                .toList();

        matkul.getItems().addAll(matkulList);

        matkul.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            matkul.hide();
            if (newText.isEmpty()) {
                matkul.getItems().setAll(matkulList);
            } else {
                matkul.getItems().setAll(
                        matkulList.stream()
                                .filter(n -> n.toLowerCase().contains(newText.toLowerCase()))
                                .toList()
                );
            }
            matkul.show();
        });


        ComboBox<String> semester = new ComboBox<>();
        semester.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8");

        // ✅ ComboBox kelas A-N
        ComboBox<String> kelas = new ComboBox<>();
        for (char c = 'A'; c <= 'N'; c++) {
            kelas.getItems().add(String.valueOf(c));
        }
        kelas.setPromptText("Pilih Kelas");

        // ✅ Autocomplete Dosen
        // DOSEN
        ComboBox<String> dosen = new ComboBox<>();
        dosen.setEditable(true);
        dosen.setPromptText("Nama Dosen");

        List<String> dosenList = DosenDB.loadDosen().stream()
                .distinct()
                .toList();

        dosen.getItems().addAll(dosenList);

        dosen.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            dosen.hide();
            if (newText.isEmpty()) {
                dosen.getItems().setAll(dosenList);
            } else {
                dosen.getItems().setAll(
                        dosenList.stream()
                                .filter(n -> n.toLowerCase().contains(newText.toLowerCase()))
                                .toList()
                );
            }
            dosen.show();
        });


        // ✅ Autocomplete Ruang
        ComboBox<String> ruang = new ComboBox<>();
        ruang.setEditable(true);
        ruang.setPromptText("Ruang (cth: R101)");

        List<String> ruangList = RuangDB.loadRuang().stream()
                .map(r -> r.getNama())
                .distinct()
                .toList();

        ruang.getItems().addAll(ruangList);

        ruang.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            ruang.hide();
            if (newText.isEmpty()) {
                ruang.getItems().setAll(ruangList);
            } else {
                ruang.getItems().setAll(
                        ruangList.stream()
                                .filter(n -> n.toLowerCase().contains(newText.toLowerCase()))
                                .toList()
                );
            }
            ruang.show();
        });


        ComboBox<String> fakultas = new ComboBox<>();
        fakultas.getItems().addAll(FakultasDB.load());
        fakultas.setEditable(true);

        ComboBox<String> prodi = new ComboBox<>();
        prodi.getItems().addAll(ProdiDB.load());
        prodi.setEditable(true);

        sesi.setOnAction(e -> updateWaktuLabel(waktuLabel, sesi, sks));
        sks.setOnAction(e -> updateWaktuLabel(waktuLabel, sesi, sks));

        Button simpan = new Button("Simpan");
        Button kembali = new Button("Kembali");

        simpan.setOnAction(e -> {
            if (hari.getValue() == null || sesi.getValue() == null || sks.getValue() == null ||
                    matkul.getValue().isEmpty() || semester.getValue() == null || kelas.getValue() == null ||
                    dosen.getValue().isEmpty() || ruang.getEditor().getText().isEmpty() ||
                    fakultas.getValue() == null || prodi.getValue() == null) {
                alert("Semua field harus diisi.");
                return;
            }

            int sesiMulai = sesi.getValue();
            int sksInt = Integer.parseInt(sks.getValue());

            if (SesiUtil.sesiMelebihiBatas(sesiMulai, sksInt)) {
                alert("Sesi melebihi sesi maksimal (14).");
                return;
            }

            String dosenVal = dosen.getEditor().getText().trim();
            if (!dosenVal.matches("^[\\p{L} .,'-]+$")) {
                alert("Nama dosen tidak valid.");
                return;
            }

            String waktu = SesiUtil.getRentangWaktu(sesiMulai, sksInt);
            String ruangVal = ruang.getEditor().getText().trim();

            Jadwal baru = new Jadwal(hari.getValue(), waktu, matkul.getValue(), semester.getValue(),
                    sks.getValue(), kelas.getValue(), dosenVal, ruangVal,
                    fakultas.getValue(), prodi.getValue());

            if (JadwalDB.isJadwalBentrok(baru)) {
                alert("Jadwal bentrok dengan jadwal lain di ruang yang sama.");
                return;
            }

            JadwalDB.saveJadwal(baru);
            RuangDB.kurangiSesi(ruangVal);
            DosenDB.saveIfNew(dosenVal);
            MatkulDB.saveIfNew(matkul.getValue());
            FakultasDB.saveIfNew(fakultas.getValue());
            ProdiDB.saveIfNew(prodi.getValue());

            alert("Jadwal berhasil ditambahkan.");
            stage.setScene(new Scene(new KelolaJadwalView(stage), 800, 600));
        });

        kembali.setOnAction(e -> stage.setScene(new Scene(new KelolaJadwalView(stage), 800, 600)));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setMaxWidth(Double.MAX_VALUE);

        form.add(new Label("Hari:"), 0, 0); form.add(hari, 1, 0);
        form.add(new Label("Sesi Mulai:"), 0, 1); form.add(sesi, 1, 1);
        form.add(new Label("SKS:"), 0, 2); form.add(sks, 1, 2);
        form.add(new Label("Waktu:"), 0, 3); form.add(waktuLabel, 1, 3);
        form.add(new Label("Mata Kuliah:"), 0, 4); form.add(matkul, 1, 4);
        form.add(new Label("Semester:"), 0, 5); form.add(semester, 1, 5);
        form.add(new Label("Kelas:"), 0, 6); form.add(kelas, 1, 6);
        form.add(new Label("Dosen:"), 0, 7); form.add(dosen, 1, 7);
        form.add(new Label("Ruang:"), 0, 8); form.add(ruang, 1, 8);
        form.add(new Label("Fakultas:"), 0, 9); form.add(fakultas, 1, 9);
        form.add(new Label("Prodi:"), 0, 10); form.add(prodi, 1, 10);

        HBox buttons = new HBox(10, simpan, kembali);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(title, form, buttons);
    }

    private void updateWaktuLabel(Label label, ComboBox<Integer> sesi, ComboBox<String> sks) {
        if (sesi.getValue() != null && sks.getValue() != null) {
            label.setText("Waktu: " + SesiUtil.getRentangWaktu(sesi.getValue(), Integer.parseInt(sks.getValue())));
        }
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.setContentText(msg);
        a.showAndWait();
    }
}
