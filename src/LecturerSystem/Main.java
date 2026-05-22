package LecturerSystem;

import LecturerSystem.view.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    System.out.println("Không thể thiết lập giao diện hệ thống: " + ex.getMessage());
                }
                new LoginFrame().setVisible(true);
            }
        });
    }
}
