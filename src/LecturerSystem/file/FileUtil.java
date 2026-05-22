package LecturerSystem.file;

import LecturerSystem.model.GiangVien;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileUtil {
    public static final String DEFAULT_FILE = "src/data/giangvien.dat";

    public static void saveToFile(ArrayList<GiangVien> danhSach, String fileName) throws IOException {
        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        // Ghi nguyên ArrayList bằng ObjectOutputStream theo yêu cầu File IO.
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(danhSach);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<GiangVien> readFromFile(String fileName) throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<GiangVien>();
        }

        // File rỗng được xử lý ở trên để tránh lỗi khi project mới tạo.
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            Object data = ois.readObject();
            if (data instanceof ArrayList) {
                return (ArrayList<GiangVien>) data;
            }
            return new ArrayList<GiangVien>();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }
}
