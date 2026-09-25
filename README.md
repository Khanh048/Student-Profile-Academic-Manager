# Student Profile & Academic Manager

Ứng dụng Android XML/Kotlin quản lý hồ sơ và điểm GPA của sinh viên, được thực hiện theo bài Lab Lecture 2.

## Chức năng

- Hiển thị hồ sơ sinh viên bằng `ScrollView` và `CardView`.
- Sử dụng ViewBinding, không dùng `findViewById`.
- Quản lý dữ liệu bằng immutable `Student` data class và `copy()`.
- Validate GPA an toàn bằng `toDoubleOrNull()` và giới hạn `0.0..4.0`.
- Xem trước xếp loại khi đang nhập và đổi màu theo kết quả.
- Giữ nguyên GPA đã cập nhật khi xoay màn hình bằng `onSaveInstanceState`.
- Hộp thoại xác nhận trước khi khôi phục dữ liệu mặc định.
- Gửi báo cáo học tập qua ứng dụng email bằng implicit intent `ACTION_SENDTO`.
- Tách extension functions cho `View`, `Context`, `EditText` và `Double`.

## Công nghệ và cấu hình

- Kotlin với giao diện XML truyền thống
- Android Gradle Plugin 9.3.0, Gradle 9.5.0
- `compileSdk`/`targetSdk`: 37; `minSdk`: 24
- AndroidX AppCompat, Core KTX và CardView

## Cấu trúc chính

```text
app/src/main/
├── java/com/example/studentprofile/
│   ├── MainActivity.kt
│   ├── model/Student.kt
│   └── utils/ViewExtensions.kt
└── res/
    ├── drawable/
    ├── layout/activity_main.xml
    └── values/
```

## Chạy bằng Android Studio

1. Mở thư mục repository trong Android Studio.
2. Chờ Gradle Sync hoàn tất và chọn thiết bị Android API 24 trở lên.
3. Nhấn **Run 'app'** (`Shift + F10`).

Có thể kiểm tra build từ terminal Windows:

```bat
gradlew.bat assembleDebug
```

APK debug được tạo tại `app/build/outputs/apk/debug/app-debug.apk`.

## Ma trận kiểm thử thủ công

| Mã | Thao tác | Kết quả mong đợi |
|---|---|---|
| TC-01 | Nhập `3.85`, bấm **Cập nhật GPA** | Badge hiển thị 3.85, xếp loại Xuất sắc và có Toast thành công |
| TC-02 | Để trống, bấm **Cập nhật GPA** | EditText báo lỗi, ứng dụng không crash |
| TC-03 | Nhập ký tự không phải số rồi cập nhật | Dữ liệu được xử lý an toàn bởi `toDoubleOrNull()` |
| TC-04 | Nhập `-1.0` hoặc `5.0` rồi cập nhật | Báo GPA phải trong khoảng 0.0 đến 4.0 |
| TC-05 | Cập nhật `3.9`, sau đó xoay màn hình | GPA 3.9 và xếp loại vẫn được giữ nguyên |

## Dữ liệu mẫu

- Họ tên: Võ Quốc Khánh
- MSSV: 2415053122121
- Lớp: 126TLTTD01
- Email: 2415053122121@sv.ute.udn.vn
- GPA mặc định: 3.75
