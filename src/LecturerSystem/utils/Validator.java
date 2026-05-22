package LecturerSystem.utils;

public class Validator {
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isSoDienThoaiHopLe(String soDienThoai) {
        if (isEmpty(soDienThoai)) {
            return false;
        }
        return soDienThoai.matches("0\\d{9}");
    }

    public static boolean isEmailHopLe(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isHeSoLuongHopLe(double heSoLuong) {
        return heSoLuong > 0;
    }

    public static boolean isSoTinChiHopLe(int soTinChi) {
        return soTinChi > 0;
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
