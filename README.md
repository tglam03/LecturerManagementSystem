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

# Cấu trúc project

```text id="6q3r91"
LecturerManagementSystem
│
├── src
│   │
│   ├── LecturerSystem
│   │   └── Main.java
│   │
│   ├── LecturerSystem.model
│   │   ├── GiangVien.java
│   │   ├── GiangVienCoHuu.java
│   │   ├── GiangVienThinhGiang.java
│   │   └── TaiKhoan.java
│   │
│   ├── LecturerSystem.service
│   │   ├── QuanLyGiangVien.java
│   │   └── AuthService.java
│   │
│   ├── LecturerSystem.view
│   │   ├── MainFrame.java
│   │   ├── LoginFrame.java
│   │   ├── ThemGiangVienFrame.java
│   │   └── ThongKeFrame.java
│   │
│   ├── LecturerSystem.file
│   │   └── FileUtil.java
│   │
│   ├── LecturerSystem.utils
│   │   ├── Validator.java
│   │   └── MessageDialog.java
│   │
│   └── data
│       └── giangvien.dat
│
├── nbproject
├── build.xml
├── manifest.mf
├── .gitignore
└── README.md
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

