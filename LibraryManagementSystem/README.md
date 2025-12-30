# Library Management System

## 1. Giới thiệu

Library Management System là ứng dụng quản lý thư viện được phát triển bằng ngôn ngữ Java.  
Hệ thống hỗ trợ quản lý tài liệu, quản lý thành viên, nghiệp vụ mượn – trả và thống kê báo cáo.

---

## 2. Mục tiêu dự án

- Áp dụng kiến thức môn Lập trình hướng đối tương  
- Thực hành UML (Use Case Diagram, Class Diagram)  
- Xây dựng hệ thống có kiến trúc rõ ràng, dễ bảo trì và mở rộng  
- Tách biệt giao diện, nghiệp vụ xử lý và dữ liệu  

---

## 3. Kiến trúc hệ thống

Hệ thống được tổ chức theo mô hình **Layered Architecture**:

UI → Services → Models → Utils / Config

### Mô tả các tầng

- **UI**: Giao diện người dùng (Java Swing và Console)  
- **Services**: Xử lý nghiệp vụ của hệ thống  
- **Models**: Các lớp biểu diễn dữ liệu và thực thể  
- **Utils**: Các lớp tiện ích và xử lý lỗi  
- **Config**: Cấu hình hệ thống  

---

## 4. Chức năng chính

### 4.1 Quản lý thành viên
- Thêm thành viên
- Xóa thành viên
- Tìm kiếm thành viên

### 4.2 Quản lý tài liệu
- Thêm, xóa và tìm kiếm sách, tạp chí  
- Hỗ trợ nhiều loại tài liệu thông qua kế thừa  

### 4.3 Mượn – Trả tài liệu
- Mượn tài liệu  
- Trả tài liệu  
- Lưu lịch sử mượn – trả  

### 4.4 Báo cáo và thống kê
- Thống kê dữ liệu
- Xuất báo cáo hệ thống

---

## 5. Công nghệ sử dụng

- **Ngôn ngữ lập trình**: Java  
- **Build tool**: Maven  
- **Lưu trữ dữ liệu**: File văn bản (.txt)  
- **Thiết kế hệ thống**: UML (Use Case Diagram, Class Diagram)  
- **Giao diện người dùng**: Java Swing  

---

## 6. Cấu trúc thư mục

LibraryManagementSystem/
│
├── 📄 pom.xml                         # Maven configuration
├── 📄 README.md                       # Project documentation
│
├── 📁 src/main/java/com/library/
│   │
│   ├── 📄 Main.java                   # Application entry point
│   │
│   ├── 📁 config/
│   │   └── 📄 LibraryConfig.java      # Configuration constants
│   │
│   ├── 📁 models/                     # Data models
│   │   ├── 📄 LibraryItem.java        # Abstract base class
│   │   ├── 📄 Book.java               # Extends LibraryItem
│   │   ├── 📄 Magazine.java           # Extends LibraryItem  
│   │   ├── 📄 Member.java
│   │   └── 📄 BorrowRecord.java
│   │
│   ├── 📁 services/                   # Business logic
│   │   ├── 📄 ItemService.java
│   │   ├── 📄 MemberService.java
│   │   ├── 📄 BorrowService.java
│   │   ├── 📄 ReportService.java
│   │   └── 📄 FileManager.java        # File I/O operations
│   │
│   ├── 📁 utils/                      # Utility classes
│   │   ├── 📄 Validator.java          # Input validation
│   │   ├── 📄 DateHelper.java         # Date utilities
│   │   └── 📁 exceptions/
│   │       ├── 📄 LibraryException.java
│   │       ├── 📄 ValidationException.java
│   │       ├── 📄 NotFoundException.java
│   │       └── 📄 FileOperationException.java
│   │
│   └── 📁 ui/                         # User interface
│       │
│       ├── 📄 ConsoleUI.java          # Console interface (legacy)
│       ├── 📄 MainFrame.java          # Main GUI window
│       │
│       ├── 📁 panels/                 # Main panels
│       │   ├── 📄 DashboardPanel.java      # Home dashboard
│       │   ├── 📄 ItemManagementPanel.java # Item CRUD operations
│       │   ├── 📄 MemberManagementPanel.java # Member management
│       │   ├── 📄 BorrowReturnPanel.java    # Borrow/return system
│       │   └── 📄 ReportsPanel.java         # Reporting system
│       │
│       ├── 📁 components/             # Reusable UI components
│       │   ├── 📄 CustomButton.java   # Styled buttons
│       │   ├── 📄 CustomTable.java    # Custom table component
│       │   └── 📄 SearchField.java    # Search input field
│       │
│       └── 📁 dialogs/                # Modal dialogs
│           ├── 📄 AddItemDialog.java
│           ├── 📄 AddMemberDialog.java
│           ├── 📄 BorrowDialog.java
│           └── 📄 ReturnDialog.java
│
├── 📁 src/main/resources/
│   ├── 📁 icons/                      # Icon resources
│   └── 📁 styles/
│       └── 📄 styles.css              # CSS styling
│
├── 📁 data/                           # Data storage (TXT files)
│   ├── 📄 items.txt
│   ├── 📄 members.txt
│   └── 📄 borrow_records.txt
│
└── 📁 src/test/java/                  # Test cases
    ├── 📁 services/
    │   ├── 📄 ItemServiceTest.java
    │   ├── 📄 MemberServiceTest.java
    │   └── 📄 BorrowServiceTest.java
    └── 📁 models/
        └── 📄 LibraryItemTest.java


---

## 7. Hướng dẫn chạy chương trình

### Bước 1: Clone repository
git clone https://github.com/trantuan321/OOP-BTL-162254.git

### Bước 2: Mở project bằng IDE
IntelliJ IDEA hoặc Eclipse
Import project theo dạng Maven

### Bước 3: Chạy chương trình
Chạy file: src/main/java/com/library/Main.java

---

## 8. Lưu ý
- Dữ liệu của chương trình được lưu trong thư mục data/
- Chương trình không sử dụng cơ sở dữ liệu
- Phù hợp với phạm vi bài tập học phần
- Có thể mở rộng để tích hợp database trong tương lai

## 9. Hướng phát triển
- Thêm chức năng đăng nhập và phân quyền người dùng
- Thay thế lưu trữ file bằng cơ sở dữ liệu
- Phát triển phiên bản Web hoặc RESTful API
- Cải thiện và tối ưu giao diện người dùng

## 10. Tài liệu thiết kế
- Use Case Diagram
- Class Diagram
- Báo cáo phân tích và thiết kế phần mềm
- Slide trình bày và bảo vệ dự án