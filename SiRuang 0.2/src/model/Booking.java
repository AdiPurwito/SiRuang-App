package model;

public class Booking {
    private String pemesan;
    private String ruang;
    private String hari;
    private String jam;
    private String status;
    private String tanggal; // Tambahan baru

    // Konstruktor untuk data lama (tanpa tanggal)
    public Booking(String pemesan, String ruang, String hari, String jam, String status) {
        this.pemesan = pemesan;
        this.ruang = ruang;
        this.hari = hari;
        this.jam = jam;
        this.status = status;
        this.tanggal = java.time.LocalDate.now().toString(); // default sekarang
    }

    // Konstruktor untuk data baru (dengan tanggal eksplisit)
    public Booking(String pemesan, String ruang, String hari, String jam, String status, String tanggal) {
        this.pemesan = pemesan;
        this.ruang = ruang;
        this.hari = hari;
        this.jam = jam;
        this.status = status;
        this.tanggal = tanggal;
    }

    // Getter-setter lengkap
    public String getPemesan() { return pemesan; }
    public String getRuang() { return ruang; }
    public String getHari() { return hari; }
    public String getJam() { return jam; }
    public String getStatus() { return status; }
    public String getTanggal() { return tanggal; }

    public void setRuang(String ruang) { this.ruang = ruang; }
    public void setHari(String hari) { this.hari = hari; }
    public void setJam(String jam) { this.jam = jam; }
    public void setStatus(String status) { this.status = status; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
}
