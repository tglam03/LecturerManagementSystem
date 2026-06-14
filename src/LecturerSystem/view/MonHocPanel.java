package LecturerSystem.view;

import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.model.MonHoc;
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
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class MonHocPanel extends JPanel {

    private final QuanLyMonHoc quanLyMonHoc;
    private final QuanLyGiangVien quanLyGiangVien;
    private final Runnable onDataChanged;
    private JTextField txtMaMon;
    private JTextField txtTenMon;
    private JTextField txtSoTinChi;
    private JTextField txtTimKiem;
    private JTable table;
    private DefaultTableModel tableModel;
    private String selectedMaMon;

    // Các thuộc tính phân trang
    private final ArrayList<MonHoc> currentList = new ArrayList<>();
    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private JButton btnFirst, btnPrev, btnNext, btnLast;
    private JLabel lblPageInfo;

    public MonHocPanel(QuanLyMonHoc quanLyMonHoc, QuanLyGiangVien quanLyGiangVien, Runnable onDataChanged) {
        this.quanLyMonHoc = quanLyMonHoc;
        this.quanLyGiangVien = quanLyGiangVien;
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

        addField(panel, 0, 0, "Mã môn", txtMaMon);
        addField(panel, 0, 1, "Tên môn", txtTenMon);
        addField(panel, 1, 0, "Số tín chỉ", txtSoTinChi);
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
        DefaultTableCellRenderer leftHeaderRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setFont(label.getFont().deriveFont(Font.BOLD));

                return label;
            }
        };
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        table = new JTable(tableModel);
        UIHelper.styleTable(table);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromSelectedRow();
            }
        });
        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(0).setHeaderRenderer(leftHeaderRenderer);
        table.getColumnModel().getColumn(1).setHeaderRenderer(leftHeaderRenderer);
        TableColumn creditColumn = table.getColumnModel().getColumn(2);

        DefaultTableCellRenderer headerRenderer
                = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();

        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        creditColumn.setHeaderRenderer(headerRenderer);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(10, 10, 10, 10)));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

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

        btnThem.addActionListener(e -> addMonHoc());
        btnSua.addActionListener(e -> updateMonHoc());
        btnXoa.addActionListener(e -> deleteMonHoc());
        btnTim.addActionListener(e -> {
            currentPage = 1;
            String query = txtTimKiem.getText().trim();
            if (query.isEmpty()) {
                loadTable(quanLyMonHoc.getAll());
            } else {
                ArrayList<MonHoc> results = quanLyMonHoc.searchMonHoc(query);
                if (results.isEmpty()) {
                    MessageDialog.showInfo(this, "Không tìm thấy kết quả phù hợp!");
                    loadTable(results);
                } else {
                    loadTable(results);
                }
            }
        });
        btnLamMoi.addActionListener(e -> {
            currentPage = 1;
            clearForm();
            loadTable(quanLyMonHoc.getAll());
        });
        return panel;
    }

    private void addMonHoc() {
        if (!validateMonHoc()) {
            return;
        }
        String maMoi = txtMaMon.getText().trim();
        String tenMoi = txtTenMon.getText().trim();

        if (kiemTraTrungMaMonHoc(maMoi)) {
            MessageDialog.showError(this, "Mã môn học đã tồn tại.");
            txtMaMon.requestFocus();
            return;
        }
        if (quanLyMonHoc.isDuplicateTen(tenMoi)) {
            MessageDialog.showError(this, "Tên môn học đã tồn tại.");
            txtTenMon.requestFocus();
            return;
        }

        MonHoc monHoc = createMonHocFromForm();
        if (quanLyMonHoc.addMonHoc(monHoc)) {
            afterDataChanged("Thêm môn học thành công");
        } else {
            MessageDialog.showError(this, "Mã môn học đã tồn tại.");
            txtMaMon.requestFocus();
        }
    }

    private void updateMonHoc() {
        if (selectedMaMon == null) {
            MessageDialog.showError(this, "Vui lòng chọn môn học cần sửa");
            return;
        }
        if (!validateMonHoc()) {
            return;
        }
        String maMoi = txtMaMon.getText().trim();
        String tenMoi = txtTenMon.getText().trim();

        if (!maMoi.equalsIgnoreCase(selectedMaMon)) {
            int count = demSoGiangVienDayMonHoc(selectedMaMon);
            if (count > 0) {
                MessageDialog.showError(this, "Môn học " + selectedMaMon + " đang có " + count + " giảng viên đang giảng dạy. Không cho phép sửa mã môn. Phải chuyển giảng viên sang môn học khác trước.");
                txtMaMon.requestFocus();
                return;
            }
            if (kiemTraTrungMaMonHoc(maMoi)) {
                MessageDialog.showError(this, "Mã môn học đã tồn tại.");
                txtMaMon.requestFocus();
                return;
            }
        }

        if (quanLyMonHoc.isDuplicateTenForUpdate(selectedMaMon, tenMoi)) {
            MessageDialog.showError(this, "Tên môn học đã tồn tại.");
            txtTenMon.requestFocus();
            return;
        }

        MonHoc monHoc = createMonHocFromForm();
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

        int n = demSoGiangVienDayMonHoc(selectedMaMon);
        if (n > 0) {
            String tenMon = txtTenMon.getText().trim();
            String msg = tenMon + "\nĐang được phân công cho:\n" + n + " giảng viên\nKhông thể xóa.";
            MessageDialog.showError(this, msg);
            return;
        }

        if (!xacNhanXoa()) {
            return;
        }

        if (quanLyMonHoc.deleteMonHoc(selectedMaMon)) {
            afterDataChanged("Xóa môn học thành công");
            clearForm();
        }
    }

    private MonHoc createMonHocFromForm() {
        String maMoi = txtMaMon.getText().trim();
        String tenMoi = txtTenMon.getText().trim();
        int soTinChi = Integer.parseInt(txtSoTinChi.getText().trim());
        return new MonHoc(maMoi, tenMoi, soTinChi);
    }

    private boolean validateMonHoc() {
        String maMon = txtMaMon.getText().trim();
        String tenMon = txtTenMon.getText().trim();
        String soTinChiStr = txtSoTinChi.getText().trim();

        // Validate mã môn
        if (Validator.isEmpty(maMon)) {
            MessageDialog.showError(this, "Mã môn không được để trống.");
            txtMaMon.requestFocus();
            return false;
        }
        if (maMon.length() < 2 || maMon.length() > 10) {
            MessageDialog.showError(this, "Mã môn phải từ 2 đến 10 ký tự.");
            txtMaMon.requestFocus();
            return false;
        }

        // Validate tên môn
        if (Validator.isEmpty(tenMon)) {
            MessageDialog.showError(this, "Tên môn không được để trống.");
            txtTenMon.requestFocus();
            return false;
        }

        // Validate số tín chỉ
        if (Validator.isEmpty(soTinChiStr)) {
            MessageDialog.showError(this, "Số tín chỉ phải từ 1 đến 10.");
            txtSoTinChi.requestFocus();
            return false;
        }
        try {
            int soTinChi = Integer.parseInt(soTinChiStr);
            if (soTinChi < 1 || soTinChi > 10) {
                MessageDialog.showError(this, "Số tín chỉ phải từ 1 đến 10.");
                txtSoTinChi.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            MessageDialog.showError(this, "Số tín chỉ phải từ 1 đến 10.");
            txtSoTinChi.requestFocus();
            return false;
        }

        return true;
    }

    private boolean kiemTraTrungMaMonHoc(String maMon) {
        return quanLyMonHoc.isDuplicateMa(maMon);
    }

    private int demSoGiangVienDayMonHoc(String maMon) {
        int count = 0;
        if (quanLyGiangVien != null) {
            for (LecturerSystem.model.GiangVien gv : quanLyGiangVien.getAll()) {
                if (maMon.equalsIgnoreCase(gv.getMaMonHoc())) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean coGiangVienDayMonHoc(String maMon) {
        return demSoGiangVienDayMonHoc(maMon) > 0;
    }

    private boolean xacNhanXoa() {
        return MessageDialog.confirm(this, "Bạn có chắc chắn muốn xóa dữ liệu này?");
    }

    private void afterDataChanged(String message) {
        currentPage = 1;
        loadTable(quanLyMonHoc.getAll());
        if (onDataChanged != null) {
            onDataChanged.run();
        }
        MessageDialog.showInfo(this, message);
    }

    private void loadTable(ArrayList<MonHoc> list) {
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
            MonHoc monHoc = currentList.get(i);
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
