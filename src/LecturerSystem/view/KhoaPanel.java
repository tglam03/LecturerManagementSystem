package LecturerSystem.view;

import LecturerSystem.model.Khoa;
import LecturerSystem.service.QuanLyKhoa;
import LecturerSystem.utils.MessageDialog;
import LecturerSystem.utils.Validator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class KhoaPanel extends JPanel {
    private final QuanLyKhoa quanLyKhoa;
    private final Runnable onDataChanged;
    private JTextField txtMaKhoa;
    private JTextField txtTenKhoa;
    private JTextField txtTimKiem;
    private JTable table;
    private DefaultTableModel tableModel;
    private String selectedMaKhoa;

    public KhoaPanel(QuanLyKhoa quanLyKhoa, Runnable onDataChanged) {
        this.quanLyKhoa = quanLyKhoa;
        this.onDataChanged = onDataChanged;
        initComponents();
        loadTable(quanLyKhoa.getAll());
    }

    public void refreshData() {
        loadTable(quanLyKhoa.getAll());
    }

    private void initComponents() {
        setLayout(new BorderLayout(14, 14));
        setBackground(UIHelper.BG);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel title = new JLabel("Quản lý khoa");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIHelper.TEXT_DARK);
        add(title, BorderLayout.NORTH);

        add(createBody(), BorderLayout.CENTER);
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(14, 14));
        body.setOpaque(false);
        body.add(initForm(), BorderLayout.NORTH);
        body.add(initTable(), BorderLayout.CENTER);
        body.add(initButtons(), BorderLayout.SOUTH);
        return body;
    }

    private JPanel initForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        // SỬA Ở ĐÂY: Đổi màu viền form nhập liệu sang xám đậm hơn (156, 163, 175)
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(156, 163, 175), 1),
                new EmptyBorder(16, 18, 16, 18)));

        txtMaKhoa = createInput();
        txtTenKhoa = createInput();

        addField(panel, 0, 0, "Mã khoa", txtMaKhoa);
        addField(panel, 0, 1, "Tên khoa", txtTenKhoa);
        
        return panel;
    }

    private JPanel initTable() {
        String[] columns = {"Mã khoa", "Tên khoa"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UIHelper.styleTable(table);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromSelectedRow();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        // SỬA Ở ĐÂY: Đổi màu viền khung Table sang xám đậm hơn
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(156, 163, 175), 1),
                new EmptyBorder(10, 10, 10, 10)));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel initButtons() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        JButton btnThem = UIHelper.createButton("Thêm", UIHelper.SUCCESS);
        JButton btnSua = UIHelper.createButton("Sửa", UIHelper.PRIMARY);
        JButton btnXoa = UIHelper.createButton("Xóa", UIHelper.DANGER);
        JButton btnLamMoi = UIHelper.createButton("Làm mới", new Color(100, 116, 139));

        leftPanel.add(btnThem);
        leftPanel.add(btnSua);
        leftPanel.add(btnXoa);
        leftPanel.add(btnLamMoi);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        txtTimKiem = createInput();
        txtTimKiem.setPreferredSize(new Dimension(200, 36)); 
        JButton btnTim = UIHelper.createButton("Tìm kiếm", new Color(14, 165, 233));

        rightPanel.add(txtTimKiem);
        rightPanel.add(btnTim);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        btnThem.addActionListener(e -> addKhoa());
        btnSua.addActionListener(e -> updateKhoa());
        btnXoa.addActionListener(e -> deleteKhoa());
        btnTim.addActionListener(e -> loadTable(quanLyKhoa.searchKhoa(txtTimKiem.getText())));
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadTable(quanLyKhoa.getAll());
        });
        
        return panel;
    }

    private void addKhoa() {
        if (!validateForm(true)) {
            return;
        }
        Khoa khoa = new Khoa(txtMaKhoa.getText().trim(), txtTenKhoa.getText().trim());
        if (quanLyKhoa.addKhoa(khoa)) {
            afterDataChanged("Thêm khoa thành công");
        } else {
            MessageDialog.showError(this, "Mã khoa đã tồn tại");
        }
    }

    private void updateKhoa() {
        if (selectedMaKhoa == null) {
            MessageDialog.showError(this, "Vui lòng chọn khoa cần sửa");
            return;
        }
        if (!validateForm(false)) {
            return;
        }
        Khoa khoa = new Khoa(txtMaKhoa.getText().trim(), txtTenKhoa.getText().trim());
        if (quanLyKhoa.updateKhoa(selectedMaKhoa, khoa)) {
            selectedMaKhoa = khoa.getMaKhoa();
            afterDataChanged("Cập nhật khoa thành công");
        }
    }

    private void deleteKhoa() {
        if (selectedMaKhoa == null) {
            MessageDialog.showError(this, "Vui lòng chọn khoa cần xóa");
            return;
        }
        if (!MessageDialog.confirm(this, "Bạn có chắc muốn xóa khoa này?")) {
            return;
        }
        if (quanLyKhoa.deleteKhoa(selectedMaKhoa)) {
            afterDataChanged("Xóa khoa thành công");
            clearForm();
        }
    }

    private boolean validateForm(boolean isAdd) {
        if (Validator.isEmpty(txtMaKhoa.getText()) || Validator.isEmpty(txtTenKhoa.getText())) {
            MessageDialog.showError(this, "Vui lòng nhập đầy đủ mã khoa và tên khoa");
            return false;
        }
        String maMoi = txtMaKhoa.getText().trim();
        if ((isAdd || !maMoi.equalsIgnoreCase(selectedMaKhoa)) && quanLyKhoa.isDuplicateMa(maMoi)) {
            MessageDialog.showError(this, "Mã khoa không được trùng");
            return false;
        }
        return true;
    }

    private void afterDataChanged(String message) {
        loadTable(quanLyKhoa.getAll());
        if (onDataChanged != null) {
            onDataChanged.run();
        }
        MessageDialog.showInfo(this, message);
    }

    private void loadTable(ArrayList<Khoa> list) {
        tableModel.setRowCount(0);
        for (Khoa khoa : list) {
            tableModel.addRow(new Object[]{khoa.getMaKhoa(), khoa.getTenKhoa()});
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        selectedMaKhoa = tableModel.getValueAt(modelRow, 0).toString();
        txtMaKhoa.setText(selectedMaKhoa);
        txtTenKhoa.setText(tableModel.getValueAt(modelRow, 1).toString());
    }

    private void clearForm() {
        selectedMaKhoa = null;
        txtMaKhoa.setText("");
        txtTenKhoa.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
    }

    private JTextField createInput() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(220, 36));
        // SỬA Ở ĐÂY: Đổi màu viền ô nhập liệu (Input) sang xám đậm hơn
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(156, 163, 175), 1),
                new EmptyBorder(0, 10, 0, 10)));
        return field;
    }

    private void addField(JPanel panel, int row, int column, String label, JTextField input) {
        int startCol = column * 2;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.gridy = row;
        gbc.gridx = startCol;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lbl, gbc);

        gbc.gridx = startCol + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(input, gbc);
    }
}