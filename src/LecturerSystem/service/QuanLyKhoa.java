package LecturerSystem.service;

import LecturerSystem.model.Khoa;
import java.util.ArrayList;

public class QuanLyKhoa {
    private ArrayList<Khoa> danhSachKhoa;

    public QuanLyKhoa() {
        danhSachKhoa = new ArrayList<Khoa>();
    }

    public boolean addKhoa(Khoa khoa) {
        if (khoa == null || isDuplicateMa(khoa.getMaKhoa())) {
            return false;
        }
        danhSachKhoa.add(khoa);
        return true;
    }

    public boolean updateKhoa(String maKhoa, Khoa khoaMoi) {
        int index = findIndexByMa(maKhoa);
        if (index == -1 || khoaMoi == null) {
            return false;
        }
        danhSachKhoa.set(index, khoaMoi);
        return true;
    }

    public boolean deleteKhoa(String maKhoa) {
        int index = findIndexByMa(maKhoa);
        if (index == -1) {
            return false;
        }
        danhSachKhoa.remove(index);
        return true;
    }

    public ArrayList<Khoa> getAll() {
        return danhSachKhoa;
    }

    public void setDanhSachKhoa(ArrayList<Khoa> danhSachKhoa) {
        if (danhSachKhoa == null) {
            this.danhSachKhoa = new ArrayList<Khoa>();
        } else {
            this.danhSachKhoa = danhSachKhoa;
        }
    }

    public ArrayList<Khoa> searchKhoa(String keyword) {
        ArrayList<Khoa> ketQua = new ArrayList<Khoa>();
        String tuKhoa = keyword == null ? "" : keyword.trim().toLowerCase();
        for (Khoa khoa : danhSachKhoa) {
            if (khoa.getMaKhoa().toLowerCase().contains(tuKhoa)
                    || khoa.getTenKhoa().toLowerCase().contains(tuKhoa)) {
                ketQua.add(khoa);
            }
        }
        return ketQua;
    }

    public boolean isDuplicateMa(String maKhoa) {
        return findIndexByMa(maKhoa) != -1;
    }

    private int findIndexByMa(String maKhoa) {
        if (maKhoa == null) {
            return -1;
        }
        for (int i = 0; i < danhSachKhoa.size(); i++) {
            if (maKhoa.equalsIgnoreCase(danhSachKhoa.get(i).getMaKhoa())) {
                return i;
            }
        }
        return -1;
    }
}
