package LecturerSystem.service;

import LecturerSystem.model.MonHoc;
import java.util.ArrayList;

public class QuanLyMonHoc {
    private ArrayList<MonHoc> danhSachMonHoc;

    public QuanLyMonHoc() {
        danhSachMonHoc = new ArrayList<MonHoc>();
    }

    public boolean addMonHoc(MonHoc monHoc) {
        if (monHoc == null || isDuplicateMa(monHoc.getMaMon())) {
            return false;
        }
        danhSachMonHoc.add(monHoc);
        return true;
    }

    public boolean updateMonHoc(String maMon, MonHoc monHocMoi) {
        int index = findIndexByMa(maMon);
        if (index == -1 || monHocMoi == null) {
            return false;
        }
        danhSachMonHoc.set(index, monHocMoi);
        return true;
    }

    public boolean deleteMonHoc(String maMon) {
        int index = findIndexByMa(maMon);
        if (index == -1) {
            return false;
        }
        danhSachMonHoc.remove(index);
        return true;
    }

    public ArrayList<MonHoc> getAll() {
        return danhSachMonHoc;
    }

    public void setDanhSachMonHoc(ArrayList<MonHoc> danhSachMonHoc) {
        if (danhSachMonHoc == null) {
            this.danhSachMonHoc = new ArrayList<MonHoc>();
        } else {
            this.danhSachMonHoc = danhSachMonHoc;
        }
    }

    public ArrayList<MonHoc> searchMonHoc(String keyword) {
        ArrayList<MonHoc> ketQua = new ArrayList<MonHoc>();
        String tuKhoa = keyword == null ? "" : keyword.trim().toLowerCase();
        for (MonHoc monHoc : danhSachMonHoc) {
            if (monHoc.getMaMon().toLowerCase().contains(tuKhoa)
                    || monHoc.getTenMon().toLowerCase().contains(tuKhoa)) {
                ketQua.add(monHoc);
            }
        }
        return ketQua;
    }

    public boolean isDuplicateMa(String maMon) {
        return findIndexByMa(maMon) != -1;
    }

    private int findIndexByMa(String maMon) {
        if (maMon == null) {
            return -1;
        }
        for (int i = 0; i < danhSachMonHoc.size(); i++) {
            if (maMon.equalsIgnoreCase(danhSachMonHoc.get(i).getMaMon())) {
                return i;
            }
        }
        return -1;
    }
}
