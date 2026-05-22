package LecturerSystem.model;

public class GiangVienCoHuu extends GiangVien {
    private static final long serialVersionUID = 1L;

    private double heSoLuong;
    private String hocVi;

    public GiangVienCoHuu() {
    }

    public GiangVienCoHuu(String maGiangVien, String hoTen, String ngaySinh, String diaChi,
            String soDienThoai, String khoa, String monDay, double heSoLuong, String hocVi) {
        super(maGiangVien, hoTen, ngaySinh, diaChi, soDienThoai, khoa, monDay);
        this.heSoLuong = heSoLuong;
        this.hocVi = hocVi;
    }

    public double getHeSoLuong() {
        return heSoLuong;
    }

    public void setHeSoLuong(double heSoLuong) {
        this.heSoLuong = heSoLuong;
    }

    public String getHocVi() {
        return hocVi;
    }

    public void setHocVi(String hocVi) {
        this.hocVi = hocVi;
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
    public double tinhLuong() {
        return heSoLuong * 1800000 + getPhuCapHocVi();
    }

    @Override
    public String getLoaiGiangVien() {
        return "Cơ hữu";
    }

    @Override
    public String toString() {
        return super.toString() + " - " + hocVi + " - " + tinhLuong();
    }
}
