package LecturerSystem.model;

import LecturerSystem.interfaces.TinhLuong;
import java.io.Serializable;

public abstract class GiangVien implements TinhLuong, Serializable {
    private static final long serialVersionUID = 1L;

    private String maGiangVien;
    private String hoTen;
    private String ngaySinh;
    private String diaChi;
    private String soDienThoai;
    private String khoa;
    private String monDay;

    public GiangVien() {
    }

    public GiangVien(String maGiangVien, String hoTen, String ngaySinh, String diaChi,
            String soDienThoai, String khoa, String monDay) {
        this.maGiangVien = maGiangVien;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.khoa = khoa;
        this.monDay = monDay;
    }

    public String getMaGiangVien() {
        return maGiangVien;
    }

    public void setMaGiangVien(String maGiangVien) {
        this.maGiangVien = maGiangVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getMonDay() {
        return monDay;
    }

    public void setMonDay(String monDay) {
        this.monDay = monDay;
    }

    public abstract String getLoaiGiangVien();

    @Override
    public String toString() {
        return maGiangVien + " - " + hoTen + " - " + khoa;
    }
}
