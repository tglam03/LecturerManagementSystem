package LecturerSystem.model;

public class GiangVienThinhGiang extends GiangVien {
    private static final long serialVersionUID = 1L;

    private int soGioDay;
    private double tienMoiGio;

    public GiangVienThinhGiang() {
    }

    public GiangVienThinhGiang(String maGiangVien, String hoTen, String ngaySinh, String diaChi,
            String soDienThoai, String khoa, String monDay, int soGioDay, double tienMoiGio) {
        super(maGiangVien, hoTen, ngaySinh, diaChi, soDienThoai, khoa, monDay);
        this.soGioDay = soGioDay;
        this.tienMoiGio = tienMoiGio;
    }

    public int getSoGioDay() {
        return soGioDay;
    }

    public void setSoGioDay(int soGioDay) {
        this.soGioDay = soGioDay;
    }

    public double getTienMoiGio() {
        return tienMoiGio;
    }

    public void setTienMoiGio(double tienMoiGio) {
        this.tienMoiGio = tienMoiGio;
    }

    @Override
    public double tinhLuong() {
        return soGioDay * tienMoiGio;
    }

    @Override
    public String getLoaiGiangVien() {
        return "Thỉnh giảng";
    }

    @Override
    public String toString() {
        return super.toString() + " - " + soGioDay + " giờ - " + tinhLuong();
    }
}
