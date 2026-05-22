package LecturerSystem.service;

import LecturerSystem.model.GiangVien;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuanLyGiangVien {
    private ArrayList<GiangVien> danhSachGiangVien;

    public QuanLyGiangVien() {
        danhSachGiangVien = new ArrayList<GiangVien>();
    }

    public boolean addGiangVien(GiangVien giangVien) {
        if (giangVien == null || isDuplicateMa(giangVien.getMaGV())) {
            return false;
        }
        danhSachGiangVien.add(giangVien);
        return true;
    }

    public boolean updateGiangVien(String maGV, GiangVien giangVienMoi) {
        int index = findIndexByMa(maGV);
        if (index == -1 || giangVienMoi == null) {
            return false;
        }
        danhSachGiangVien.set(index, giangVienMoi);
        return true;
    }

    public boolean deleteGiangVien(String maGV) {
        int index = findIndexByMa(maGV);
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
        for (GiangVien gv : danhSachGiangVien) {
            if (contains(gv.getMaGV(), tuKhoa)
                    || contains(gv.getHoTen(), tuKhoa)
                    || contains(gv.getTenKhoa(), tuKhoa)
                    || contains(gv.getTenMonHoc(), tuKhoa)
                    || contains(gv.getHocVi(), tuKhoa)
                    || contains(gv.getEmail(), tuKhoa)) {
                ketQua.add(gv);
            }
        }
        return ketQua;
    }

    public ArrayList<GiangVien> filterByKhoa(String maKhoa) {
        ArrayList<GiangVien> ketQua = new ArrayList<GiangVien>();
        if (maKhoa == null || maKhoa.trim().isEmpty() || "Tất cả".equals(maKhoa)) {
            ketQua.addAll(danhSachGiangVien);
            return ketQua;
        }
        for (GiangVien gv : danhSachGiangVien) {
            if (maKhoa.equalsIgnoreCase(gv.getMaKhoa())) {
                ketQua.add(gv);
            }
        }
        return ketQua;
    }

    public ArrayList<GiangVien> filterByHocVi(String hocVi) {
        ArrayList<GiangVien> ketQua = new ArrayList<GiangVien>();
        if (hocVi == null || hocVi.trim().isEmpty() || "Tất cả".equals(hocVi)) {
            ketQua.addAll(danhSachGiangVien);
            return ketQua;
        }
        for (GiangVien gv : danhSachGiangVien) {
            if (hocVi.equalsIgnoreCase(gv.getHocVi())) {
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

    public double tinhTongLuong() {
        double tong = 0;
        for (GiangVien gv : danhSachGiangVien) {
            tong += gv.tinhLuong();
        }
        return tong;
    }

    public Map<String, Integer> thongKeTheoKhoa() {
        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        for (GiangVien gv : danhSachGiangVien) {
            String key = emptyToUnknown(gv.getTenKhoa());
            Integer count = data.get(key);
            data.put(key, count == null ? 1 : count + 1);
        }
        return data;
    }

    public Map<String, Integer> thongKeTheoHocVi() {
        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        for (GiangVien gv : danhSachGiangVien) {
            String key = emptyToUnknown(gv.getHocVi());
            Integer count = data.get(key);
            data.put(key, count == null ? 1 : count + 1);
        }
        return data;
    }

    public String thongKe() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tổng giảng viên: ").append(danhSachGiangVien.size()).append("\n");
        builder.append("Tổng quỹ lương: ").append(tinhTongLuong()).append("\n");
        builder.append("Thống kê theo khoa:\n");
        for (Map.Entry<String, Integer> entry : thongKeTheoKhoa().entrySet()) {
            builder.append("- ").append(entry.getKey()).append(": ")
                    .append(entry.getValue()).append(" giảng viên\n");
        }
        return builder.toString();
    }

    public boolean isDuplicateMa(String maGV) {
        return findIndexByMa(maGV) != -1;
    }

    private int findIndexByMa(String maGV) {
        if (maGV == null) {
            return -1;
        }
        for (int i = 0; i < danhSachGiangVien.size(); i++) {
            if (maGV.equalsIgnoreCase(danhSachGiangVien.get(i).getMaGV())) {
                return i;
            }
        }
        return -1;
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private String emptyToUnknown(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Chưa phân loại";
        }
        return value;
    }
}
