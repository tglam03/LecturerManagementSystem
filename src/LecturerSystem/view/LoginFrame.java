package LecturerSystem.view;

import LecturerSystem.service.AuthService;
import LecturerSystem.utils.MessageDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private static final Color BACKGROUND = new Color(241, 245, 249);
    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);

    private final AuthService authService;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Đăng nhập - Lecturer Management System");
        setSize(520, 430);
        setMinimumSize(new Dimension(480, 390));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));
        wrapper.add(createLoginCard());
        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(28, 34, 28, 34)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 0, 7, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel icon = new JLabel("LMS", SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(new Color(219, 234, 254));
        icon.setForeground(PRIMARY);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        icon.setPreferredSize(new Dimension(72, 42));
        gbc.gridy = 0;
        card.add(icon, gbc);

        JLabel title = new JLabel("Lecturer Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(TEXT_DARK);
        gbc.gridy = 1;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Đăng nhập để quản lý thông tin giảng viên", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_MUTED);
        gbc.gridy = 2;
        card.add(subtitle, gbc);

        gbc.insets = new Insets(18, 0, 5, 0);
        gbc.gridy = 3;
        card.add(createLabel("Username"), gbc);

        txtUsername = createInput();
        txtUsername.setText("admin");
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.gridy = 4;
        card.add(txtUsername, gbc);

        gbc.insets = new Insets(8, 0, 5, 0);
        gbc.gridy = 5;
        card.add(createLabel("Password"), gbc);

        txtPassword = new JPasswordField();
        styleInput(txtPassword);
        gbc.insets = new Insets(0, 0, 18, 0);
        gbc.gridy = 6;
        card.add(txtPassword, gbc);

        JButton btnLogin = createPrimaryButton("Đăng nhập");
        gbc.insets = new Insets(4, 0, 8, 0);
        gbc.gridy = 7;
        card.add(btnLogin, gbc);

        JButton btnExit = createSecondaryButton("Thoát");
        gbc.gridy = 8;
        card.add(btnExit, gbc);

        btnLogin.addActionListener(e -> login());
        btnExit.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(btnLogin);
        return card;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JTextField createInput() {
        JTextField field = new JTextField();
        styleInput(field);
        return field;
    }

    private void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(360, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                new EmptyBorder(0, 12, 0, 12)));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(360, 42));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(360, 38));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(TEXT_DARK);
        button.setBackground(new Color(241, 245, 249));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        if (authService.login(username, password)) {
            new DashboardFrame().setVisible(true);
            dispose();
        } else {
            MessageDialog.showError(this, "Sai username hoặc password. Tài khoản mặc định: admin / 123");
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }
}
