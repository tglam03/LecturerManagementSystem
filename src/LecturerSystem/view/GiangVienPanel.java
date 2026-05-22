package LecturerSystem.view;

import LecturerSystem.model.GiangVien;
import LecturerSystem.model.Khoa;
import LecturerSystem.model.MonHoc;
import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.service.QuanLyKhoa;
import LecturerSystem.service.QuanLyMonHoc;
import LecturerSystem.utils.MessageDialog;
import LecturerSystem.utils.Validator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class GiangVienPanel extends JPanel {
    private final QuanLyGiangVien quanLyGiangVien;
    private final QuanLyKhoa quanLyKhoa;
    private final QuanLyMonHoc quanLyMonHoc;
    private final Runnable onDataChanged;
    private final DecimalFormat moneyFormat;

    private JTextField txtMaGV;
    private JTextField txtHoTen;
    private JTextField txtNgaySinh;
    private JTextField txtDiaChi;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextField txtHeSoLuong;
    private JTextField txtTimKiem;
    private JComboBox<String> cboGioiTinh;
    private JComboBox<Khoa> cboKhoa;
    private JComboBox<MonHoc> cboMonHoc;
    private JComboBox<String> cboHocVi;
    private JComboBox<String> cboTrangThai;
    private JComboBox<Object> cboFilterKhoa;
    private JComboBox<String> cboFilterHocVi;
    private JTable table;
    private DefaultTableModel tableModel;
    private String selectedMaGV;

    public GiangVienPanel(QuanLyGiangVien quanLyGiangVien, QuanLyKhoa quanLyKhoa,
            QuanLyMonHoc quanLyMonHoc, Runnable onDataChanged) {
        this.quanLyGiangVien = quanLyGiangVien;
        this.quanLyKhoa = quanLyKhoa;
        this.quanLyMonHoc = quanLyMonHoc;
        this.onDataChanged = onDataChanged;
        this.moneyFormat = new DecimalFormat("#,###");
        initComponents();
        reloadComboData();
        loadTable(quanLyGiangVien.getAll());
    }

    private void initComponents() {
        setLayout(new BorderLayout(14, 14));
        setBackground(UIHelper.BG);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel title = new JLabel("Quản lý giảng viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIHelper.TEXT_DARK);
        add(title, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(14, 14));
        body.setOpaque(false);
        body.add(initForm(), BorderLayout.NORTH);
        body.add(initTable(), BorderLayout.CENTER);
        body.add(initButtons(), BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);
    }

    private JPanel initForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(16, 18, 16, 18)));

        txtMaGV = createInput();
        txtHoTen = createInput();
        txtNgaySinh = createInput();
        txtDiaChi = createInput();
        txtSoDienThoai = createInput();
        txtEmail = createInput();
        txtHeSoLuong = createInput();
        txtTimKiem = createInput();
        cboGioiTinh = createStringCombo(new String[]{"Nam", "Nữ", "Khác"});
        cboKhoa = new JComboBox<Khoa>();
        cboMonHoc = new JComboBox<MonHoc>();
        cboHocVi = createStringCombo(new String[]{"Cử nhân", "Thạc sĩ", "Tiến sĩ", "PGS", "GS"});
        cboTrangThai = createStringCombo(new String[]{"Đang giảng dạy", "Tạm nghỉ", "Nghỉ việc"});
        cboFilterKhoa = new JComboBox<Object>();
        cboFilterHocVi = createStringCombo(new String[]{"Tất cả", "Cử nhân", "Thạc sĩ", "Tiến sĩ", "PGS", "GS"});
        styleCombo(cboKhoa);
        styleCombo(cboMonHoc);
        styleCombo(cboFilterKhoa);

        int row = 0;
        addField(panel, row, 0, "Mã giảng viên", txtMaGV);
        addField(panel, row, 1, "Họ tên", txtHoTen);
        addField(panel, row++, 2, "Ngày sinh", txtNgaySinh);
        addField(panel, row, 0, "Giới tính", cboGioiTinh);
        addField(panel, row, 1, "Số điện thoại", txtSoDienThoai);
        addField(panel, row++, 2, "Email", txtEmail);
        addField(panel, row, 0, "Khoa", cboKhoa);
        addField(panel, row, 1, "Môn học", cboMonHoc);
        addField(panel, row++, 2, "Học vị", cboHocVi);
        addField(panel, row, 0, "Hệ số lương", txtHeSoLuong);
        addField(panel, row, 1, "Trạng thái", cboTrangThai);
        addField(panel, row, 2, "Địa chỉ", txtDiaChi);
        return panel;
    }

    private JPanel initTable() {
        String[] columns = {"Mã GV", "Họ tên", "Khoa", "Môn học", "Học vị", "Hệ số", "Lương"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
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
        panel.add(initFilterBar(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel initFilterBar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton btnTim = UIHelper.createButton("Tìm kiếm", new Color(14, 165, 233));
        JButton btnLoc = UIHelper.createButton("Lọc", UIHelper.WARNING);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 8);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtTimKiem, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(cboFilterKhoa, gbc);

        gbc.gridx = 2;
        panel.add(cboFilterHocVi, gbc);

        gbc.gridx = 3;
        panel.add(btnTim, gbc);

        gbc.gridx = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(btnLoc, gbc);

        btnTim.addActionListener(e -> loadTable(quanLyGiangVien.searchGiangVien(txtTimKiem.getText())));
        btnLoc.addActionListener(e -> applyFilter());
        return panel;
    }

    private JPanel initButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);
        JButton btnThem = UIHelper.createButton("Thêm", UIHelper.SUCCESS);
        JButton btnSua = UIHelper.createButton("Sửa", UIHelper.PRIMARY);
        JButton btnXoa = UIHelper.createButton("Xóa", UIHelper.DANGER);
        JButton btnLamMoi = UIHelper.createButton("Làm mới", new Color(100, 116, 139));

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);

        btnThem.addActionListener(e -> addGiangVien());
        btnSua.addActionListener(e -> updateGiangVien());
        btnXoa.addActionListener(e -> deleteGiangVien());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            reloadComboData();
            loadTable(quanLyGiangVien.getAll());
        });
        return panel;
    }

    public void reloadComboData() {
        Object selectedKhoa = cboKhoa == null ? null : cboKhoa.getSelectedItem();
        Object selectedMon = cboMonHoc == null ? null : cboMonHoc.getSelectedItem();

        cboKhoa.removeAllItems();
        for (Khoa khoa : quanLyKhoa.getAll()) {
            cboKhoa.addItem(khoa);
        }
        cboMonHoc.removeAllItems();
        for (MonHoc monHoc : quanLyMonHoc.getAll()) {
            cboMonHoc.addItem(monHoc);
        }
        cboFilterKhoa.removeAllItems();
        cboFilterKhoa.addItem("Tất cả");
        for (Khoa khoa : quanLyKhoa.getAll()) {
            cboFilterKhoa.addItem(khoa);
        }

        selectComboItem(cboKhoa, selectedKhoa);
        selectComboItem(cboMonHoc, selectedMon);
    }

    public void refreshData() {
        reloadComboData();
        loadTable(quanLyGiangVien.getAll());
    }

    private void addGiangVien() {
        GiangVien gv = createGiangVienFromForm(true);
        if (gv == null) {
            return;
        }
        if (quanLyGiangVien.addGiangVien(gv)) {
            afterDataChanged("Thêm giảng viên thành công");
            clearForm();
        } else {
            MessageDialog.showError(this, "Mã giảng viên đã tồn tại");
        }
    }

    private void updateGiangVien() {
        if (selectedMaGV == null) {
            MessageDialog.showError(this, "Vui lòng chọn giảng viên cần sửa");
            return;
        }
        GiangVien gv = createGiangVienFromForm(false);
        if (gv == null) {
            return;
        }
        if (quanLyGiangVien.updateGiangVien(selectedMaGV, gv)) {
            selectedMaGV = gv.getMaGV();
            afterDataChanged("Cập nhật giảng viên thành công");
        }
    }

    private void deleteGiangVien() {
        if (selectedMaGV == null) {
            MessageDialog.showError(this, "Vui lòng chọn giảng viên cần xóa");
            return;
        }
        if (!MessageDialog.confirm(this, "Bạn có chắc muốn xóa giảng viên này?")) {
            return;
        }
        if (quanLyGiangVien.deleteGiangVien(selectedMaGV)) {
            afterDataChanged("Xóa giảng viên thành công");
            clearForm();
        }
    }

    private GiangVien createGiangVienFromForm(boolean isAdd) {
        if (!validateForm(isAdd)) {
            return null;
        }
        Khoa khoa = (Khoa) cboKhoa.getSelectedItem();
        MonHoc monHoc = (MonHoc) cboMonHoc.getSelectedItem();
        double heSoLuong = Validator.parseDouble(txtHeSoLuong.getText());
        return new GiangVien(
                txtMaGV.getText().trim(),
                txtHoTen.getText().trim(),
                txtNgaySinh.getText().trim(),
                cboGioiTinh.getSelectedItem().toString(),
                txtDiaChi.getText().trim(),
                txtSoDienThoai.getText().trim(),
                txtEmail.getText().trim(),
                khoa.getMaKhoa(),
                khoa.getTenKhoa(),
                monHoc.getMaMon(),
                monHoc.getTenMon(),
                cboHocVi.getSelectedItem().toString(),
                heSoLuong,
                cboTrangThai.getSelectedItem().toString());
    }

    private boolean validateForm(boolean isAdd) {
        if (Validator.isEmpty(txtMaGV.getText()) || Validator.isEmpty(txtHoTen.getText())
                || Validator.isEmpty(txtNgaySinh.getText()) || Validator.isEmpty(txtDiaChi.getText())
                || Validator.isEmpty(txtSoDienThoai.getText()) || Validator.isEmpty(txtEmail.getText())
                || Validator.isEmpty(txtHeSoLuong.getText())) {
            MessageDialog.showError(this, "Vui lòng nhập đầy đủ thông tin giảng viên");
            return false;
        }
        if (cboKhoa.getItemCount() == 0) {
            MessageDialog.showError(this, "Vui lòng thêm khoa trước khi thêm giảng viên");
            return false;
        }
        if (cboMonHoc.getItemCount() == 0) {
            MessageDialog.showError(this, "Vui lòng thêm môn học trước khi thêm giảng viên");
            return false;
        }
        if (!Validator.isSoDienThoaiHopLe(txtSoDienThoai.getText().trim())) {
            MessageDialog.showError(this, "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0");
            return false;
        }
        if (!Validator.isEmailHopLe(txtEmail.getText().trim())) {
            MessageDialog.showError(this, "Email không hợp lệ");
            return false;
        }
        String maMoi = txtMaGV.getText().trim();
        if ((isAdd || !maMoi.equalsIgnoreCase(selectedMaGV)) && quanLyGiangVien.isDuplicateMa(maMoi)) {
            MessageDialog.showError(this, "Mã giảng viên không được trùng");
            return false;
        }
        try {
            double heSoLuong = Validator.parseDouble(txtHeSoLuong.getText());
            if (!Validator.isHeSoLuongHopLe(heSoLuong)) {
                MessageDialog.showError(this, "Hệ số lương phải lớn hơn 0");
                return false;
            }
        } catch (NumberFormatException ex) {
            MessageDialog.showError(this, "Hệ số lương phải là số");
            return false;
        }
        return true;
    }

    private void applyFilter() {
        Object khoaFilter = cboFilterKhoa.getSelectedItem();
        String hocViFilter = cboFilterHocVi.getSelectedItem().toString();
        ArrayList<GiangVien> ketQua = new ArrayList<GiangVien>();
        for (GiangVien gv : quanLyGiangVien.getAll()) {
            boolean matchKhoa = true;
            boolean matchHocVi = true;
            if (khoaFilter instanceof Khoa) {
                matchKhoa = ((Khoa) khoaFilter).getMaKhoa().equalsIgnoreCase(gv.getMaKhoa());
            }
            if (!"Tất cả".equals(hocViFilter)) {
                matchHocVi = hocViFilter.equalsIgnoreCase(gv.getHocVi());
            }
            if (matchKhoa && matchHocVi) {
                ketQua.add(gv);
            }
        }
        loadTable(ketQua);
    }

    private void afterDataChanged(String message) {
        loadTable(quanLyGiangVien.getAll());
        if (onDataChanged != null) {
            onDataChanged.run();
        }
        MessageDialog.showInfo(this, message);
    }

    private void loadTable(ArrayList<GiangVien> list) {
        tableModel.setRowCount(0);
        for (GiangVien gv : list) {
            tableModel.addRow(new Object[]{
                gv.getMaGV(),
                gv.getHoTen(),
                gv.getTenKhoa(),
                gv.getTenMonHoc(),
                gv.getHocVi(),
                gv.getHeSoLuong(),
                moneyFormat.format(gv.tinhLuong())
            });
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        selectedMaGV = tableModel.getValueAt(modelRow, 0).toString();
        for (GiangVien gv : quanLyGiangVien.getAll()) {
            if (selectedMaGV.equalsIgnoreCase(gv.getMaGV())) {
                fillForm(gv);
                break;
            }
        }
    }

    private void fillForm(GiangVien gv) {
        txtMaGV.setText(gv.getMaGV());
        txtHoTen.setText(gv.getHoTen());
        txtNgaySinh.setText(gv.getNgaySinh());
        cboGioiTinh.setSelectedItem(gv.getGioiTinh());
        txtDiaChi.setText(gv.getDiaChi());
        txtSoDienThoai.setText(gv.getSoDienThoai());
        txtEmail.setText(gv.getEmail());
        cboHocVi.setSelectedItem(gv.getHocVi());
        txtHeSoLuong.setText(String.valueOf(gv.getHeSoLuong()));
        cboTrangThai.setSelectedItem(gv.getTrangThai());
        selectKhoaByMa(gv.getMaKhoa());
        selectMonHocByMa(gv.getMaMonHoc());
    }

    private void clearForm() {
        selectedMaGV = null;
        txtMaGV.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtHeSoLuong.setText("");
        txtTimKiem.setText("");
        cboGioiTinh.setSelectedIndex(0);
        cboHocVi.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        if (cboKhoa.getItemCount() > 0) {
            cboKhoa.setSelectedIndex(0);
        }
        if (cboMonHoc.getItemCount() > 0) {
            cboMonHoc.setSelectedIndex(0);
        }
        table.clearSelection();
    }

    private JTextField createInput() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(190, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                new EmptyBorder(0, 10, 0, 10)));
        return field;
    }

    private JComboBox<String> createStringCombo(String[] values) {
        JComboBox<String> combo = new JComboBox<String>(values);
        styleCombo(combo);
        return combo;
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(190, 36));
        combo.setBackground(Color.WHITE);
    }

    private void addField(JPanel panel, int row, int column, String label, Component input) {
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

    private void selectKhoaByMa(String maKhoa) {
        for (int i = 0; i < cboKhoa.getItemCount(); i++) {
            Khoa khoa = cboKhoa.getItemAt(i);
            if (khoa.getMaKhoa().equalsIgnoreCase(maKhoa)) {
                cboKhoa.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectMonHocByMa(String maMonHoc) {
        for (int i = 0; i < cboMonHoc.getItemCount(); i++) {
            MonHoc monHoc = cboMonHoc.getItemAt(i);
            if (monHoc.getMaMon().equalsIgnoreCase(maMonHoc)) {
                cboMonHoc.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectComboItem(JComboBox<?> combo, Object oldItem) {
        if (oldItem == null) {
            return;
        }
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item.toString().equals(oldItem.toString())) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }
}
