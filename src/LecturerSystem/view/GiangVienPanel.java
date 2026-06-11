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

    // Các thuộc tính phân trang
    private final ArrayList<GiangVien> currentList = new ArrayList<>();
    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private JButton btnFirst, btnPrev, btnNext, btnLast;
    private JLabel lblPageInfo;

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

        // Thanh phân trang ở phía dưới bảng
        JPanel paginationBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        paginationBar.setBackground(Color.WHITE);

        btnFirst = new JButton("|<");
        btnPrev = new JButton("<");
        btnNext = new JButton(">");
        btnLast = new JButton(">|");
        lblPageInfo = new JLabel("Trang 1 / 1");

        stylePageButton(btnFirst);
        stylePageButton(btnPrev);
        stylePageButton(btnNext);
        stylePageButton(btnLast);
        lblPageInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPageInfo.setForeground(UIHelper.TEXT_DARK);

        btnFirst.addActionListener(e -> {
            currentPage = 1;
            loadTable(currentList);
        });
        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadTable(currentList);
            }
        });
        btnNext.addActionListener(e -> {
            int totalPages = (int) Math.ceil((double) currentList.size() / rowsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                loadTable(currentList);
            }
        });
        btnLast.addActionListener(e -> {
            int totalPages = (int) Math.ceil((double) currentList.size() / rowsPerPage);
            currentPage = Math.max(1, totalPages);
            loadTable(currentList);
        });

        paginationBar.add(btnFirst);
        paginationBar.add(btnPrev);
        paginationBar.add(lblPageInfo);
        paginationBar.add(btnNext);
        paginationBar.add(btnLast);

        panel.add(paginationBar, BorderLayout.SOUTH);
        return panel;
    }

    private void stylePageButton(JButton button) {
        button.setPreferredSize(new Dimension(45, 36));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(Color.WHITE);
        button.setForeground(UIHelper.TEXT_DARK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        btnTim.addActionListener(e -> {
            currentPage = 1;
            String query = txtTimKiem.getText().trim();
            if (query.isEmpty()) {
                loadTable(quanLyGiangVien.getAll());
            } else {
                ArrayList<GiangVien> results = quanLyGiangVien.searchGiangVien(query);
                if (results.isEmpty()) {
                    MessageDialog.showInfo(this, "Không tìm thấy kết quả phù hợp!");
                    loadTable(results);
                } else {
                    loadTable(results);
                }
            }
        });
        btnLoc.addActionListener(e -> {
            currentPage = 1;
            applyFilter();
        });
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
            currentPage = 1;
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
        if (!validateGiangVien()) {
            return;
        }
        String maMoi = txtMaGV.getText().trim();
        if (kiemTraTrungMaGiangVien(maMoi)) {
            MessageDialog.showError(this, "Mã giảng viên đã tồn tại.");
            txtMaGV.requestFocus();
            return;
        }

        GiangVien gv = createGiangVienFromForm();
        if (quanLyGiangVien.addGiangVien(gv)) {
            afterDataChanged("Thêm giảng viên thành công");
            clearForm();
        } else {
            MessageDialog.showError(this, "Mã giảng viên đã tồn tại.");
            txtMaGV.requestFocus();
        }
    }

    private void updateGiangVien() {
        if (selectedMaGV == null) {
            MessageDialog.showError(this, "Vui lòng chọn giảng viên cần sửa");
            return;
        }
        if (!validateGiangVien()) {
            return;
        }
        String maMoi = txtMaGV.getText().trim();
        if (!maMoi.equalsIgnoreCase(selectedMaGV) && kiemTraTrungMaGiangVien(maMoi)) {
            MessageDialog.showError(this, "Mã giảng viên đã tồn tại.");
            txtMaGV.requestFocus();
            return;
        }

        GiangVien gv = createGiangVienFromForm();
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
        GiangVien target = null;
        for (GiangVien gv : quanLyGiangVien.getAll()) {
            if (selectedMaGV.equalsIgnoreCase(gv.getMaGV())) {
                target = gv;
                break;
            }
        }
        if (target == null) {
            MessageDialog.showError(this, "Không tìm thấy giảng viên cần xóa");
            return;
        }
        if (!xacNhanXoa(target)) {
            return;
        }
        if (quanLyGiangVien.deleteGiangVien(selectedMaGV)) {
            afterDataChanged("Xóa giảng viên thành công");
            clearForm();
        }
    }

    private GiangVien createGiangVienFromForm() {
        Khoa khoa = (Khoa) cboKhoa.getSelectedItem();
        MonHoc monHoc = (MonHoc) cboMonHoc.getSelectedItem();
        double heSoLuong = Double.parseDouble(txtHeSoLuong.getText().trim());
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

    private boolean validateGiangVien() {
        String maGV = txtMaGV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String ngaySinhStr = txtNgaySinh.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();
        String hslStr = txtHeSoLuong.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        // 1. Mã giảng viên
        if (Validator.isEmpty(maGV)) {
            MessageDialog.showError(this, "Mã giảng viên không được để trống.");
            txtMaGV.requestFocus();
            return false;
        }
        if (!maGV.matches("^[A-Za-z0-9]{3,10}$")) {
            MessageDialog.showError(this, "Mã giảng viên không hợp lệ.");
            txtMaGV.requestFocus();
            return false;
        }

        // 2. Họ tên
        if (Validator.isEmpty(hoTen) || !hoTen.matches("^[\\p{L}\\s]{2,50}$")) {
            MessageDialog.showError(this, "Họ tên không hợp lệ.");
            txtHoTen.requestFocus();
            return false;
        }

        // 3. Ngày sinh
        if (Validator.isEmpty(ngaySinhStr)) {
            MessageDialog.showError(this, "Ngày sinh không được để trống.");
            txtNgaySinh.requestFocus();
            return false;
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        java.util.Date ngaySinh;
        try {
            ngaySinh = sdf.parse(ngaySinhStr);
        } catch (Exception e) {
            MessageDialog.showError(this, "Ngày sinh không hợp lệ (định dạng đúng: dd/MM/yyyy).");
            txtNgaySinh.requestFocus();
            return false;
        }
        java.util.Date current = new java.util.Date();
        if (ngaySinh.after(current)) {
            MessageDialog.showError(this, "Ngày sinh không được lớn hơn ngày hiện tại.");
            txtNgaySinh.requestFocus();
            return false;
        }
        java.util.Calendar dob = java.util.Calendar.getInstance();
        dob.setTime(ngaySinh);
        java.util.Calendar today = java.util.Calendar.getInstance();
        int age = today.get(java.util.Calendar.YEAR) - dob.get(java.util.Calendar.YEAR);
        if (today.get(java.util.Calendar.DAY_OF_YEAR) < dob.get(java.util.Calendar.DAY_OF_YEAR)) {
            age--;
        }
        if (age < 22 || age > 70) {
            MessageDialog.showError(this, "Tuổi giảng viên phải từ 22 đến 70.");
            txtNgaySinh.requestFocus();
            return false;
        }

        // 4. Giới tính
        if (cboGioiTinh.getSelectedItem() == null) {
            MessageDialog.showError(this, "Vui lòng chọn giới tính.");
            cboGioiTinh.requestFocus();
            return false;
        }

        // 5. Số điện thoại
        if (Validator.isEmpty(sdt)) {
            MessageDialog.showError(this, "Số điện thoại phải gồm 10 chữ số.");
            txtSoDienThoai.requestFocus();
            return false;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            MessageDialog.showError(this, "Số điện thoại phải gồm 10 chữ số.");
            txtSoDienThoai.requestFocus();
            return false;
        }

        // 6. Email
        if (Validator.isEmpty(email)) {
            MessageDialog.showError(this, "Email không đúng định dạng.");
            txtEmail.requestFocus();
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            MessageDialog.showError(this, "Email không đúng định dạng.");
            txtEmail.requestFocus();
            return false;
        }

        // 7. Khoa
        if (cboKhoa.getSelectedItem() == null || cboKhoa.getItemCount() == 0) {
            MessageDialog.showError(this, "Vui lòng chọn khoa.");
            cboKhoa.requestFocus();
            return false;
        }

        // 8. Môn học
        if (cboMonHoc.getSelectedItem() == null || cboMonHoc.getItemCount() == 0) {
            MessageDialog.showError(this, "Vui lòng chọn môn học.");
            cboMonHoc.requestFocus();
            return false;
        }

        // 9. Học vị
        if (cboHocVi.getSelectedItem() == null) {
            MessageDialog.showError(this, "Vui lòng chọn học vị.");
            cboHocVi.requestFocus();
            return false;
        }

        // 10. Hệ số lương
        if (Validator.isEmpty(hslStr)) {
            MessageDialog.showError(this, "Hệ số lương không hợp lệ.");
            txtHeSoLuong.requestFocus();
            return false;
        }
        try {
            double hsl = Double.parseDouble(hslStr);
            if (hsl <= 0 || hsl > 30) {
                MessageDialog.showError(this, "Hệ số lương không hợp lệ.");
                txtHeSoLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            MessageDialog.showError(this, "Hệ số lương không hợp lệ.");
            txtHeSoLuong.requestFocus();
            return false;
        }

        // 11. Trạng thái
        if (cboTrangThai.getSelectedItem() == null) {
            MessageDialog.showError(this, "Vui lòng chọn trạng thái.");
            cboTrangThai.requestFocus();
            return false;
        }

        // 12. Địa chỉ
        if (!diaChi.isEmpty() && diaChi.length() > 200) {
            MessageDialog.showError(this, "Địa chỉ không được vượt quá 200 ký tự.");
            txtDiaChi.requestFocus();
            return false;
        }

        return true;
    }

    private boolean kiemTraTrungMaGiangVien(String maGV) {
        return quanLyGiangVien.isDuplicateMa(maGV);
    }

    private boolean xacNhanXoa(GiangVien gv) {
        String msg = "Giảng viên:\n" + gv.getHoTen() + "\n\nMã:\n" + gv.getMaGV() + "\n\nBạn có chắc muốn xóa?";
        return MessageDialog.confirm(this, msg);
    }

    private void applyFilter() {
        currentPage = 1;
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
        currentPage = 1;
        loadTable(quanLyGiangVien.getAll());
        if (onDataChanged != null) {
            onDataChanged.run();
        }
        MessageDialog.showInfo(this, message);
    }

    private void loadTable(ArrayList<GiangVien> list) {
        if (list != currentList) {
            currentList.clear();
            currentList.addAll(list);
        }

        int totalRows = currentList.size();
        int totalPages = (int) Math.ceil((double) totalRows / rowsPerPage);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        lblPageInfo.setText("Trang " + currentPage + " / " + totalPages);
        btnFirst.setEnabled(currentPage > 1);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        btnLast.setEnabled(currentPage < totalPages);

        tableModel.setRowCount(0);
        int startIndex = (currentPage - 1) * rowsPerPage;
        int endIndex = Math.min(startIndex + rowsPerPage, totalRows);

        for (int i = startIndex; i < endIndex; i++) {
            GiangVien gv = currentList.get(i);
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
