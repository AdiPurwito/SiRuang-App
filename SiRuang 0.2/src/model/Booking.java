package model;

public class Booking {
    private String pemesan, ruang, hari, jam, status;

    public Booking(String pemesan, String ruang, String hari, String jam, String status) {
        this.pemesan = pemesan;
        this.ruang = ruang;
        this.hari = hari;
        this.jam = jam;
        this.status = status;
    }

    public String getPemesan() { return pemesan; }
    public String getRuang() { return ruang; }
    public String getHari() { return hari; }
    public String getJam() { return jam; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}
