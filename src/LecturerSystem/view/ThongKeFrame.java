package LecturerSystem.view;

import LecturerSystem.model.GiangVien;
import LecturerSystem.model.GiangVienCoHuu;
import LecturerSystem.service.QuanLyGiangVien;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ThongKeFrame extends JFrame {
    private static final Color BG = new Color(245, 247, 251);
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);

    private final QuanLyGiangVien quanLyGiangVien;
    private final DecimalFormat moneyFormat;
    private JTable tblThongKe;
    private DefaultTableModel tableModel;

    public ThongKeFrame(QuanLyGiangVien quanLyGiangVien) {
        this.quanLyGiangVien = quanLyGiangVien;
        moneyFormat = new DecimalFormat("#,###");
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Thống kê giảng viên");
        setSize(980, 640);
        setMinimumSize(new Dimension(880, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(16, 16));
        getContentPane().setBackground(BG);

        add(initHeader(), BorderLayout.NORTH);
        add(initContent(), BorderLayout.CENTER);
    }

    private JPanel initHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 24, 0, 24));

        JLabel title = new JLabel("Thống kê hệ thống");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Tổng quan số lượng, quỹ lương, khoa và học vị");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_MUTED);

        JPanel texts = new JPanel(new BorderLayout());
        texts.setOpaque(false);
        texts.add(title, BorderLayout.NORTH);
        texts.add(subtitle, BorderLayout.SOUTH);
        panel.add(texts, BorderLayout.WEST);
        return panel;
    }

    private JPanel initContent() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 24, 24, 24));
        panel.add(initCards(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 16));
        center.setOpaque(false);
        center.add(wrapCard(new KhoaBarChartPanel()));
        center.add(wrapCard(new HocViPieChartPanel()));
        panel.add(center, BorderLayout.CENTER);
        panel.add(initTablePanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel initCards() {
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);
        cards.add(createStatCard("Tổng giảng viên", String.valueOf(quanLyGiangVien.getAll().size()),
                new Color(239, 246, 255), new Color(37, 99, 235)));
        cards.add(createStatCard("Tổng quỹ lương", moneyFormat.format(quanLyGiangVien.tinhTongLuong()) + " VND",
                new Color(240, 253, 244), new Color(22, 163, 74)));
        cards.add(createStatCard("Tổng khoa", String.valueOf(quanLyGiangVien.getDanhSachKhoa().size()),
                new Color(255, 247, 237), new Color(234, 88, 12)));
        return cards;
    }

    private JPanel createStatCard(String title, String value, Color bg, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setPreferredSize(new Dimension(0, 104));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(16, 18, 16, 18)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(TEXT_MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblValue.setForeground(accent);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private JPanel initTablePanel() {
        String[] columns = {"Nội dung", "Giá trị"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblThongKe = new JTable(tableModel);
        styleTable(tblThongKe);

        JPanel panel = wrapCard(new JScrollPane(tblThongKe));
        panel.setPreferredSize(new Dimension(0, 190));
        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"Tổng giảng viên", quanLyGiangVien.getAll().size()});
        tableModel.addRow(new Object[]{"Tổng quỹ lương", moneyFormat.format(quanLyGiangVien.tinhTongLuong()) + " VND"});
        for (String khoa : quanLyGiangVien.getDanhSachKhoa()) {
            tableModel.addRow(new Object[]{"Khoa " + khoa, quanLyGiangVien.countByKhoa(khoa) + " giảng viên"});
        }
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

    private void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT_DARK);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 36));
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

    private class KhoaBarChartPanel extends JPanel {
        KhoaBarChartPanel() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Map<String, Integer> data = buildKhoaData();
            drawTitle(g2, "Biểu đồ cột theo khoa");
            if (data.isEmpty()) {
                drawEmpty(g2);
                g2.dispose();
                return;
            }

            int left = 44;
            int bottom = getHeight() - 42;
            int top = 52;
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

            int slot = Math.max(54, chartWidth / data.size());
            int barWidth = Math.max(30, slot / 2);
            int i = 0;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int barHeight = (int) ((entry.getValue() * 1.0 / max) * (chartHeight - 18));
                int x = left + i * slot + (slot - barWidth) / 2;
                int y = bottom - barHeight;
                g2.setColor(new Color(37, 99, 235));
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                g2.setColor(TEXT_DARK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString(String.valueOf(entry.getValue()), x + barWidth / 2 - 4, y - 6);
                g2.setColor(TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.drawString(shortText(entry.getKey()), x - 6, bottom + 18);
                i++;
            }
            g2.dispose();
        }
    }

    private class HocViPieChartPanel extends JPanel {
        private final Color[] colors = {
            new Color(37, 99, 235),
            new Color(22, 163, 74),
            new Color(234, 88, 12),
            new Color(147, 51, 234),
            new Color(14, 165, 233),
            new Color(239, 68, 68)
        };

        HocViPieChartPanel() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Map<String, Integer> data = buildHocViData();
            drawTitle(g2, "Biểu đồ tròn theo học vị");
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
            int x = 30;
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

    private void drawTitle(Graphics2D g2, String title) {
        g2.setColor(TEXT_DARK);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.drawString(title, 14, 24);
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
