package LecturerSystem.service;

import LecturerSystem.model.GiangVien;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class QuanLyGiangVien {
    private ArrayList<GiangVien> danhSachGiangVien;

    public QuanLyGiangVien() {
        danhSachGiangVien = new ArrayList<GiangVien>();
    }

    public boolean addGiangVien(GiangVien giangVien) {
        if (giangVien == null || isDuplicateMa(giangVien.getMaGiangVien())) {
            return false;
        }
        danhSachGiangVien.add(giangVien);
        return true;
    }

    public boolean updateGiangVien(String maGiangVien, GiangVien giangVienMoi) {
        int index = findIndexByMa(maGiangVien);
        if (index == -1 || giangVienMoi == null) {
            return false;
        }
        danhSachGiangVien.set(index, giangVienMoi);
        return true;
    }

    public boolean deleteGiangVien(String maGiangVien) {
        int index = findIndexByMa(maGiangVien);
        if (index == -1) {
            return false;
        }
        danhSachGiangVien.remove(index);
        return true;
    }

    public ArrayList<GiangVien> getAll() {
        return danhSachGiangVien;
    }

    public void setDanhSachGiangVien(ArrayList<GiangVien> danhSachGiangVien) {
        if (danhSachGiangVien == null) {
            this.danhSachGiangVien = new ArrayList<GiangVien>();
        } else {
            this.danhSachGiangVien = danhSachGiangVien;
        }
    }

    public ArrayList<GiangVien> searchGiangVien(String keyword) {
        ArrayList<GiangVien> ketQua = new ArrayList<GiangVien>();
        String tuKhoa = keyword == null ? "" : keyword.trim().toLowerCase();
        // Tìm theo các thông tin thường dùng trên màn hình quản lý.
        for (GiangVien gv : danhSachGiangVien) {
            if (gv.getMaGiangVien().toLowerCase().contains(tuKhoa)
                    || gv.getHoTen().toLowerCase().contains(tuKhoa)
                    || gv.getKhoa().toLowerCase().contains(tuKhoa)
                    || gv.getMonDay().toLowerCase().contains(tuKhoa)) {
                ketQua.add(gv);
            }
        }
        return ketQua;
    }

    public void sortByName() {
        Collections.sort(danhSachGiangVien, new Comparator<GiangVien>() {
            @Override
            public int compare(GiangVien gv1, GiangVien gv2) {
                return gv1.getHoTen().compareToIgnoreCase(gv2.getHoTen());
            }
        });
    }

    public void sortBySalary() {
        Collections.sort(danhSachGiangVien, new Comparator<GiangVien>() {
            @Override
            public int compare(GiangVien gv1, GiangVien gv2) {
                return Double.compare(gv2.tinhLuong(), gv1.tinhLuong());
            }
        });
    }

    public String thongKe() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tổng giảng viên: ").append(danhSachGiangVien.size()).append("\n");
        builder.append("Tổng quỹ lương: ").append(tinhTongLuong()).append("\n");
        builder.append("Thống kê theo khoa:\n");
        // Lấy danh sách khoa duy nhất rồi đếm số giảng viên từng khoa.
        for (String khoa : getDanhSachKhoa()) {
            builder.append("- ").append(khoa).append(": ")
                    .append(countByKhoa(khoa)).append(" giảng viên\n");
        }
        return builder.toString();
    }

    public double tinhTongLuong() {
        double tong = 0;
        for (GiangVien gv : danhSachGiangVien) {
            tong += gv.tinhLuong();
        }
        return tong;
    }

    public ArrayList<String> getDanhSachKhoa() {
        ArrayList<String> danhSachKhoa = new ArrayList<String>();
        for (GiangVien gv : danhSachGiangVien) {
            if (!containsIgnoreCase(danhSachKhoa, gv.getKhoa())) {
                danhSachKhoa.add(gv.getKhoa());
            }
        }
        return danhSachKhoa;
    }

    public int countByKhoa(String khoa) {
        int dem = 0;
        for (GiangVien gv : danhSachGiangVien) {
            if (gv.getKhoa().equalsIgnoreCase(khoa)) {
                dem++;
            }
        }
        return dem;
    }

    public boolean isDuplicateMa(String maGiangVien) {
        return findIndexByMa(maGiangVien) != -1;
    }

    private int findIndexByMa(String maGiangVien) {
        if (maGiangVien == null) {
            return -1;
        }
        for (int i = 0; i < danhSachGiangVien.size(); i++) {
            if (maGiangVien.equalsIgnoreCase(danhSachGiangVien.get(i).getMaGiangVien())) {
                return i;
            }
        }
        return -1;
    }

    private boolean containsIgnoreCase(ArrayList<String> list, String value) {
        for (String item : list) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
