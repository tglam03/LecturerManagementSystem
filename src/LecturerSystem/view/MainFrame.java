package LecturerSystem.view;

import LecturerSystem.file.FileUtil;
import LecturerSystem.model.GiangVien;
import LecturerSystem.model.GiangVienCoHuu;
import LecturerSystem.model.GiangVienThinhGiang;
import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.utils.MessageDialog;
import LecturerSystem.utils.Validator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MainFrame extends JFrame {
    private static final Color BG = new Color(245, 247, 251);
    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color SUCCESS = new Color(22, 163, 74);
    private static final Color DANGER = new Color(220, 38, 38);
    private static final Color WARNING = new Color(234, 88, 12);
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);

    private final QuanLyGiangVien quanLyGiangVien;
    private final DecimalFormat moneyFormat;

    private JTextField txtMaGiangVien;
    private JTextField txtHoTen;
    private JTextField txtNgaySinh;
    private JTextField txtDiaChi;
    private JTextField txtSoDienThoai;
    private JTextField txtKhoa;
    private JTextField txtMonDay;
    private JTextField txtHeSoLuong;
    private JTextField txtSoGioDay;
    private JTextField txtTienMoiGio;
    private JTextField txtTimKiem;
    private JComboBox<String> cboHocVi;
    private JComboBox<String> cboLoaiGiangVien;
    private JTable tblGiangVien;
    private DefaultTableModel tableModel;
    private String selectedMaGiangVien;

    public MainFrame() {
        this(new QuanLyGiangVien());
    }

    public MainFrame(QuanLyGiangVien quanLyGiangVien) {
        this.quanLyGiangVien = quanLyGiangVien == null ? new QuanLyGiangVien() : quanLyGiangVien;
        moneyFormat = new DecimalFormat("#,###");
        initComponents();
        loadTable(this.quanLyGiangVien.getAll());
    }

    private void initComponents() {
        setTitle("Quản lý giảng viên");
        setSize(1240, 760);
        setMinimumSize(new Dimension(1060, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(14, 14));
        getContentPane().setBackground(BG);
        setJMenuBar(createMenuBar());

        add(initHeader(), BorderLayout.NORTH);
        add(initTable(), BorderLayout.CENTER);
        add(initBottomPanel(), BorderLayout.SOUTH);
        updateInputState();
    }

    private JPanel initHeader() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 22, 0, 22));

        JLabel title = new JLabel("Quản lý giảng viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Thêm, sửa, xóa, tìm kiếm và lưu dữ liệu giảng viên");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_MUTED);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(initForm(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel initForm() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(16, 18, 16, 18)));

        txtMaGiangVien = createInput();
        txtHoTen = createInput();
        txtNgaySinh = createInput();
        txtDiaChi = createInput();
        txtSoDienThoai = createInput();
        txtKhoa = createInput();
        txtMonDay = createInput();
        txtHeSoLuong = createInput();
        txtSoGioDay = createInput();
        txtTienMoiGio = createInput();
        cboHocVi = createCombo(new String[]{"Cử nhân", "Thạc sĩ", "Tiến sĩ", "PGS", "GS"});
        cboLoaiGiangVien = createCombo(new String[]{"Cơ hữu", "Thỉnh giảng"});

        int row = 0;
        addFormField(card, row, 0, "Mã giảng viên", txtMaGiangVien);
        addFormField(card, row++, 1, "Họ tên", txtHoTen);
        addFormField(card, row, 0, "Ngày sinh", txtNgaySinh);
        addFormField(card, row++, 1, "Số điện thoại", txtSoDienThoai);
        addFormField(card, row, 0, "Khoa", txtKhoa);
        addFormField(card, row++, 1, "Học vị", cboHocVi);
        addFormField(card, row, 0, "Môn dạy", txtMonDay);
        addFormField(card, row++, 1, "Loại giảng viên", cboLoaiGiangVien);
        addFormField(card, row, 0, "Hệ số lương", txtHeSoLuong);
        addFormField(card, row++, 1, "Số giờ dạy", txtSoGioDay);
        addFormField(card, row, 0, "Tiền mỗi giờ", txtTienMoiGio);
        addFormField(card, row, 1, "Địa chỉ", txtDiaChi);

        cboLoaiGiangVien.addActionListener(e -> updateInputState());
        return card;
    }

    private JPanel initTable() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 22, 0, 22));

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        txtTimKiem = createInput();
        JButton btnTimKiem = createButton("Tìm kiếm", PRIMARY);
        searchPanel.add(txtTimKiem, BorderLayout.CENTER);
        searchPanel.add(btnTimKiem, BorderLayout.EAST);

        String[] columns = {"Mã", "Họ tên", "Ngày sinh", "Địa chỉ", "SĐT", "Khoa",
            "Học vị", "Môn dạy", "Hệ số", "Số giờ", "Tiền/giờ", "Loại", "Lương"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblGiangVien = new JTable(tableModel);
        tblGiangVien.setAutoCreateRowSorter(true);
        styleTable(tblGiangVien);
        tblGiangVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromSelectedRow();
            }
        });

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(10, 10, 10, 10)));
        tableCard.add(new JScrollPane(tblGiangVien), BorderLayout.CENTER);

        btnTimKiem.addActionListener(e -> searchGiangVien());
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel initBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(0, 18, 12, 18));

        JButton btnThem = createButton("Thêm", SUCCESS);
        JButton btnSua = createButton("Sửa", PRIMARY);
        JButton btnXoa = createButton("Xóa", DANGER);
        JButton btnLamMoi = createButton("Làm mới", new Color(71, 85, 105));
        JButton btnLuuFile = createButton("Lưu file", WARNING);
        JButton btnDocFile = createButton("Đọc file", new Color(14, 165, 233));
        JButton btnThongKe = createButton("Thống kê", new Color(147, 51, 234));
        JButton btnThoat = createButton("Thoát", new Color(100, 116, 139));

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        panel.add(btnLuuFile);
        panel.add(btnDocFile);
        panel.add(btnThongKe);
        panel.add(btnThoat);

        btnThem.addActionListener(e -> addGiangVien());
        btnSua.addActionListener(e -> updateGiangVien());
        btnXoa.addActionListener(e -> deleteGiangVien());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadTable(quanLyGiangVien.getAll());
        });
        btnLuuFile.addActionListener(e -> saveFile());
        btnDocFile.addActionListener(e -> readFile());
        btnThongKe.addActionListener(e -> showThongKe());
        btnThoat.addActionListener(e -> dispose());
        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        JMenu menuHeThong = new JMenu("Hệ thống");
        JMenuItem itemLuu = new JMenuItem("Lưu file");
        JMenuItem itemDoc = new JMenuItem("Đọc file");
        JMenuItem itemThoat = new JMenuItem("Thoát");
        itemLuu.addActionListener(e -> saveFile());
        itemDoc.addActionListener(e -> readFile());
        itemThoat.addActionListener(e -> dispose());
        menuHeThong.add(itemLuu);
        menuHeThong.add(itemDoc);
        menuHeThong.addSeparator();
        menuHeThong.add(itemThoat);

        JMenu menuQuanLy = new JMenu("Quản lý");
        JMenuItem itemThem = new JMenuItem("Thêm");
        JMenuItem itemSua = new JMenuItem("Sửa");
        JMenuItem itemXoa = new JMenuItem("Xóa");
        itemThem.addActionListener(e -> addGiangVien());
        itemSua.addActionListener(e -> updateGiangVien());
        itemXoa.addActionListener(e -> deleteGiangVien());
        menuQuanLy.add(itemThem);
        menuQuanLy.add(itemSua);
        menuQuanLy.add(itemXoa);

        JMenu menuTimKiem = new JMenu("Tìm kiếm");
        JMenuItem itemTimKiem = new JMenuItem("Tìm kiếm giảng viên");
        itemTimKiem.addActionListener(e -> searchGiangVien());
        menuTimKiem.add(itemTimKiem);

        JMenu menuSapXep = new JMenu("Sắp xếp");
        JMenuItem itemSapXepTen = new JMenuItem("Theo tên");
        JMenuItem itemSapXepLuong = new JMenuItem("Theo lương");
        itemSapXepTen.addActionListener(e -> sortByName());
        itemSapXepLuong.addActionListener(e -> sortBySalary());
        menuSapXep.add(itemSapXepTen);
        menuSapXep.add(itemSapXepLuong);

        JMenu menuThongKe = new JMenu("Thống kê");
        JMenuItem itemThongKe = new JMenuItem("Xem thống kê");
        itemThongKe.addActionListener(e -> showThongKe());
        menuThongKe.add(itemThongKe);

        menuBar.add(menuHeThong);
        menuBar.add(menuQuanLy);
        menuBar.add(menuTimKiem);
        menuBar.add(menuSapXep);
        menuBar.add(menuThongKe);
        return menuBar;
    }

    private void addFormField(JPanel panel, int row, int column, String label, Component input) {
        int startCol = column * 2;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.gridy = row;
        gbc.gridx = startCol;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_DARK);
        panel.add(lbl, gbc);

        gbc.gridx = startCol + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(input, gbc);
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

    private JComboBox<String> createCombo(String[] values) {
        JComboBox<String> combo = new JComboBox<String>(values);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(190, 36));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(112, 38));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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

    private void addGiangVien() {
        try {
            if (!validateForm(true)) {
                return;
            }
            GiangVien gv = createGiangVienFromForm();
            if (quanLyGiangVien.addGiangVien(gv)) {
                loadTable(quanLyGiangVien.getAll());
                clearForm();
                MessageDialog.showInfo(this, "Thêm giảng viên thành công");
            } else {
                MessageDialog.showError(this, "Mã giảng viên đã tồn tại");
            }
        } catch (NumberFormatException ex) {
            MessageDialog.showError(this, "Hệ số lương, số giờ dạy hoặc tiền mỗi giờ không hợp lệ");
        }
    }

    private void updateGiangVien() {
        if (selectedMaGiangVien == null) {
            MessageDialog.showError(this, "Vui lòng chọn giảng viên cần sửa");
            return;
        }
        try {
            if (!validateForm(false)) {
                return;
            }
            GiangVien gv = createGiangVienFromForm();
            if (quanLyGiangVien.updateGiangVien(selectedMaGiangVien, gv)) {
                selectedMaGiangVien = gv.getMaGiangVien();
                loadTable(quanLyGiangVien.getAll());
                MessageDialog.showInfo(this, "Cập nhật giảng viên thành công");
            } else {
                MessageDialog.showError(this, "Không tìm thấy giảng viên cần sửa");
            }
        } catch (NumberFormatException ex) {
            MessageDialog.showError(this, "Hệ số lương, số giờ dạy hoặc tiền mỗi giờ không hợp lệ");
        }
    }

    private void deleteGiangVien() {
        if (selectedMaGiangVien == null) {
            MessageDialog.showError(this, "Vui lòng chọn giảng viên cần xóa");
            return;
        }
        if (!MessageDialog.confirm(this, "Bạn có chắc muốn xóa giảng viên này?")) {
            return;
        }
        if (quanLyGiangVien.deleteGiangVien(selectedMaGiangVien)) {
            loadTable(quanLyGiangVien.getAll());
            clearForm();
            MessageDialog.showInfo(this, "Xóa giảng viên thành công");
        }
    }

    private boolean validateForm(boolean isAdd) {
        // Kiểm tra chung trước, sau đó kiểm tra riêng theo loại giảng viên.
        if (Validator.isEmpty(txtMaGiangVien.getText())
                || Validator.isEmpty(txtHoTen.getText())
                || Validator.isEmpty(txtNgaySinh.getText())
                || Validator.isEmpty(txtDiaChi.getText())
                || Validator.isEmpty(txtSoDienThoai.getText())
                || Validator.isEmpty(txtKhoa.getText())
                || Validator.isEmpty(txtMonDay.getText())) {
            MessageDialog.showError(this, "Vui lòng nhập đầy đủ thông tin bắt buộc");
            return false;
        }

        if (!Validator.isSoDienThoaiHopLe(txtSoDienThoai.getText().trim())) {
            MessageDialog.showError(this, "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0");
            return false;
        }

        String maMoi = txtMaGiangVien.getText().trim();
        if ((isAdd || !maMoi.equalsIgnoreCase(selectedMaGiangVien))
                && Validator.isMaGiangVienTrung(maMoi, quanLyGiangVien)) {
            MessageDialog.showError(this, "Mã giảng viên không được trùng");
            return false;
        }

        if (isCoHuu()) {
            double heSoLuong = Validator.parseDouble(txtHeSoLuong.getText());
            if (!Validator.isLuongHopLe(heSoLuong) || heSoLuong == 0) {
                MessageDialog.showError(this, "Hệ số lương phải lớn hơn 0");
                return false;
            }
        } else {
            int soGioDay = Validator.parseInt(txtSoGioDay.getText());
            double tienMoiGio = Validator.parseDouble(txtTienMoiGio.getText());
            if (!Validator.isSoNguyenKhongAm(soGioDay) || soGioDay == 0
                    || !Validator.isLuongHopLe(tienMoiGio) || tienMoiGio == 0) {
                MessageDialog.showError(this, "Số giờ dạy và tiền mỗi giờ phải lớn hơn 0");
                return false;
            }
        }
        return true;
    }

    private GiangVien createGiangVienFromForm() {
        String ma = txtMaGiangVien.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String ngaySinh = txtNgaySinh.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        String khoa = txtKhoa.getText().trim();
        String monDay = txtMonDay.getText().trim();

        if (isCoHuu()) {
            double heSoLuong = Validator.parseDouble(txtHeSoLuong.getText());
            String hocVi = cboHocVi.getSelectedItem().toString();
            return new GiangVienCoHuu(ma, hoTen, ngaySinh, diaChi, soDienThoai, khoa, monDay, heSoLuong, hocVi);
        }

        int soGioDay = Validator.parseInt(txtSoGioDay.getText());
        double tienMoiGio = Validator.parseDouble(txtTienMoiGio.getText());
        return new GiangVienThinhGiang(ma, hoTen, ngaySinh, diaChi, soDienThoai, khoa, monDay, soGioDay, tienMoiGio);
    }

    private void loadTable(ArrayList<GiangVien> danhSach) {
        tableModel.setRowCount(0);
        for (GiangVien gv : danhSach) {
            String hocVi = "";
            String heSoLuong = "";
            String soGioDay = "";
            String tienMoiGio = "";

            if (gv instanceof GiangVienCoHuu) {
                GiangVienCoHuu gvCoHuu = (GiangVienCoHuu) gv;
                hocVi = gvCoHuu.getHocVi();
                heSoLuong = String.valueOf(gvCoHuu.getHeSoLuong());
            } else if (gv instanceof GiangVienThinhGiang) {
                GiangVienThinhGiang gvThinhGiang = (GiangVienThinhGiang) gv;
                soGioDay = String.valueOf(gvThinhGiang.getSoGioDay());
                tienMoiGio = moneyFormat.format(gvThinhGiang.getTienMoiGio());
            }

            tableModel.addRow(new Object[]{
                gv.getMaGiangVien(),
                gv.getHoTen(),
                gv.getNgaySinh(),
                gv.getDiaChi(),
                gv.getSoDienThoai(),
                gv.getKhoa(),
                hocVi,
                gv.getMonDay(),
                heSoLuong,
                soGioDay,
                tienMoiGio,
                gv.getLoaiGiangVien(),
                moneyFormat.format(gv.tinhLuong())
            });
        }
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = tblGiangVien.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        int modelRow = tblGiangVien.convertRowIndexToModel(selectedRow);
        selectedMaGiangVien = tableModel.getValueAt(modelRow, 0).toString();

        for (GiangVien gv : quanLyGiangVien.getAll()) {
            if (gv.getMaGiangVien().equalsIgnoreCase(selectedMaGiangVien)) {
                fillForm(gv);
                break;
            }
        }
    }

    private void fillForm(GiangVien gv) {
        txtMaGiangVien.setText(gv.getMaGiangVien());
        txtHoTen.setText(gv.getHoTen());
        txtNgaySinh.setText(gv.getNgaySinh());
        txtDiaChi.setText(gv.getDiaChi());
        txtSoDienThoai.setText(gv.getSoDienThoai());
        txtKhoa.setText(gv.getKhoa());
        txtMonDay.setText(gv.getMonDay());

        if (gv instanceof GiangVienCoHuu) {
            GiangVienCoHuu gvCoHuu = (GiangVienCoHuu) gv;
            cboLoaiGiangVien.setSelectedItem("Cơ hữu");
            cboHocVi.setSelectedItem(gvCoHuu.getHocVi());
            txtHeSoLuong.setText(String.valueOf(gvCoHuu.getHeSoLuong()));
            txtSoGioDay.setText("");
            txtTienMoiGio.setText("");
        } else if (gv instanceof GiangVienThinhGiang) {
            GiangVienThinhGiang gvThinhGiang = (GiangVienThinhGiang) gv;
            cboLoaiGiangVien.setSelectedItem("Thỉnh giảng");
            txtHeSoLuong.setText("");
            txtSoGioDay.setText(String.valueOf(gvThinhGiang.getSoGioDay()));
            txtTienMoiGio.setText(String.valueOf(gvThinhGiang.getTienMoiGio()));
        }
        updateInputState();
    }

    private void searchGiangVien() {
        loadTable(quanLyGiangVien.searchGiangVien(txtTimKiem.getText()));
    }

    private void sortByName() {
        quanLyGiangVien.sortByName();
        loadTable(quanLyGiangVien.getAll());
    }

    private void sortBySalary() {
        quanLyGiangVien.sortBySalary();
        loadTable(quanLyGiangVien.getAll());
    }

    private void saveFile() {
        try {
            FileUtil.saveToFile(quanLyGiangVien.getAll(), FileUtil.DEFAULT_FILE);
            MessageDialog.showInfo(this, "Lưu file thành công: " + FileUtil.DEFAULT_FILE);
        } catch (IOException ex) {
            MessageDialog.showError(this, "Không thể lưu file: " + ex.getMessage());
        }
    }

    private void readFile() {
        try {
            quanLyGiangVien.setDanhSachGiangVien(FileUtil.readFromFile(FileUtil.DEFAULT_FILE));
            loadTable(quanLyGiangVien.getAll());
            clearForm();
            MessageDialog.showInfo(this, "Đọc file thành công: " + FileUtil.DEFAULT_FILE);
        } catch (IOException ex) {
            MessageDialog.showError(this, "Không thể đọc file: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            MessageDialog.showError(this, "Dữ liệu trong file không hợp lệ");
        }
    }

    private void showThongKe() {
        new ThongKeFrame(quanLyGiangVien).setVisible(true);
    }

    private void clearForm() {
        selectedMaGiangVien = null;
        txtMaGiangVien.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtSoDienThoai.setText("");
        txtKhoa.setText("");
        txtMonDay.setText("");
        txtHeSoLuong.setText("");
        txtSoGioDay.setText("");
        txtTienMoiGio.setText("");
        txtTimKiem.setText("");
        cboHocVi.setSelectedIndex(0);
        cboLoaiGiangVien.setSelectedIndex(0);
        tblGiangVien.clearSelection();
        updateInputState();
    }

    private void updateInputState() {
        boolean coHuu = isCoHuu();
        cboHocVi.setEnabled(coHuu);
        txtHeSoLuong.setEnabled(coHuu);
        txtSoGioDay.setEnabled(!coHuu);
        txtTienMoiGio.setEnabled(!coHuu);
    }

    private boolean isCoHuu() {
        return "Cơ hữu".equals(cboLoaiGiangVien.getSelectedItem());
    }
}
