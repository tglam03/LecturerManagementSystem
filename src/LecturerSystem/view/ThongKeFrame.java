package LecturerSystem.view;

import LecturerSystem.file.FileUtil;
import LecturerSystem.model.GiangVien;
import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.service.QuanLyKhoa;
import LecturerSystem.service.QuanLyMonHoc;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;

public class ThongKeFrame extends JFrame {
    private final QuanLyGiangVien quanLyGiangVien;
    private final QuanLyKhoa quanLyKhoa;
    private final QuanLyMonHoc quanLyMonHoc;
    private ThongKePanel thongKePanel;

    public ThongKeFrame() {
        this(new QuanLyGiangVien(), new QuanLyKhoa(), new QuanLyMonHoc());
        loadDataQuietly();
    }

    public ThongKeFrame(QuanLyGiangVien quanLyGiangVien) {
        this(quanLyGiangVien, new QuanLyKhoa(), new QuanLyMonHoc());
        loadCatalogQuietly();
    }

    public ThongKeFrame(QuanLyGiangVien quanLyGiangVien, QuanLyKhoa quanLyKhoa, QuanLyMonHoc quanLyMonHoc) {
        this.quanLyGiangVien = quanLyGiangVien == null ? new QuanLyGiangVien() : quanLyGiangVien;
        this.quanLyKhoa = quanLyKhoa == null ? new QuanLyKhoa() : quanLyKhoa;
        this.quanLyMonHoc = quanLyMonHoc == null ? new QuanLyMonHoc() : quanLyMonHoc;
        initComponents();
    }

    private void initComponents() {
        setTitle("Thống kê giảng viên");
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        thongKePanel = new ThongKePanel(quanLyGiangVien, quanLyKhoa, quanLyMonHoc);
        add(thongKePanel, BorderLayout.CENTER);
    }

    private void loadDataQuietly() {
        try {
            quanLyGiangVien.setDanhSachGiangVien(FileUtil.readGiangVien());
        } catch (Exception ex) {
            quanLyGiangVien.setDanhSachGiangVien(new ArrayList<GiangVien>());
        }
        loadCatalogQuietly();
        if (thongKePanel != null) {
            thongKePanel.refreshData();
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
        if (thongKePanel != null) {
            thongKePanel.refreshData();
        }
    }
}
