package LecturerSystem.file;

import LecturerSystem.model.GiangVien;
import LecturerSystem.model.Khoa;
import LecturerSystem.model.MonHoc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileUtil {
    public static final String GIANG_VIEN_FILE = "src/data/giangvien.dat";
    public static final String KHOA_FILE = "src/data/khoa.dat";
    public static final String MON_HOC_FILE = "src/data/monhoc.dat";

    public static void saveGiangVien(ArrayList<GiangVien> danhSach) throws IOException {
        saveObject(danhSach, GIANG_VIEN_FILE);
    }

    public static ArrayList<GiangVien> readGiangVien() throws IOException, ClassNotFoundException {
        return readObjectList(GIANG_VIEN_FILE);
    }

    public static void saveKhoa(ArrayList<Khoa> danhSach) throws IOException {
        saveObject(danhSach, KHOA_FILE);
    }

    public static ArrayList<Khoa> readKhoa() throws IOException, ClassNotFoundException {
        return readObjectList(KHOA_FILE);
    }

    public static void saveMonHoc(ArrayList<MonHoc> danhSach) throws IOException {
        saveObject(danhSach, MON_HOC_FILE);
    }

    public static ArrayList<MonHoc> readMonHoc() throws IOException, ClassNotFoundException {
        return readObjectList(MON_HOC_FILE);
    }

    public static void saveAll(ArrayList<GiangVien> giangVien, ArrayList<Khoa> khoa,
            ArrayList<MonHoc> monHoc) throws IOException {
        saveGiangVien(giangVien);
        saveKhoa(khoa);
        saveMonHoc(monHoc);
    }

    private static void saveObject(Object data, String fileName) throws IOException {
        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(data);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> readObjectList(String fileName) throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<T>();
        }

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            Object data = ois.readObject();
            if (data instanceof ArrayList) {
                return (ArrayList<T>) data;
            }
            return new ArrayList<T>();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }
}
