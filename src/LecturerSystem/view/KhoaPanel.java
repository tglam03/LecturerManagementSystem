package LecturerSystem.view;

import LecturerSystem.service.QuanLyGiangVien;
import LecturerSystem.model.Khoa;
import LecturerSystem.service.QuanLyKhoa;
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

public class KhoaPanel extends JPanel {

    private final QuanLyKhoa quanLyKhoa;
    private final QuanLyGiangVien quanLyGiangVien;
    private final Runnable onDataChanged;
    private JTextField txtMaKhoa;
    private JTextField txtTenKhoa;
    private JTextField txtTimKiem;
    private JTable table;
    private DefaultTableModel tableModel;
    private String selectedMaKhoa;

    // Các thuộc tính phân trang
    private final ArrayList<Khoa> currentList = new ArrayList<>();
    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private JButton btnFirst, btnPrev, btnNext, btnLast;
    private JLabel lblPageInfo;

    public KhoaPanel(QuanLyKhoa quanLyKhoa, QuanLyGiangVien quanLyGiangVien, Runnable onDataChanged) {
        this.quanLyKhoa = quanLyKhoa;
        this.quanLyGiangVien = quanLyGiangVien;
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
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.LEFT); // căn trái
                return c;
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
        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        // SỬA Ở ĐÂY: Đổi màu viền khung Table sang xám đậm hơn
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(156, 163, 175), 1),
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

        btnThem.addActionListener(e -> addKhoa());
        btnSua.addActionListener(e -> updateKhoa());
        btnXoa.addActionListener(e -> deleteKhoa());
        btnTim.addActionListener(e -> {
            currentPage = 1;
            String query = txtTimKiem.getText().trim();
            if (query.isEmpty()) {
                loadTable(quanLyKhoa.getAll());
            } else {
                ArrayList<Khoa> results = quanLyKhoa.searchKhoa(query);
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
            loadTable(quanLyKhoa.getAll());
        });

        return panel;
    }

    private void addKhoa() {
        if (!validateKhoa()) {
            return;
        }
        String maMoi = txtMaKhoa.getText().trim();
        String tenMoi = txtTenKhoa.getText().trim();

        if (kiemTraTrungMaKhoa(maMoi)) {
            MessageDialog.showError(this, "Mã khoa đã tồn tại.");
            txtMaKhoa.requestFocus();
            return;
        }
        if (quanLyKhoa.isDuplicateTen(tenMoi)) {
            MessageDialog.showError(this, "Tên khoa đã tồn tại.");
            txtTenKhoa.requestFocus();
            return;
        }

        Khoa khoa = new Khoa(maMoi, tenMoi);
        if (quanLyKhoa.addKhoa(khoa)) {
            afterDataChanged("Thêm khoa thành công");
        } else {
            MessageDialog.showError(this, "Mã khoa đã tồn tại.");
            txtMaKhoa.requestFocus();
        }
    }

    private void updateKhoa() {
        if (selectedMaKhoa == null) {
            MessageDialog.showError(this, "Vui lòng chọn khoa cần sửa");
            return;
        }
        if (!validateKhoa()) {
            return;
        }
        String maMoi = txtMaKhoa.getText().trim();
        String tenMoi = txtTenKhoa.getText().trim();

        if (!maMoi.equalsIgnoreCase(selectedMaKhoa)) {
            int count = demSoGiangVienThuocKhoa(selectedMaKhoa);
            if (count > 0) {
                MessageDialog.showError(this, "Khoa " + selectedMaKhoa + " đang có " + count + " giảng viên. Không cho phép sửa mã khoa. Phải chuyển toàn bộ giảng viên sang khoa khác trước.");
                txtMaKhoa.requestFocus();
                return;
            }
            if (kiemTraTrungMaKhoa(maMoi)) {
                MessageDialog.showError(this, "Mã khoa đã tồn tại.");
                txtMaKhoa.requestFocus();
                return;
            }
        }

        if (quanLyKhoa.isDuplicateTenForUpdate(selectedMaKhoa, tenMoi)) {
            MessageDialog.showError(this, "Tên khoa đã tồn tại.");
            txtTenKhoa.requestFocus();
            return;
        }

        Khoa khoa = new Khoa(maMoi, tenMoi);
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

        int n = demSoGiangVienThuocKhoa(selectedMaKhoa);
        if (n > 0) {
            String tenKhoa = txtTenKhoa.getText().trim();
            String msg = selectedMaKhoa + " - " + tenKhoa + "\nSố giảng viên:\n" + n + "\nKhông thể xóa.";
            MessageDialog.showError(this, msg);
            return;
        }

        if (!xacNhanXoa()) {
            return;
        }

        if (quanLyKhoa.deleteKhoa(selectedMaKhoa)) {
            afterDataChanged("Xóa khoa thành công");
            clearForm();
        }
    }

    private boolean validateKhoa() {
        String maKhoa = txtMaKhoa.getText().trim();
        String tenKhoa = txtTenKhoa.getText().trim();

        // Validate mã khoa
        if (Validator.isEmpty(maKhoa)) {
            MessageDialog.showError(this, "Mã khoa không được để trống.");
            txtMaKhoa.requestFocus();
            return false;
        }
        if (!maKhoa.matches("^[A-Z0-9]{3,10}$")) {
            MessageDialog.showError(this, "Mã khoa không hợp lệ.");
            txtMaKhoa.requestFocus();
            return false;
        }

        // Validate tên khoa
        if (Validator.isEmpty(tenKhoa)) {
            MessageDialog.showError(this, "Tên khoa không được để trống.");
            txtTenKhoa.requestFocus();
            return false;
        }
        if (tenKhoa.length() < 3 || tenKhoa.length() > 100) {
            MessageDialog.showError(this, "Tên khoa phải từ 3 đến 100 ký tự.");
            txtTenKhoa.requestFocus();
            return false;
        }

        return true;
    }

    private boolean kiemTraTrungMaKhoa(String maKhoa) {
        return quanLyKhoa.isDuplicateMa(maKhoa);
    }

    private int demSoGiangVienThuocKhoa(String maKhoa) {
        int count = 0;
        if (quanLyGiangVien != null) {
            for (LecturerSystem.model.GiangVien gv : quanLyGiangVien.getAll()) {
                if (maKhoa.equalsIgnoreCase(gv.getMaKhoa())) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean coGiangVienThuocKhoa(String maKhoa) {
        return demSoGiangVienThuocKhoa(maKhoa) > 0;
    }

    private boolean xacNhanXoa() {
        return MessageDialog.confirm(this, "Bạn có chắc chắn muốn xóa dữ liệu này?");
    }

    private void afterDataChanged(String message) {
        currentPage = 1;
        loadTable(quanLyKhoa.getAll());
        if (onDataChanged != null) {
            onDataChanged.run();
        }
        MessageDialog.showInfo(this, message);
    }

    private void loadTable(ArrayList<Khoa> list) {
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
            Khoa khoa = currentList.get(i);
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
