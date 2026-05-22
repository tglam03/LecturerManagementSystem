package LecturerSystem.view;

import LecturerSystem.file.FileUtil;
import LecturerSystem.model.GiangVien;
import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.service.QuanLyKhoa;
import LecturerSystem.service.QuanLyMonHoc;
import LecturerSystem.utils.MessageDialog;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DashboardFrame extends JFrame {
    private static final Color SIDEBAR_COLOR = new Color(30, 41, 59);
    private static final Color SIDEBAR_HOVER = new Color(51, 65, 85);

    private final QuanLyGiangVien quanLyGiangVien;
    private final QuanLyKhoa quanLyKhoa;
    private final QuanLyMonHoc quanLyMonHoc;
    private final DecimalFormat moneyFormat;

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel lblTime;
    private JLabel lblTongGiangVien;
    private JLabel lblTongKhoa;
    private JLabel lblTongMonHoc;
    private JLabel lblTongLuong;
    private JTable tblOverview;
    private DefaultTableModel overviewModel;
    private GiangVienPanel giangVienPanel;
    private KhoaPanel khoaPanel;
    private MonHocPanel monHocPanel;
    private ThongKePanel thongKePanel;

    public DashboardFrame() {
        quanLyGiangVien = new QuanLyGiangVien();
        quanLyKhoa = new QuanLyKhoa();
        quanLyMonHoc = new QuanLyMonHoc();
        moneyFormat = new DecimalFormat("#,###");
        loadDataQuietly();
        initComponents();
        refreshAll();
    }

    private void initComponents() {
        setTitle("Lecturer Management System - Dashboard");
        setSize(1280, 760);
        setMinimumSize(new Dimension(1080, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIHelper.BG);

        giangVienPanel = new GiangVienPanel(quanLyGiangVien, quanLyKhoa, quanLyMonHoc, this::refreshAll);
        khoaPanel = new KhoaPanel(quanLyKhoa, this::refreshAll);
        monHocPanel = new MonHocPanel(quanLyMonHoc, this::refreshAll);
        thongKePanel = new ThongKePanel(quanLyGiangVien, quanLyKhoa, quanLyMonHoc);

        contentPanel.add(initDashboardPanel(), "dashboard");
        contentPanel.add(giangVienPanel, "giangvien");
        contentPanel.add(khoaPanel, "khoa");
        contentPanel.add(monHocPanel, "monhoc");
        contentPanel.add(thongKePanel, "thongke");

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(initHeader(), BorderLayout.NORTH);
        rightPanel.add(contentPanel, BorderLayout.CENTER);

        add(initSidebar(), BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private JPanel initSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 14, 20, 14));

        JLabel title = new JLabel("LMS Admin");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(title);

        JLabel subTitle = new JLabel("Hệ thống quản lý giảng viên");
        subTitle.setForeground(new Color(203, 213, 225));
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(subTitle);
        sidebar.add(Box.createVerticalStrut(28));

        sidebar.add(createSidebarButton("Dashboard", () -> showPage("dashboard")));
        sidebar.add(createSidebarButton("Quản lý giảng viên", () -> showPage("giangvien")));
        sidebar.add(createSidebarButton("Quản lý khoa", () -> showPage("khoa")));
        sidebar.add(createSidebarButton("Quản lý môn học", () -> showPage("monhoc")));
        sidebar.add(createSidebarButton("Thống kê", () -> showPage("thongke")));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(createSidebarButton("Đọc file", this::readFile));
        sidebar.add(createSidebarButton("Lưu file", this::saveFile));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createSidebarButton("Đăng xuất", this::logout));
        sidebar.add(createSidebarButton("Thoát", this::exitApp));
        return sidebar;
    }

    private JButton createSidebarButton(String text, final Runnable action) {
        final JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(200, 44));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(SIDEBAR_COLOR);
        button.setForeground(new Color(226, 232, 240));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(new EmptyBorder(0, 14, 0, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SIDEBAR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SIDEBAR_COLOR);
            }
        });
        button.addActionListener(e -> action.run());
        return button;
    }

    private JPanel initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 72));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                new EmptyBorder(0, 28, 0, 28)));

        JLabel lblSystem = new JLabel("Lecturer Management System");
        lblSystem.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblSystem.setForeground(UIHelper.TEXT_DARK);

        JPanel right = new JPanel(new GridLayout(2, 1));
        right.setOpaque(false);
        JLabel lblWelcome = new JLabel("Welcome Admin", SwingConstants.RIGHT);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblWelcome.setForeground(UIHelper.TEXT_DARK);
        lblTime = new JLabel("", SwingConstants.RIGHT);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTime.setForeground(UIHelper.TEXT_MUTED);
        right.add(lblWelcome);
        right.add(lblTime);

        header.add(lblSystem, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        updateTime();
        return header;
    }

    private JPanel initDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UIHelper.BG);
        panel.setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIHelper.TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setOpaque(false);
        content.add(initDashboardCards(), BorderLayout.NORTH);
        content.add(initOverviewTable(), BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel initDashboardCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);
        lblTongGiangVien = new JLabel("0");
        lblTongKhoa = new JLabel("0");
        lblTongMonHoc = new JLabel("0");
        lblTongLuong = new JLabel("0 VND");
        cards.add(createStatCard("Tổng giảng viên", lblTongGiangVien, new Color(239, 246, 255), UIHelper.PRIMARY));
        cards.add(createStatCard("Tổng khoa", lblTongKhoa, new Color(255, 247, 237), UIHelper.WARNING));
        cards.add(createStatCard("Tổng môn học", lblTongMonHoc, new Color(240, 253, 244), UIHelper.SUCCESS));
        cards.add(createStatCard("Tổng quỹ lương", lblTongLuong, new Color(250, 245, 255), new Color(147, 51, 234)));
        return cards;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color bg, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setPreferredSize(new Dimension(0, 104));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(16, 18, 16, 18)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(UIHelper.TEXT_MUTED);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accent);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel initOverviewTable() {
        String[] columns = {"Mã GV", "Họ tên", "Khoa", "Môn học", "Học vị", "Lương", "Trạng thái"};
        overviewModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblOverview = new JTable(overviewModel);
        UIHelper.styleTable(tblOverview);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(10, 10, 10, 10)));
        card.add(new JScrollPane(tblOverview), BorderLayout.CENTER);
        return card;
    }

    private void showPage(String page) {
        refreshAll();
        cardLayout.show(contentPanel, page);
    }

    private void refreshAll() {
        if (lblTongGiangVien != null) {
            lblTongGiangVien.setText(String.valueOf(quanLyGiangVien.getAll().size()));
            lblTongKhoa.setText(String.valueOf(quanLyKhoa.getAll().size()));
            lblTongMonHoc.setText(String.valueOf(quanLyMonHoc.getAll().size()));
            lblTongLuong.setText(moneyFormat.format(quanLyGiangVien.tinhTongLuong()) + " VND");
        }
        loadOverview();
        if (giangVienPanel != null) {
            giangVienPanel.refreshData();
        }
        if (khoaPanel != null) {
            khoaPanel.refreshData();
        }
        if (monHocPanel != null) {
            monHocPanel.refreshData();
        }
        if (thongKePanel != null) {
            thongKePanel.refreshData();
        }
    }

    private void loadOverview() {
        if (overviewModel == null) {
            return;
        }
        overviewModel.setRowCount(0);
        for (GiangVien gv : quanLyGiangVien.getAll()) {
            overviewModel.addRow(new Object[]{
                gv.getMaGV(),
                gv.getHoTen(),
                gv.getTenKhoa(),
                gv.getTenMonHoc(),
                gv.getHocVi(),
                moneyFormat.format(gv.tinhLuong()),
                gv.getTrangThai()
            });
        }
    }

    private void loadDataQuietly() {
        try {
            quanLyGiangVien.setDanhSachGiangVien(FileUtil.readGiangVien());
        } catch (Exception ex) {
            quanLyGiangVien.setDanhSachGiangVien(new ArrayList<GiangVien>());
        }
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
    }

    private void readFile() {
        try {
            quanLyGiangVien.setDanhSachGiangVien(FileUtil.readGiangVien());
            quanLyKhoa.setDanhSachKhoa(FileUtil.readKhoa());
            quanLyMonHoc.setDanhSachMonHoc(FileUtil.readMonHoc());
            refreshAll();
            MessageDialog.showInfo(this, "Đọc dữ liệu thành công");
        } catch (IOException ex) {
            MessageDialog.showError(this, "Không thể đọc file: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            MessageDialog.showError(this, "Dữ liệu file không hợp lệ");
        }
    }

    private void saveFile() {
        try {
            FileUtil.saveAll(quanLyGiangVien.getAll(), quanLyKhoa.getAll(), quanLyMonHoc.getAll());
            MessageDialog.showInfo(this, "Lưu dữ liệu thành công");
        } catch (IOException ex) {
            MessageDialog.showError(this, "Không thể lưu file: " + ex.getMessage());
        }
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    private void exitApp() {
        if (MessageDialog.confirm(this, "Bạn có muốn thoát chương trình?")) {
            System.exit(0);
        }
    }

    private void updateTime() {
        lblTime.setText(new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy").format(new Date()));
    }
}
