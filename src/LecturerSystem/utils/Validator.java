package LecturerSystem.utils;

import LecturerSystem.service.QuanLyGiangVien;

public class Validator {
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isMaGiangVienTrung(String maGiangVien, QuanLyGiangVien quanLy) {
        return quanLy != null && quanLy.isDuplicateMa(maGiangVien);
    }

    public static boolean isSoDienThoaiHopLe(String soDienThoai) {
        if (isEmpty(soDienThoai)) {
            return false;
        }
        return soDienThoai.matches("0\\d{9}");
    }

    public static boolean isLuongHopLe(double value) {
        return value >= 0;
    }

    public static boolean isSoNguyenKhongAm(int value) {
        return value >= 0;
    }

    public static double parseDouble(String value) throws NumberFormatException {
        if (isEmpty(value)) {
            return 0;
        }
        return Double.parseDouble(value.trim());
    }

    public static int parseInt(String value) throws NumberFormatException {
        if (isEmpty(value)) {
            return 0;
        }
        return Integer.parseInt(value.trim());
    }
}
