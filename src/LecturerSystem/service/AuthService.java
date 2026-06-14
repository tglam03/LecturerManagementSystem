package LecturerSystem.service;

import LecturerSystem.model.TaiKhoan;

public class AuthService {
    private final TaiKhoan taiKhoanMacDinh;

    public AuthService() {
        taiKhoanMacDinh = new TaiKhoan("admin", "Java@123");
    }

    public boolean login(String username, String password) {
        return kiemTraUsername(username) && kiemTraPassword(password);
    }

    public boolean kiemTraUsername(String username) {
        return taiKhoanMacDinh.getUsername().equals(username);
    }

    public boolean kiemTraPassword(String password) {
        return taiKhoanMacDinh.getPassword().equals(password);
    }
}
