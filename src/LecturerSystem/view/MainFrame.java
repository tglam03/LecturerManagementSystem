package LecturerSystem.view;

import LecturerSystem.file.FileUtil;
import LecturerSystem.model.GiangVien;
import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.service.QuanLyKhoa;
import LecturerSystem.service.QuanLyMonHoc;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
    private final QuanLyGiangVien quanLyGiangVien;
    private final QuanLyKhoa quanLyKhoa;
    private final QuanLyMonHoc quanLyMonHoc;
    private GiangVienPanel giangVienPanel;

    public MainFrame() {
        this(new QuanLyGiangVien(), new QuanLyKhoa(), new QuanLyMonHoc());
        loadDataQuietly();
    }

    public MainFrame(QuanLyGiangVien quanLyGiangVien) {
        this(quanLyGiangVien, new QuanLyKhoa(), new QuanLyMonHoc());
        loadCatalogQuietly();
    }

    public MainFrame(QuanLyGiangVien quanLyGiangVien, QuanLyKhoa quanLyKhoa, QuanLyMonHoc quanLyMonHoc) {
        this.quanLyGiangVien = quanLyGiangVien == null ? new QuanLyGiangVien() : quanLyGiangVien;
        this.quanLyKhoa = quanLyKhoa == null ? new QuanLyKhoa() : quanLyKhoa;
        this.quanLyMonHoc = quanLyMonHoc == null ? new QuanLyMonHoc() : quanLyMonHoc;
        initComponents();
    }

    private void initComponents() {
        setTitle("Quản lý giảng viên");
        setSize(1180, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        giangVienPanel = new GiangVienPanel(quanLyGiangVien, quanLyKhoa, quanLyMonHoc, null);
        add(giangVienPanel, BorderLayout.CENTER);
    }

    private void loadDataQuietly() {
        try {
            quanLyGiangVien.setDanhSachGiangVien(FileUtil.readGiangVien());
        } catch (Exception ex) {
            quanLyGiangVien.setDanhSachGiangVien(new ArrayList<GiangVien>());
        }
        loadCatalogQuietly();
        if (giangVienPanel != null) {
            giangVienPanel.refreshData();
        }
    }

    private void loadCatalogQuietly() {
        try {
            quanLyKhoa.setDanhSachKhoa(FileUtil.readKhoa());
        } catch (Exception ex) {
            quanLyKhoa.setDanhSachKhoa(null);
        }
        try {
            quanLyMonHoc.setDanhSachMonHoc(FileUtil.readMonHoc());
        } catch (Exception ex) {
            quanLyMonHoc.setDanhSachMonHoc(null);
        }
        if (giangVienPanel != null) {
            giangVienPanel.refreshData();
        }
    }
}
