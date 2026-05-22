package LecturerSystem.model;

import java.io.Serializable;

public class GiangVien implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double LUONG_CO_SO = 1800000;

    private String maGV;
    private String hoTen;
    private String ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String soDienThoai;
    private String email;
    private String maKhoa;
    private String tenKhoa;
    private String maMonHoc;
    private String tenMonHoc;
    private String hocVi;
    private double heSoLuong;
    private String trangThai;

    public GiangVien() {
    }

    public GiangVien(String maGV, String hoTen, String ngaySinh, String gioiTinh, String diaChi,
            String soDienThoai, String email, String maKhoa, String tenKhoa, String maMonHoc,
            String tenMonHoc, String hocVi, double heSoLuong, String trangThai) {
        this.maGV = maGV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.hocVi = hocVi;
        this.heSoLuong = heSoLuong;
        this.trangThai = trangThai;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
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

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public String getHocVi() {
        return hocVi;
    }

    public void setHocVi(String hocVi) {
        this.hocVi = hocVi;
    }

    public double getHeSoLuong() {
        return heSoLuong;
    }

    public void setHeSoLuong(double heSoLuong) {
        this.heSoLuong = heSoLuong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double tinhLuong() {
        return heSoLuong * LUONG_CO_SO + getPhuCapHocVi();
    }

    public double getPhuCapHocVi() {
        if ("Cử nhân".equalsIgnoreCase(hocVi)) {
            return 500000;
        }
        if ("Thạc sĩ".equalsIgnoreCase(hocVi)) {
            return 1000000;
        }
        if ("Tiến sĩ".equalsIgnoreCase(hocVi)) {
            return 2000000;
        }
        if ("PGS".equalsIgnoreCase(hocVi)) {
            return 3000000;
        }
        if ("GS".equalsIgnoreCase(hocVi)) {
            return 5000000;
        }
        return 0;
    }

    @Override
    public String toString() {
        return maGV + " - " + hoTen + " - " + tenKhoa;
    }
}
