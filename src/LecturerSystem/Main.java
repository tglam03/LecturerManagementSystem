package LecturerSystem;

import LecturerSystem.file.FileUtil;
import LecturerSystem.model.GiangVien;
import LecturerSystem.model.Khoa;
import LecturerSystem.model.MonHoc;
import LecturerSystem.view.LoginFrame;
import java.io.IOException;
import java.util.ArrayList;
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
