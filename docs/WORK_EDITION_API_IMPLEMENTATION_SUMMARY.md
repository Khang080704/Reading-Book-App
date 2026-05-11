# Tóm tắt triển khai API Work / Edition

## Mục tiêu
Triển khai lại các API đọc dữ liệu `work` và `edition` theo chiến lược:
1. Kiểm tra DB trước.
2. Nếu chưa có dữ liệu thì gọi Open Library.
3. Lưu dữ liệu vào DB để dùng cho lần sau.
4. Đảm bảo quan hệ khóa ngoại đúng với entity hiện tại.

---

## Endpoint đã bổ sung

### 1) Lấy chi tiết work
- `GET /api/v1/books/works/{workKey}`

### 2) Lấy danh sách editions của một work
- `GET /api/v1/books/works/{workKey}/editions`
- Đây là nested route, dùng để lọc tất cả bản in thuộc cùng một work.

### 3) Lấy chi tiết một edition
- `GET /api/v1/books/editions/{editionKey}`
- Đây là independent route, dùng để lấy đúng một bản in.

---

## Luồng xử lý mới

### Work detail
1. Chuẩn hóa `workKey` về dạng canonical nội bộ.
2. Tìm trong `WorkRepository`.
3. Nếu có thì map ra `BookDetailDTO`.
4. Nếu không có thì gọi Open Library `/works/{workId}.json`.
5. Lưu `Work` vào DB.
6. Tạo stub `AuthorDetail` theo `olKey` nếu author chưa tồn tại.
7. Trả dữ liệu cho client.

### Work editions
1. Tìm editions theo `workKey` trong DB.
2. Nếu có dữ liệu thì trả ngay.
3. Nếu chưa có thì đảm bảo `Work` đã tồn tại trước.
4. Gọi Open Library `/works/{workId}/editions.json`.
5. Lưu từng `Edition` với `work_id` đúng FK.
6. Trả `EditionsListDTO`.

### Single edition
1. Tìm `Edition` theo `editionKey` trong DB.
2. Nếu có thì trả ngay.
3. Nếu chưa có thì gọi Open Library `/books/{editionId}.json`.
4. Lấy `work` liên kết từ payload edition.
5. Nếu work chưa có thì tạo/lưu work trước.
6. Lưu edition với FK trỏ về work.
7. Trả `EditionDTO`.

---

## Thay đổi chính trong code

### `SearchService`
- Bổ sung logic DB-first cho work và edition.
- Thêm hàm chuẩn hóa key:
  - `canonicalWorkKey(...)`
  - `canonicalEditionKey(...)`
  - `canonicalAuthorKey(...)`
- Thêm mapping:
  - `Work -> BookDetailDTO`
  - `Edition -> EditionDTO`
- Thêm logic tạo stub `AuthorDetail` theo `olKey`.

### `BookController`
- Thêm 3 endpoint work/edition.

### `BookApiClient`
- Thêm call đến Open Library:
  - `/works/{workId}.json`
  - `/works/{workId}/editions.json`
  - `/books/{editionId}.json`

### `EditionRepository`
- Thêm `findByEditionKey(...)`.

### `WorkRepository`
- Bổ sung fetch graph cho `authors` khi load work để map chi tiết an toàn hơn.

### DTO mới
- `OpenLibraryEditionDTO`
  - Dùng cho chi tiết một edition.
  - Hỗ trợ lấy ISBN, publisher và work reference từ payload.

---

## Về repository `Book` và `Work`

Đã **cân nhắc nhưng chưa gộp**.

### Lý do
- `Book` đang là mô hình legacy/deprecated cho search.
- `Work` là mô hình chính cho luồng work/edition mới.
- Gộp repository lúc này dễ làm lẫn trách nhiệm và phá các luồng cũ.

### Kết luận
- Giữ `BookRepository` và `WorkRepository` tách riêng.
- Ưu tiên `WorkRepository` cho luồng work/edition.

---

## Ghi chú
- `workKey` và `editionKey` được chuẩn hóa về dạng canonical nội bộ (`/works/...`, `/editions/...`).
- Dữ liệu author được tạo stub theo `olKey` để đảm bảo FK/join table hoạt động đúng.
- File này chỉ là tóm tắt thay đổi, không chứa test.

