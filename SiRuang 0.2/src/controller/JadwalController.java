package controller;

import database.JadwalDB;
import database.RuangDB;
import model.Jadwal;
import util.AlertUtil;
import util.BentrokUtil;
import util.SesiUtil;

import java.util.List;

public class JadwalController {

    public static void tambahJadwal(Jadwal baru, Runnable onSuccess) {
        if (!isLengkap(baru)) {
            AlertUtil.error("Lengkapi semua isian.");
            return;
        }

        int[] sesiData = SesiUtil.ekstrakSesiDanSKS(baru.getJam());
        int sesi = sesiData[0];
        int sks = sesiData[1];


        if (SesiUtil.sesiMelebihiBatas(sesi, sks)) {
            AlertUtil.error("Sesi melebihi batas maksimum (14).");
            return;
        }

        if (BentrokUtil.bentrokDenganJadwal(baru.getHari(), baru.getRuang(), baru.getJam())) {
            AlertUtil.error("Jadwal bentrok dengan jadwal lain!");
            return;
        }

        JadwalDB db = new JadwalDB(); // bisa disimpan di variabel kalau sering dipakai
        List<Jadwal> semua = db.load();
        semua.add(baru);
        RuangDB.kurangiSesi(baru.getRuang(), sks);
        db.saveAll(semua);
        AlertUtil.success("Jadwal berhasil ditambahkan.");
        onSuccess.run();

    }

    public static void updateJadwal(Jadwal lama, Jadwal baru, Runnable onSuccess) {
        List<Jadwal> semua = JadwalDB.loadJadwal();
        semua.removeIf(j -> JadwalDB.isSame(j, lama));

        if (BentrokUtil.bentrokDenganJadwal(baru.getHari(), baru.getRuang(), baru.getJam())) {
            AlertUtil.error("Jadwal bentrok dengan jadwal lain!");
            return;
        }

        semua.add(baru);

        int[] sesiLama = SesiUtil.ekstrakSesiDanSKS(lama.getJam());
        int[] sesiBaru = SesiUtil.ekstrakSesiDanSKS(baru.getJam());

        RuangDB.tambahSesi(lama.getRuang(), sesiLama[1]);
        RuangDB.kurangiSesi(baru.getRuang(), sesiBaru[1]);

        JadwalDB db = new JadwalDB();
        db.saveAll(semua);
        AlertUtil.success("Jadwal berhasil diperbarui.");
        onSuccess.run();
    }

    public static void hapusJadwal(Jadwal target, Runnable onSuccess) {
        JadwalDB.deleteJadwal(target);
        AlertUtil.success("Jadwal berhasil dihapus.");
        onSuccess.run();
    }

    private static boolean isLengkap(Jadwal j) {
        return j.getHari() != null && j.getJam() != null &&
                j.getMatkul() != null && !j.getMatkul().isEmpty() &&
                j.getSemester() != null &&
                j.getSks() != null &&
                j.getKelas() != null && !j.getKelas().isEmpty() &&
                j.getDosen() != null && !j.getDosen().isEmpty() &&
                j.getRuang() != null && !j.getRuang().isEmpty() &&
                j.getFakultas() != null &&
                j.getProdi() != null;
    }
}
