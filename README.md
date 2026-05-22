# Lecturer Management System

Hệ thống quản lý giảng viên được xây dựng bằng Java Core và Java Swing.

---

# Chức năng chính

* Đăng nhập hệ thống
* Thêm giảng viên
* Cập nhật thông tin giảng viên
* Xóa giảng viên
* Tìm kiếm giảng viên
* Thống kê dữ liệu
* Lưu dữ liệu bằng file `.dat`
* Đọc dữ liệu từ file
* Test commit
* Command

---

# Công nghệ sử dụng

* Java Core
* Java Swing
* OOP
* ArrayList
* Serializable
* File IO
* Apache NetBeans
* Git & GitHub

---

# Cấu trúc project và giải thích

```text id="6q3r91"
# HỆ THỐNG QUẢN LÝ GIẢNG VIÊN

# JAVA SWING - JAVA CORE - OOP - FILE IO

---

# 1. GIỚI THIỆU ĐỀ TÀI

Hệ thống quản lý giảng viên là phần mềm được xây dựng bằng ngôn ngữ Java kết hợp giao diện đồ họa Java Swing nhằm hỗ trợ quản lý thông tin giảng viên trong nhà trường một cách trực quan, thuận tiện và hiệu quả.

Hệ thống cho phép:

* Quản lý thông tin giảng viên
* Quản lý khoa
* Quản lý học vị
* Quản lý môn giảng dạy
* Tính lương giảng viên
* CRUD dữ liệu
* Tìm kiếm
* Sắp xếp
* Thống kê
* Đăng nhập hệ thống
* Đọc/Ghi file

Hệ thống sử dụng:

* Java Core
* OOP
* Java Swing
* ArrayList
* File IO

Dữ liệu được lưu bằng file:

```text id="x0n3v7"
giangvien.dat
```

Không sử dụng Database.

---

# 2. CÔNG NGHỆ SỬ DỤNG

| Thành phần         | Công nghệ  |
| ------------------ | ---------- |
| Ngôn ngữ lập trình | Java       |
| Giao diện          | Java Swing |
| IDE                | NetBeans   |
| Quản lý dữ liệu    | ArrayList  |
| Lưu dữ liệu        | File IO    |
| Mô hình lập trình  | OOP        |

---

# 3. MỤC TIÊU CỦA HỆ THỐNG

Hệ thống được xây dựng nhằm:

* Hỗ trợ quản lý thông tin giảng viên
* Giảm thời gian quản lý thủ công
* Hạn chế sai sót dữ liệu
* Tăng tính chính xác trong lưu trữ thông tin
* Áp dụng kiến thức Java Swing và OOP vào thực tế

---

# 4. CHỨC NĂNG CHÍNH CỦA HỆ THỐNG

# 4.1 Đăng nhập hệ thống

Cho phép người dùng đăng nhập bằng:

* Username
* Password

Nếu đăng nhập thành công:

* Chuyển sang MainFrame

Nếu sai:

* Hiển thị thông báo lỗi

Ví dụ:

```text id="3q8n1x"
admin / 123
```

---

# 4.2 Quản lý giảng viên

Hệ thống quản lý đầy đủ thông tin:

* Mã giảng viên
* Họ tên
* Ngày sinh
* Địa chỉ
* Số điện thoại
* Khoa
* Học vị
* Môn dạy
* Loại giảng viên
* Lương

---

# 4.3 CRUD dữ liệu

## CREATE

Thêm giảng viên mới.

---

## READ

Hiển thị danh sách giảng viên bằng JTable.

---

## UPDATE

Cập nhật thông tin giảng viên.

---

## DELETE

Xóa giảng viên khỏi hệ thống.

---

# 4.4 Tìm kiếm

Tìm kiếm giảng viên theo:

* Mã giảng viên
* Họ tên
* Khoa
* Học vị

---

# 4.5 Sắp xếp

Sắp xếp theo:

* Họ tên
* Mức lương
* Khoa

---

# 4.6 Thống kê

Hệ thống hỗ trợ:

* Tổng số giảng viên
* Tổng quỹ lương
* Thống kê theo khoa
* Thống kê theo học vị
* Biểu đồ thống kê

---

# 4.7 Đọc/Ghi file

Hệ thống hỗ trợ:

* Lưu dữ liệu
* Đọc dữ liệu

Sử dụng:

```text id="9m4v0k"
ObjectOutputStream
ObjectInputStream
```

---

# 5. NGHIỆP VỤ TÍNH LƯƠNG

Hệ thống quản lý 2 loại giảng viên:

* Giảng viên cơ hữu
* Giảng viên thỉnh giảng

---

# 5.1 Giảng viên cơ hữu

## Thuộc tính

* Hệ số lương
* Phụ cấp

## Công thức tính lương

```text id="2u9x4m"
Lương =
Hệ số lương × 1.800.000
+
Phụ cấp học vị
```

---

## Phụ cấp học vị

| Học vị  | Phụ cấp   |
| ------- | --------- |
| Cử nhân | 500.000   |
| Thạc sĩ | 1.000.000 |
| Tiến sĩ | 2.000.000 |
| PGS     | 3.000.000 |
| GS      | 5.000.000 |

---

# 5.2 Giảng viên thỉnh giảng

## Thuộc tính

* Số giờ dạy
* Tiền mỗi giờ

## Công thức tính lương

```text id="m7q1v5"
Lương =
Số giờ dạy × Tiền mỗi giờ
```

---

# 6. CẤU TRÚC SOURCE HOÀN CHỈNH

```text id="r5t9x0"
LecturerManagementSystem
│
├── src
│   │
│   ├── LecturerSystem
│   │   │
│   │   └── Main.java
│   │
│   ├── LecturerSystem.model
│   │   │
│   │   ├── GiangVien.java
│   │   ├── GiangVienCoHuu.java
│   │   ├── GiangVienThinhGiang.java
│   │   └── TaiKhoan.java
│   │
│   ├── LecturerSystem.interfaces
│   │   │
│   │   └── TinhLuong.java
│   │
│   ├── LecturerSystem.service
│   │   │
│   │   ├── QuanLyGiangVien.java
│   │   └── AuthService.java
│   │
│   ├── LecturerSystem.view
│   │   │
│   │   ├── LoginFrame.java
│   │   ├── MainFrame.java
│   │   └── ThongKeFrame.java
│   │
│   ├── LecturerSystem.file
│   │   │
│   │   └── FileUtil.java
│   │
│   ├── LecturerSystem.utils
│   │   │
│   │   ├── Validator.java
│   │   └── MessageDialog.java
│   │
│   └── data
│       │
│       └── giangvien.dat
│
├── nbproject
├── build
├── dist
├── build.xml
├── manifest.mf
├── .gitignore
└── README.md
```

---

# 7. GIẢI THÍCH CÁC PACKAGE

# 7.1 Package LecturerSystem

## Main.java

### Chức năng

* File chạy chính của hệ thống
* Chứa hàm main()
* Khởi động chương trình

Ví dụ:

```java id="4m8q2u"
public static void main(String[] args) {
    new LoginFrame().setVisible(true);
}
```

---

# 7.2 Package model

Chứa các class đối tượng dữ liệu.

---

## GiangVien.java

### Chức năng

Quản lý thông tin chung của giảng viên.

### Chứa

* Mã giảng viên
* Họ tên
* Địa chỉ
* Khoa
* Học vị
* Môn dạy

### Đặc biệt

```java id="q6n1w9"
implements Serializable
```

để lưu file object.

---

## GiangVienCoHuu.java

### Kế thừa

```java id="8x0m2p"
extends GiangVien
```

### Chứa

* Hệ số lương
* Phụ cấp

### Chức năng

Tính lương giảng viên cơ hữu.

---

## GiangVienThinhGiang.java

### Kế thừa

```java id="6u4k9r"
extends GiangVien
```

### Chứa

* Số giờ dạy
* Tiền mỗi giờ

### Chức năng

Tính lương giảng viên thỉnh giảng.

---

## TaiKhoan.java

### Chức năng

Quản lý tài khoản đăng nhập.

### Chứa

* username
* password

---

# 7.3 Package interfaces

## TinhLuong.java

### Chức năng

Interface dùng để tính lương.

Ví dụ:

```java id="9r2m0x"
public interface TinhLuong {
    double tinhLuong();
}
```

### Dùng để làm gì

Áp dụng:

* Interface
* Đa hình
* OOP

---

# 7.4 Package service

Package xử lý nghiệp vụ hệ thống.

---

## QuanLyGiangVien.java

### Chức năng

Quản lý danh sách:

```java id="1v7m0q"
ArrayList<GiangVien>
```

### Các chức năng

* Thêm
* Sửa
* Xóa
* Tìm kiếm
* Sắp xếp
* Thống kê

---

## AuthService.java

### Chức năng

Xử lý đăng nhập.

### Chức năng chính

* Kiểm tra username
* Kiểm tra password

---

# 7.5 Package view

Package chứa giao diện Java Swing.

---

## LoginFrame.java

### Chức năng

Giao diện đăng nhập.

### Thành phần

* JTextField username
* JPasswordField password
* JButton đăng nhập
* JButton thoát

---

## MainFrame.java

### Chức năng

Là giao diện chính của toàn bộ hệ thống.

### Chứa

## FORM NHẬP

* Mã giảng viên
* Họ tên
* Địa chỉ
* Khoa
* Học vị
* Môn dạy
* Hệ số lương
* Số giờ dạy

---

## JTable

Hiển thị danh sách giảng viên.

---

## BUTTON CRUD

* Thêm
* Sửa
* Xóa
* Tìm kiếm
* Làm mới
* Lưu file
* Đọc file
* Thống kê
* Thoát

---

## MENU

* Menu hệ thống
* Menu quản lý
* Menu tìm kiếm
* Menu sắp xếp
* Menu thống kê

---

## ThongKeFrame.java

### Chức năng

Hiển thị thống kê hệ thống.

### Nội dung

* Tổng giảng viên
* Tổng quỹ lương
* Thống kê theo khoa
* Biểu đồ thống kê

---

# 7.6 Package file

## FileUtil.java

### Chức năng

* Ghi dữ liệu xuống file
* Đọc dữ liệu từ file

### Sử dụng

```java id="3w0n8m"
ObjectOutputStream
ObjectInputStream
```

---

# 7.7 Package utils

## Validator.java

### Chức năng

Kiểm tra dữ liệu nhập.

### Kiểm tra

* Không để trống
* Mã giảng viên không trùng
* Lương hợp lệ
* Số điện thoại hợp lệ

---

## MessageDialog.java

### Chức năng

Hiển thị thông báo hệ thống.

### Công nghệ

```java id="7m0q2u"
JOptionPane
```

---

# 8. CÁC GIAO DIỆN CỦA HỆ THỐNG

| Giao diện    | Chức năng            |
| ------------ | -------------------- |
| LoginFrame   | Đăng nhập            |
| MainFrame    | CRUD + JTable + Menu |
| ThongKeFrame | Thống kê + biểu đồ   |

---

# 9. LUỒNG HOẠT ĐỘNG HỆ THỐNG

```text id="x8v0m2"
LoginFrame
    ↓
MainFrame
    ↓
CRUD dữ liệu
    ↓
Lưu file / Thống kê
```

---

# 10. KIẾN THỨC ÁP DỤNG

Hệ thống áp dụng:

* Java Core
* OOP
* Kế thừa
* Đa hình
* Interface
* Java Swing
* Event Handling
* JTable
* ArrayList
* File IO
* Serializable

---

# 11. KẾT LUẬN

Hệ thống quản lý giảng viên bằng Java Swing giúp quản lý thông tin giảng viên một cách trực quan và hiệu quả. Đề tài áp dụng đầy đủ kiến thức Java Core, OOP, Swing GUI và File IO để xây dựng một ứng dụng quản lý hoàn chỉnh theo mô hình Java Desktop Application.

```

---

# Clone project

## SSH

```bash id="duztj5"
git clone git@github.com:tglam03/LecturerManagementSystem.git
```

## HTTPS

```bash id="n6vbvz"
git clone https://github.com/tglam03/LecturerManagementSystem.git
```

---

# Cách chạy project

## Bước 1

Mở project bằng Apache NetBeans.

## Bước 2

Run file:

```text id="u8m0wz"
Main.java
```

---

# Cài đặt Git

# Tạo SSH Key nếu mỗi lần push mà bị hỏi đăng nhập thì tạo key (Hỏi chat)

---

# Cấu hình Git Ignore khi pull source code về 

Tạo file:
```text id="c2y3m7"
.gitignore
```

Nội dung:

```gitignore id="ndq7ie"
build/
dist/
nbproject/private/
*.class
```

---

# Workflow Làm Việc Nhóm

## 1. Thành viên clone project về máy

```bash id="8d1k2n"
git clone git@github.com:tglam03/LecturerManagementSystem.git
```

---

## 2. Di chuyển vào project

```bash id="9o5h2q"
cd LecturerManagementSystem
```

---

## 3. Pull code mới nhất từ main

```bash id="0hmwd8"
git pull origin main
```

---

## 4. Tạo branch riêng để làm việc

Ví dụ:

```bash id="k0e4n7"
git checkout -b feature-login
```

Hoặc:

```bash id="4z6n7x"
git checkout -b feature-statistic
```

---

## 5. Kiểm tra branch hiện tại

```bash id="g1cf8u"
git branch
```

Branch hiện tại sẽ có dấu:

```text id="gm3wxq"
*
```

---

## 6. Code chức năng trên branch riêng

Ví dụ:

* Login
* CRUD giảng viên
* File IO
* Thống kê

---

## 7. Add code sau khi hoàn thành

```bash id="9c6j2s"
git add .
```

---

## 8. Commit code

```bash id="7s6h4w"
git commit -m "Add login feature"
```

---

## 9. Push branch lên GitHub

```bash id="j4xt7f"
git push origin feature-login
```

---

## 10. Tạo Pull Request trên GitHub

```text id="rqvpr8"
GitHub
→ Compare & Pull Request
→ Create Pull Request
```

---

## 11. Leader merge branch vào main

```text id="96mv8m"
Merge Pull Request
```

---

## 12. Sau khi merge xong quay về main

```bash id="n5f0jq"
git checkout main
```

---

## 13. Pull code mới nhất

```bash id="a9qqkl"
git pull origin main
```

---

## 14. Tạo branch mới cho chức năng tiếp theo

Ví dụ:

```bash id="h4s21312vi"
git checkout -b feature-ui
```

---

# Quy tắc làm việc nhóm

* Không code trực tiếp trên branch `main`
* Luôn tạo branch riêng cho từng chức năng
* Luôn pull code mới nhất trước khi làm
* Kiểm tra project chạy ổn trước khi push

---

* Nguyen Duc Tung Lam

