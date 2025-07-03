package model;

import java.util.Objects;

public class Ruang {
    private String nama, gedung;
    private int kapasitas, jumlahSesi;

    public Ruang(String nama, String gedung, int kapasitas, int jumlahSesi) {
        this.nama = nama;
        this.gedung = gedung;
        this.kapasitas = kapasitas;
        this.jumlahSesi = jumlahSesi;
    }

    public String getNama() { return nama; }
    public String getGedung() { return gedung; }
    public int getKapasitas() { return kapasitas; }
    public int getJumlahSesi() { return jumlahSesi; }

    public void setJumlahSesi(int jumlahSesi) {
        this.jumlahSesi = jumlahSesi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruang ruang = (Ruang) o;
        return nama.equalsIgnoreCase(ruang.nama) &&
                gedung.equalsIgnoreCase(ruang.gedung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nama.toLowerCase(), gedung.toLowerCase());
    }


}
