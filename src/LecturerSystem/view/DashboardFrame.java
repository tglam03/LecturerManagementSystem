package LecturerSystem.view;

import LecturerSystem.file.FileUtil;
import LecturerSystem.model.GiangVien;
import LecturerSystem.model.GiangVienCoHuu;
import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.utils.MessageDialog;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class DashboardFrame extends JFrame {
    private static final Color SIDEBAR_COLOR = new Color(30, 41, 59);
    private static final Color SIDEBAR_HOVER = new Color(51, 65, 85);
    private static final Color CONTENT_BG = new Color(245, 247, 251);
    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);

    private final QuanLyGiangVien quanLyGiangVien;
    private final DecimalFormat moneyFormat;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel lblTime;
    private JLabel lblTongGiangVien;
    private JLabel lblTongLuong;
    private JLabel lblTongKhoa;
    private JLabel lblTongGiangVienThongKe;
    private JLabel lblTongLuongThongKe;
    private JLabel lblTongKhoaThongKe;
    private DefaultTableModel overviewTableModel;
    private DefaultTableModel lecturerTableModel;
    private JTable tblOverview;
    private JTable tblLecturer;
    private JTextField txtSearch;
    private BarChartPanel barChartPanel;
    private PieChartPanel pieChartPanel;

    public DashboardFrame() {
        quanLyGiangVien = new QuanLyGiangVien();
        moneyFormat = new DecimalFormat("#,###");
        loadDataQuietly();
        initComponents();
        refreshDashboard();
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
        contentPanel.setBackground(CONTENT_BG);
        contentPanel.add(initDashboardPanel(), "dashboard");
        contentPanel.add(initLecturerPanel(), "lecturers");
        contentPanel.add(initStatisticsPanel(), "statistics");

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

        JLabel subTitle = new JLabel("Quản lý giảng viên");
        subTitle.setForeground(new Color(203, 213, 225));
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(subTitle);
        sidebar.add(Box.createVerticalStrut(28));

        sidebar.add(createSidebarButton("Dashboard", () -> showPage("dashboard")));
        sidebar.add(createSidebarButton("Quản lý giảng viên", () -> showPage("lecturers")));
        sidebar.add(createSidebarButton("Thống kê", () -> showPage("statistics")));
        sidebar.add(createSidebarButton("Tìm kiếm", () -> {
            showPage("lecturers");
            txtSearch.requestFocus();
        }));
        sidebar.add(createSidebarButton("Sắp xếp", () -> {
            quanLyGiangVien.sortBySalary();
            refreshDashboard();
            showPage("lecturers");
            MessageDialog.showInfo(this, "Đã sắp xếp theo lương giảm dần");
        }));
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
        lblSystem.setForeground(TEXT_DARK);

        JPanel right = new JPanel(new GridLayout(2, 1));
        right.setOpaque(false);
        JLabel lblWelcome = new JLabel("Welcome Admin", SwingConstants.RIGHT);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblWelcome.setForeground(TEXT_DARK);
        lblTime = new JLabel("", SwingConstants.RIGHT);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTime.setForeground(TEXT_MUTED);
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
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(22, 24, 22, 24));
        panel.add(initDashboardCards(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 16));
        center.setOpaque(false);
        barChartPanel = new BarChartPanel("Số lượng giảng viên theo khoa");
        pieChartPanel = new PieChartPanel("Tỷ lệ học vị");
        center.add(wrapCard(barChartPanel));
        center.add(wrapCard(pieChartPanel));
        panel.add(center, BorderLayout.CENTER);
        panel.add(initOverviewTablePanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel initDashboardCards() {
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);
        lblTongGiangVien = new JLabel("0");
        lblTongLuong = new JLabel("0 VND");
        lblTongKhoa = new JLabel("0");
        cards.add(createStatCard("Tổng giảng viên", lblTongGiangVien, new Color(239, 246, 255), new Color(37, 99, 235)));
        cards.add(createStatCard("Tổng quỹ lương", lblTongLuong, new Color(240, 253, 244), new Color(22, 163, 74)));
        cards.add(createStatCard("Tổng khoa", lblTongKhoa, new Color(255, 247, 237), new Color(234, 88, 12)));
        return cards;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color bg, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(18, 20, 18, 20)));
        card.setPreferredSize(new Dimension(0, 112));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(TEXT_MUTED);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accent);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel initLecturerPanel() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setOpaque(false);
        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                new EmptyBorder(8, 10, 8, 10)));
        JButton btnSearch = createActionButton("Tìm kiếm", PRIMARY);
        JButton btnManage = createActionButton("Mở màn hình CRUD", new Color(22, 163, 74));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(btnSearch);
        actions.add(btnManage);
        top.add(txtSearch, BorderLayout.CENTER);
        top.add(actions, BorderLayout.EAST);

        btnSearch.addActionListener(e -> loadOverviewTable(quanLyGiangVien.searchGiangVien(txtSearch.getText())));
        btnManage.addActionListener(e -> new MainFrame(quanLyGiangVien).setVisible(true));

        JPanel tableCard = wrapCard(new JScrollPane(createLecturerTable()));
        panel.add(top, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel initStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(new EmptyBorder(22, 24, 22, 24));
        panel.add(initStatisticsCards(), BorderLayout.NORTH);

        JPanel charts = new JPanel(new GridLayout(1, 2, 16, 16));
        charts.setOpaque(false);
        charts.add(wrapCard(new BarChartPanel("Giảng viên theo khoa")));
        charts.add(wrapCard(new PieChartPanel("Học vị giảng viên")));
        panel.add(charts, BorderLayout.CENTER);
        return panel;
    }

    private JPanel initStatisticsCards() {
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);
        lblTongGiangVienThongKe = new JLabel("0");
        lblTongLuongThongKe = new JLabel("0 VND");
        lblTongKhoaThongKe = new JLabel("0");
        cards.add(createStatCard("Tổng giảng viên", lblTongGiangVienThongKe, new Color(239, 246, 255), new Color(37, 99, 235)));
        cards.add(createStatCard("Tổng quỹ lương", lblTongLuongThongKe, new Color(240, 253, 244), new Color(22, 163, 74)));
        cards.add(createStatCard("Tổng khoa", lblTongKhoaThongKe, new Color(255, 247, 237), new Color(234, 88, 12)));
        return cards;
    }

    private JPanel initOverviewTablePanel() {
        JPanel panel = wrapCard(new JScrollPane(createDashboardTable()));
        panel.setPreferredSize(new Dimension(0, 230));
        return panel;
    }

    private JTable createDashboardTable() {
        if (tblOverview != null) {
            return tblOverview;
        }
        String[] columns = {"Mã", "Họ tên", "Khoa", "Môn dạy", "Loại", "Lương"};
        overviewTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblOverview = new JTable(overviewTableModel);
        styleTable(tblOverview);
        return tblOverview;
    }

    private JTable createLecturerTable() {
        if (tblLecturer != null) {
            return tblLecturer;
        }
        String[] columns = {"Mã", "Họ tên", "Khoa", "Môn dạy", "Loại", "Lương"};
        lecturerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLecturer = new JTable(lecturerTableModel);
        styleTable(tblLecturer);
        return tblLecturer;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT_DARK);
        table.setGridColor(new Color(226, 232, 240));
        table.setShowVerticalLines(false);
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 38));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(TEXT_DARK);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(TEXT_DARK);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(150, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel wrapCard(Component component) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(12, 12, 12, 12)));
        card.add(component, BorderLayout.CENTER);
        return card;
    }

    private void showPage(String page) {
        refreshDashboard();
        cardLayout.show(contentPanel, page);
    }

    private void refreshDashboard() {
        if (lblTongGiangVien != null) {
            lblTongGiangVien.setText(String.valueOf(quanLyGiangVien.getAll().size()));
            lblTongLuong.setText(moneyFormat.format(quanLyGiangVien.tinhTongLuong()) + " VND");
            lblTongKhoa.setText(String.valueOf(quanLyGiangVien.getDanhSachKhoa().size()));
        }
        if (lblTongGiangVienThongKe != null) {
            lblTongGiangVienThongKe.setText(String.valueOf(quanLyGiangVien.getAll().size()));
            lblTongLuongThongKe.setText(moneyFormat.format(quanLyGiangVien.tinhTongLuong()) + " VND");
            lblTongKhoaThongKe.setText(String.valueOf(quanLyGiangVien.getDanhSachKhoa().size()));
        }
        loadOverviewTable(quanLyGiangVien.getAll());
        repaint();
    }

    private void loadOverviewTable(ArrayList<GiangVien> list) {
        if (overviewTableModel != null) {
            overviewTableModel.setRowCount(0);
        }
        if (lecturerTableModel != null) {
            lecturerTableModel.setRowCount(0);
        }
        for (GiangVien gv : list) {
            Object[] row = new Object[]{
                gv.getMaGiangVien(),
                gv.getHoTen(),
                gv.getKhoa(),
                gv.getMonDay(),
                gv.getLoaiGiangVien(),
                moneyFormat.format(gv.tinhLuong())
            };
            if (overviewTableModel != null) {
                overviewTableModel.addRow(row);
            }
            if (lecturerTableModel != null) {
                lecturerTableModel.addRow(row);
            }
        }
    }

    private void loadDataQuietly() {
        try {
            quanLyGiangVien.setDanhSachGiangVien(FileUtil.readFromFile(FileUtil.DEFAULT_FILE));
        } catch (Exception ex) {
            quanLyGiangVien.setDanhSachGiangVien(new ArrayList<GiangVien>());
        }
    }

    private void readFile() {
        try {
            quanLyGiangVien.setDanhSachGiangVien(FileUtil.readFromFile(FileUtil.DEFAULT_FILE));
            refreshDashboard();
            MessageDialog.showInfo(this, "Đọc file thành công");
        } catch (IOException ex) {
            MessageDialog.showError(this, "Không thể đọc file: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            MessageDialog.showError(this, "Dữ liệu file không hợp lệ");
        }
    }

    private void saveFile() {
        try {
            FileUtil.saveToFile(quanLyGiangVien.getAll(), FileUtil.DEFAULT_FILE);
            MessageDialog.showInfo(this, "Lưu file thành công");
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

    private Map<String, Integer> buildKhoaData() {
        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        for (String khoa : quanLyGiangVien.getDanhSachKhoa()) {
            data.put(khoa, quanLyGiangVien.countByKhoa(khoa));
        }
        return data;
    }

    private Map<String, Integer> buildHocViData() {
        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        for (GiangVien gv : quanLyGiangVien.getAll()) {
            String key = "Thỉnh giảng";
            if (gv instanceof GiangVienCoHuu) {
                key = ((GiangVienCoHuu) gv).getHocVi();
            }
            Integer count = data.get(key);
            data.put(key, count == null ? 1 : count + 1);
        }
        return data;
    }

    private class BarChartPanel extends JPanel {
        private final String title;

        BarChartPanel(String title) {
            this.title = title;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Map<String, Integer> data = buildKhoaData();

            g2.setColor(TEXT_DARK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.drawString(title, 14, 24);

            if (data.isEmpty()) {
                drawEmpty(g2);
                g2.dispose();
                return;
            }

            int left = 44;
            int bottom = getHeight() - 42;
            int top = 48;
            int chartWidth = getWidth() - left - 24;
            int chartHeight = bottom - top;
            int max = 1;
            for (Integer value : data.values()) {
                if (value > max) {
                    max = value;
                }
            }

            g2.setColor(new Color(226, 232, 240));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(left, top, left, bottom);
            g2.drawLine(left, bottom, left + chartWidth, bottom);

            int count = data.size();
            int slot = Math.max(48, chartWidth / count);
            int barWidth = Math.max(28, slot / 2);
            int i = 0;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int barHeight = (int) ((entry.getValue() * 1.0 / max) * (chartHeight - 16));
                int x = left + i * slot + (slot - barWidth) / 2;
                int y = bottom - barHeight;
                g2.setColor(new Color(37, 99, 235));
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                g2.setColor(TEXT_DARK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString(String.valueOf(entry.getValue()), x + barWidth / 2 - 4, y - 6);
                g2.setColor(TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.drawString(shortText(entry.getKey()), x - 8, bottom + 18);
                i++;
            }
            g2.dispose();
        }
    }

    private class PieChartPanel extends JPanel {
        private final String title;
        private final Color[] colors = {
            new Color(37, 99, 235),
            new Color(22, 163, 74),
            new Color(234, 88, 12),
            new Color(147, 51, 234),
            new Color(14, 165, 233),
            new Color(239, 68, 68)
        };

        PieChartPanel(String title) {
            this.title = title;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Map<String, Integer> data = buildHocViData();

            g2.setColor(TEXT_DARK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.drawString(title, 14, 24);

            if (data.isEmpty()) {
                drawEmpty(g2);
                g2.dispose();
                return;
            }

            int total = 0;
            for (Integer value : data.values()) {
                total += value;
            }

            int size = Math.min(getWidth() / 2, getHeight() - 72);
            int x = 32;
            int y = 54;
            int start = 0;
            int i = 0;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int angle = (int) Math.round(entry.getValue() * 360.0 / total);
                g2.setColor(colors[i % colors.length]);
                g2.fillArc(x, y, size, size, start, angle);
                start += angle;
                i++;
            }

            int legendX = x + size + 28;
            int legendY = y + 10;
            i = 0;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                g2.setColor(colors[i % colors.length]);
                g2.fillRoundRect(legendX, legendY + i * 24, 14, 14, 4, 4);
                g2.setColor(TEXT_DARK);
                g2.drawString(entry.getKey() + " (" + entry.getValue() + ")", legendX + 22, legendY + 12 + i * 24);
                i++;
            }
            g2.dispose();
        }
    }

    private void drawEmpty(Graphics2D g2) {
        g2.setColor(TEXT_MUTED);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.drawString("Chưa có dữ liệu để hiển thị", 24, 96);
    }

    private String shortText(String value) {
        if (value == null) {
            return "";
        }
        return value.length() > 10 ? value.substring(0, 10) + "..." : value;
    }
}
