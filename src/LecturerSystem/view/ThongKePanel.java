package LecturerSystem.view;

import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.service.QuanLyKhoa;
import LecturerSystem.service.QuanLyMonHoc;
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
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ThongKePanel extends JPanel {
    private final QuanLyGiangVien quanLyGiangVien;
    private final QuanLyKhoa quanLyKhoa;
    private final QuanLyMonHoc quanLyMonHoc;
    private final DecimalFormat moneyFormat;

    private JLabel lblTongGiangVien;
    private JLabel lblTongKhoa;
    private JLabel lblTongMonHoc;
    private JLabel lblTongLuong;
    private BarChartPanel barChartPanel;
    private PieChartPanel pieChartPanel;

    public ThongKePanel(QuanLyGiangVien quanLyGiangVien, QuanLyKhoa quanLyKhoa,
            QuanLyMonHoc quanLyMonHoc) {
        this.quanLyGiangVien = quanLyGiangVien;
        this.quanLyKhoa = quanLyKhoa;
        this.quanLyMonHoc = quanLyMonHoc;
        this.moneyFormat = new DecimalFormat("#,###");
        initComponents();
        refreshData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(UIHelper.BG);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel title = new JLabel("Thống kê hệ thống");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIHelper.TEXT_DARK);
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setOpaque(false);
        content.add(initCards(), BorderLayout.NORTH);

        JPanel charts = new JPanel(new GridLayout(1, 2, 16, 16));
        charts.setOpaque(false);
        barChartPanel = new BarChartPanel();
        pieChartPanel = new PieChartPanel();
        charts.add(wrapCard(barChartPanel));
        charts.add(wrapCard(pieChartPanel));
        content.add(charts, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    private JPanel initCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);
        lblTongGiangVien = new JLabel("0");
        lblTongKhoa = new JLabel("0");
        lblTongMonHoc = new JLabel("0");
        lblTongLuong = new JLabel("0 VND");
        cards.add(createStatCard("Tổng giảng viên", lblTongGiangVien, new Color(239, 246, 255), new Color(37, 99, 235)));
        cards.add(createStatCard("Tổng khoa", lblTongKhoa, new Color(255, 247, 237), new Color(234, 88, 12)));
        cards.add(createStatCard("Tổng môn học", lblTongMonHoc, new Color(240, 253, 244), new Color(22, 163, 74)));
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

    public void refreshData() {
        if (lblTongGiangVien == null) {
            return;
        }
        lblTongGiangVien.setText(String.valueOf(quanLyGiangVien.getAll().size()));
        lblTongKhoa.setText(String.valueOf(quanLyKhoa.getAll().size()));
        lblTongMonHoc.setText(String.valueOf(quanLyMonHoc.getAll().size()));
        lblTongLuong.setText(moneyFormat.format(quanLyGiangVien.tinhTongLuong()) + " VND");
        repaint();
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

    private class BarChartPanel extends JPanel {
        BarChartPanel() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawTitle(g2, "Số lượng giảng viên theo khoa");

            Map<String, Integer> data = quanLyGiangVien.thongKeTheoKhoa();
            if (data.isEmpty()) {
                drawEmpty(g2);
                g2.dispose();
                return;
            }

            int left = 44;
            int bottom = getHeight() - 44;
            int top = 54;
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

            int slot = Math.max(56, chartWidth / data.size());
            int barWidth = Math.max(30, slot / 2);
            int i = 0;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int barHeight = (int) ((entry.getValue() * 1.0 / max) * (chartHeight - 18));
                int x = left + i * slot + (slot - barWidth) / 2;
                int y = bottom - barHeight;
                g2.setColor(UIHelper.PRIMARY);
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                g2.setColor(UIHelper.TEXT_DARK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString(String.valueOf(entry.getValue()), x + barWidth / 2 - 4, y - 6);
                g2.setColor(UIHelper.TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.drawString(shortText(entry.getKey()), x - 8, bottom + 18);
                i++;
            }
            g2.dispose();
        }
    }

    private class PieChartPanel extends JPanel {
        private final Color[] colors = {
            new Color(37, 99, 235),
            new Color(22, 163, 74),
            new Color(234, 88, 12),
            new Color(147, 51, 234),
            new Color(14, 165, 233),
            new Color(239, 68, 68)
        };

        PieChartPanel() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawTitle(g2, "Số lượng theo học vị");

            Map<String, Integer> data = quanLyGiangVien.thongKeTheoHocVi();
            if (data.isEmpty()) {
                drawEmpty(g2);
                g2.dispose();
                return;
            }

            int total = 0;
            for (Integer value : data.values()) {
                total += value;
            }

            int size = Math.min(getWidth() / 2, getHeight() - 76);
            int x = 30;
            int y = 56;
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
                g2.setColor(UIHelper.TEXT_DARK);
                g2.drawString(entry.getKey() + " (" + entry.getValue() + ")", legendX + 22, legendY + 12 + i * 24);
                i++;
            }
            g2.dispose();
        }
    }

    private void drawTitle(Graphics2D g2, String title) {
        g2.setColor(UIHelper.TEXT_DARK);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.drawString(title, 14, 24);
    }

    private void drawEmpty(Graphics2D g2) {
        g2.setColor(UIHelper.TEXT_MUTED);
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
