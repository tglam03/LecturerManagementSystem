package LecturerSystem.view;

import LecturerSystem.model.MonHoc;
import LecturerSystem.service.QuanLyMonHoc;
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

public class MonHocPanel extends JPanel {
    private final QuanLyMonHoc quanLyMonHoc;
    private final Runnable onDataChanged;
    private JTextField txtMaMon;
    private JTextField txtTenMon;
    private JTextField txtSoTinChi;
    private JTextField txtTimKiem;
    private JTable table;
    private DefaultTableModel tableModel;
    private String selectedMaMon;

    public MonHocPanel(QuanLyMonHoc quanLyMonHoc, Runnable onDataChanged) {
        this.quanLyMonHoc = quanLyMonHoc;
        this.onDataChanged = onDataChanged;
        initComponents();
        loadTable(quanLyMonHoc.getAll());
    }

    public void refreshData() {
        loadTable(quanLyMonHoc.getAll());
    }

    private void initComponents() {
        setLayout(new BorderLayout(14, 14));
        setBackground(UIHelper.BG);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel title = new JLabel("Quản lý môn học");
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
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(16, 18, 16, 18)));

        txtMaMon = createInput();
        txtTenMon = createInput();
        txtSoTinChi = createInput();
        txtTimKiem = createInput();

        addField(panel, 0, 0, "Mã môn", txtMaMon);
        addField(panel, 0, 1, "Tên môn", txtTenMon);
        addField(panel, 1, 0, "Số tín chỉ", txtSoTinChi);
        addField(panel, 1, 1, "Tìm kiếm", txtTimKiem);
        return panel;
    }

    private JPanel initTable() {
        String[] columns = {"Mã môn", "Tên môn", "Số tín chỉ"};
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
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(10, 10, 10, 10)));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel initButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);
        JButton btnThem = UIHelper.createButton("Thêm", UIHelper.SUCCESS);
        JButton btnSua = UIHelper.createButton("Sửa", UIHelper.PRIMARY);
        JButton btnXoa = UIHelper.createButton("Xóa", UIHelper.DANGER);
        JButton btnTim = UIHelper.createButton("Tìm kiếm", new Color(14, 165, 233));
        JButton btnLamMoi = UIHelper.createButton("Làm mới", new Color(100, 116, 139));

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnTim);
        panel.add(btnLamMoi);

        btnThem.addActionListener(e -> addMonHoc());
        btnSua.addActionListener(e -> updateMonHoc());
        btnXoa.addActionListener(e -> deleteMonHoc());
        btnTim.addActionListener(e -> loadTable(quanLyMonHoc.searchMonHoc(txtTimKiem.getText())));
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadTable(quanLyMonHoc.getAll());
        });
        return panel;
    }

    private void addMonHoc() {
        MonHoc monHoc = createMonHocFromForm(true);
        if (monHoc == null) {
            return;
        }
        if (quanLyMonHoc.addMonHoc(monHoc)) {
            afterDataChanged("Thêm môn học thành công");
        } else {
            MessageDialog.showError(this, "Mã môn học đã tồn tại");
        }
    }

    private void updateMonHoc() {
        if (selectedMaMon == null) {
            MessageDialog.showError(this, "Vui lòng chọn môn học cần sửa");
            return;
        }
        MonHoc monHoc = createMonHocFromForm(false);
        if (monHoc == null) {
            return;
        }
        if (quanLyMonHoc.updateMonHoc(selectedMaMon, monHoc)) {
            selectedMaMon = monHoc.getMaMon();
            afterDataChanged("Cập nhật môn học thành công");
        }
    }

    private void deleteMonHoc() {
        if (selectedMaMon == null) {
            MessageDialog.showError(this, "Vui lòng chọn môn học cần xóa");
            return;
        }
        if (!MessageDialog.confirm(this, "Bạn có chắc muốn xóa môn học này?")) {
            return;
        }
        if (quanLyMonHoc.deleteMonHoc(selectedMaMon)) {
            afterDataChanged("Xóa môn học thành công");
            clearForm();
        }
    }

    private MonHoc createMonHocFromForm(boolean isAdd) {
        if (Validator.isEmpty(txtMaMon.getText()) || Validator.isEmpty(txtTenMon.getText())
                || Validator.isEmpty(txtSoTinChi.getText())) {
            MessageDialog.showError(this, "Vui lòng nhập đầy đủ thông tin môn học");
            return null;
        }
        String maMoi = txtMaMon.getText().trim();
        if ((isAdd || !maMoi.equalsIgnoreCase(selectedMaMon)) && quanLyMonHoc.isDuplicateMa(maMoi)) {
            MessageDialog.showError(this, "Mã môn học không được trùng");
            return null;
        }
        try {
            int soTinChi = Validator.parseInt(txtSoTinChi.getText());
            if (!Validator.isSoTinChiHopLe(soTinChi)) {
                MessageDialog.showError(this, "Số tín chỉ phải lớn hơn 0");
                return null;
            }
            return new MonHoc(maMoi, txtTenMon.getText().trim(), soTinChi);
        } catch (NumberFormatException ex) {
            MessageDialog.showError(this, "Số tín chỉ phải là số nguyên");
            return null;
        }
    }

    private void afterDataChanged(String message) {
        loadTable(quanLyMonHoc.getAll());
        if (onDataChanged != null) {
            onDataChanged.run();
        }
        MessageDialog.showInfo(this, message);
    }

    private void loadTable(ArrayList<MonHoc> list) {
        tableModel.setRowCount(0);
        for (MonHoc monHoc : list) {
            tableModel.addRow(new Object[]{monHoc.getMaMon(), monHoc.getTenMon(), monHoc.getSoTinChi()});
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        selectedMaMon = tableModel.getValueAt(modelRow, 0).toString();
        txtMaMon.setText(selectedMaMon);
        txtTenMon.setText(tableModel.getValueAt(modelRow, 1).toString());
        txtSoTinChi.setText(tableModel.getValueAt(modelRow, 2).toString());
    }

    private void clearForm() {
        selectedMaMon = null;
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtSoTinChi.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
    }

    private JTextField createInput() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(220, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
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
