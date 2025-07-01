package model;

public class Jadwal {
    private String hari, jam, matkul, semester, sks, kelas, dosen, ruang;
    private String fakultas, prodi;

    public Jadwal(String hari, String jam, String matkul, String semester, String sks, String kelas, String dosen, String ruang, String fakultas, String prodi) {
        this.hari = hari;
        this.jam = jam;
        this.matkul = matkul;
        this.semester = semester;
        this.sks = sks;
        this.kelas = kelas;
        this.dosen = dosen;
        this.ruang = ruang;
        this.fakultas = fakultas;
        this.prodi = prodi;
    }

    public Jadwal(String value, String text, String value1, String value2, String value3, String text1, String value4, String text2) {
    }

    public Jadwal(String matkul, String dosen, String ruang, String hari, String jam) {
    }

    // Getter & Setter
    public String getHari() { return hari; }
    public String getJam() { return jam; }
    public String getMatkul() { return matkul; }
    public String getSemester() { return semester; }
    public String getSks() { return sks; }
    public String getKelas() { return kelas; }
    public String getDosen() { return dosen; }
    public String getRuang() { return ruang; }
    public String getFakultas() { return fakultas; }
    public String getProdi() { return prodi; }
}
