# BookReadingApp API Reference

Tài liệu này được tổng hợp trực tiếp từ các file `controller`, `dto` và `exception` trong codebase hiện tại.

## 1) Tổng quan

- **Base path**: `/api/v1`
- **Public endpoints**:
  - `POST /api/v1/auth/register`
  - `POST /api/v1/auth/login`
- **Protected endpoints**: tất cả endpoint còn lại trong tài liệu này yêu cầu `Authorization: Bearer <accessToken>`.
- **Lưu ý**:
  - `SecurityConfig` có permit cho `/api/v1/auth/refresh`, nhưng hiện tại không có controller tương ứng trong source code nên **không** được liệt kê như một API hiện có.
  - Một số endpoint lấy dữ liệu từ Open Library và có thể trả về `null` ở vài field nếu upstream không có dữ liệu.

---

## 2) API theo controller

# 2.1 `AuthController`

## 2.1.1 `POST /api/v1/auth/register`

- **Mục đích**: Đăng ký tài khoản mới.
- **Auth**: Không cần token.
- **Request body**: `RegisterRequest`

```json
{
  "username": "dwqfq",
  "password": "fqwfqw",
  "email": "user@example.com"
}
```

### Request fields
- `username` *(string, required)*
- `password` *(string, required, tối thiểu 6 ký tự theo message validation hiện tại)*
- `email` *(string, required, định dạng email)*

### Response success: `201 Created`
`UserDto`

```json
{
  "email": "user@example.com",
  "userName": "dwqfq"
}
```

### Response fields
- `email` *(string)*: email của user.
- `userName` *(string)*: username từ request.

### Ghi chú
- Service hiện tại chỉ lưu `email` và `password` vào database.
- `username` được trả về trong response nhưng không thấy được persist trong luồng `register` hiện tại.

### Exception có thể gặp
- `400 Bad Request` nếu validate fail (`MethodArgumentNotValidException`)
- `400 Bad Request` nếu email đã tồn tại (`EmailExists`)

---

## 2.1.2 `POST /api/v1/auth/login`

- **Mục đích**: Đăng nhập và nhận JWT.
- **Auth**: Không cần token.
- **Request body**: `LoginRequest`

```json
{
  "email": "user@example.com",
  "password": "fqwfqw"
}
```

### Request fields
- `email` *(string, required, định dạng email)*
- `password` *(string, required)*

### Response success: `200 OK`
`TokenResponse`

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

### Response fields
- `accessToken` *(string)*: JWT access token.
- `refreshToken` *(string)*: JWT refresh token.

### Exception có thể gặp
- `400 Bad Request` nếu validate fail (`MethodArgumentNotValidException`)
- `401 Unauthorized` nếu sai email/mật khẩu (`WrongCredentials`)

---

# 2.2 `UserController`

## 2.2.1 `GET /api/v1/me`

- **Mục đích**: Lấy thông tin user hiện tại từ token.
- **Auth**: Bắt buộc.
- **Request body**: Không có.
- **Query params**: Không có.
- **Path params**: Không có.

### Response success: `200 OK`
`UserDto`

```json
{
  "email": "user@example.com",
  "userName": "John Doe"
}
```

### Response fields
- `email` *(string)*: email lấy từ database.
- `userName` *(string)*: tên hiển thị của user.

### Ghi chú
- `UserService.getCurrentUser()` sẽ đọc principal từ `SecurityContextHolder` và tìm user theo email.
- Nếu user không tìm thấy, service có thể trả về `null`.

### Exception / hành vi liên quan
- Endpoint này phụ thuộc vào JWT authentication của Spring Security.
- Trong source hiện tại **không có** custom exception handler riêng cho lỗi thiếu/invalid token; response sẽ phụ thuộc vào Spring Security/JWT filter.

---

# 2.3 `AuthorController`

## 2.3.1 `GET /api/v1/author/search`

- **Mục đích**: Tìm tác giả theo từ khóa.
- **Auth**: Bắt buộc.
- **Request body**: Không có.

### Query params
| Tên | Kiểu | Default | Mô tả |
|---|---:|---:|---|
| `q` | string | `""` | Từ khóa tìm kiếm |
| `page` | int | `0` | Số trang nhận vào từ client |
| `limit` | int | `20` | Số bản ghi tối đa |
| `sortBy` | string | `"name"` | Trường sắp xếp nhận vào |
| `direction` | string | `"desc"` | Hướng sắp xếp nhận vào |

### Ghi chú quan trọng
- Trong implementation hiện tại, controller **chỉ truyền `q` và `limit` xuống service**.
- `page`, `sortBy`, `direction` được nhận từ request nhưng **chưa được dùng** trong luồng xử lý hiện tại.

### Response success: `200 OK`
Danh sách `AuthorDTO`

```json
[
  {
    "id": "/authors/OL34221A",
    "name": "J. R. R. Tolkien",
    "birthDay": "1892-01-03",
    "readCount": 12345,
    "olKey": "/authors/OL34221A",
    "avatar": "https://covers.openlibrary.org/a/olid//authors/OL34221A-M.jpg"
  }
]
```

### Response fields của từng phần tử
- `id` *(string)*: key của author.
- `name` *(string)*: tên tác giả.
- `birthDay` *(string)*: ngày sinh dạng chuỗi.
- `readCount` *(int)*: số lượng đọc / reading count.
- `olKey` *(string)*: Open Library key.
- `avatar` *(string)*: URL avatar Open Library.

### Exception có thể gặp
- Lỗi authentication từ Spring Security nếu thiếu token.
- Các lỗi khác upstream/Open Library có thể đi qua global exception nếu phát sinh ngoài controller.

---

## 2.3.2 `GET /api/v1/author/{olkey}`

- **Mục đích**: Lấy chi tiết một tác giả.
- **Auth**: Bắt buộc.
- **Request body**: Không có.

### Path params
| Tên | Kiểu | Mô tả |
|---|---:|---|
| `olkey` | string | Open Library author key, ví dụ `OL34221A` hoặc `\/authors\/OL34221A` |

### Response success: `200 OK`
`AuthorDetailDTO`

```json
{
  "birthDate": "1892-01-03",
  "fullName": "J. R. R. Tolkien",
  "bio": "English writer, poet, philologist, and academic...",
  "createdAt": "2024-01-01T10:15:30",
  "lastModifiedAt": "2024-01-02T11:20:45",
  "avatar": "https://covers.openlibrary.org/a/olid/OL34221A-M.jpg"
}
```

### Response fields
- `birthDate` *(string)*: ngày sinh.
- `fullName` *(string)*: tên đầy đủ.
- `bio` *(string)*: tiểu sử.
- `createdAt` *(LocalDateTime serialized to string)*: thời điểm tạo.
- `lastModifiedAt` *(LocalDateTime serialized to string)*: thời điểm cập nhật.
- `avatar` *(string)*: URL ảnh đại diện.

### Exception có thể gặp
- `404 Not Found` nếu author không tồn tại (`AuthorNotExists`)
- Các lỗi HTTP từ Open Library có thể được chuyển thành exception tương ứng trong service

---

## 2.3.3 `GET /api/v1/author/{authorKey}/works`

- **Mục đích**: Lấy danh sách works của một tác giả.
- **Auth**: Bắt buộc.
- **Request body**: Không có.

### Path params
| Tên | Kiểu | Mô tả |
|---|---:|---|
| `authorKey` | string | Open Library author key, ví dụ `OL34221A` hoặc `\/authors\/OL34221A` |

### Response success: `200 OK`
Danh sách `WorkDTO`

```json
[
  {
    "workKey": "/works/OL45804W",
    "title": "The Hobbit",
    "description": "Fantasy novel by J. R. R. Tolkien",
    "coverUrl": "https://covers.openlibrary.org/b/id/123456-M.jpg"
  }
]
```

### Response fields của từng phần tử
- `workKey` *(string)*: key của work.
- `title` *(string)*: tiêu đề.
- `description` *(string)*: mô tả, có thể `null`.
- `coverUrl` *(string)*: URL cover Open Library, có thể `null` nếu không có cover.

### Exception có thể gặp
- `404 Not Found` nếu author không tồn tại (`AuthorNotExists`)
- `404 Not Found` nếu Open Library trả về 404
- Các lỗi khác khi gọi Open Library có thể được map về `AuthorNotExists` theo logic hiện tại

---

# 2.4 `BookController`

## 2.4.1 `GET /api/v1/books/search`

- **Mục đích**: Tìm sách theo từ khóa.
- **Auth**: Bắt buộc.
- **Request body**: Không có.

### Query params
| Tên | Kiểu | Default | Mô tả |
|---|---:|---:|---|
| `q` | string | — | Từ khóa tìm kiếm, bắt buộc |
| `page` | int | `1` | Trang |
| `limit` | int | `10` | Số kết quả trên trang |

### Response success: `200 OK`
Danh sách `SearchBookDTO`

```json
[
  {
    "bookKey": "/works/OL45804W",
    "title": "The Hobbit",
    "authorNames": ["J. R. R. Tolkien"],
    "firstPublishYear": 1937,
    "isbn": "9780007118359",
    "editionCount": 120,
    "coverUrl": "https://covers.openlibrary.org/a/olid/123456-M.jpg"
  }
]
```

### Response fields của từng phần tử
- `bookKey` *(string)*: key của work/book.
- `title` *(string)*: tiêu đề.
- `authorNames` *(string[])*: danh sách tên tác giả.
- `firstPublishYear` *(int)*: năm xuất bản đầu tiên.
- `isbn` *(string)*: ISBN đầu tiên lấy từ dữ liệu Open Library.
- `editionCount` *(int)*: số ấn bản.
- `coverUrl` *(string)*: URL cover ảnh sách.

### Exception có thể gặp
- `404 Not Found` nếu dữ liệu work/book không tồn tại (`BookNotFound`)
- `502 Bad Gateway` hoặc status code do upstream trả về nếu Open Library lỗi (`BookApiException`)
- `400 Bad Request` nếu thiếu/không hợp lệ query param bắt buộc `q` (nếu binding/validation bị kích hoạt ở tầng trên)

---

## 2.4.2 `GET /api/v1/books/works/{workKey}`

- **Mục đích**: Lấy chi tiết một work.
- **Auth**: Bắt buộc.
- **Request body**: Không có.

### Path params
| Tên | Kiểu | Mô tả |
|---|---:|---|
| `workKey` | string | Key work, ví dụ `OL45804W` hoặc `\/works\/OL45804W` |

### Response success: `200 OK`
`BookDetailDTO`

```json
{
  "workKey": "/works/OL45804W",
  "title": "The Hobbit",
  "description": "Fantasy novel by J. R. R. Tolkien",
  "coverUrl": "https://covers.openlibrary.org/b/id/123456-M.jpg",
  "authorKeys": ["/authors/OL34221A"]
}
```

### Response fields
- `workKey` *(string)*: key của work.
- `title` *(string)*: tiêu đề.
- `description` *(string)*: mô tả.
- `coverUrl` *(string)*: URL cover.
- `authorKeys` *(List<string>)*: danh sách author key.

### Exception có thể gặp
- `404 Not Found` nếu work không tồn tại (`BookNotFound`)
- `502 Bad Gateway` hoặc status code do upstream trả về nếu Open Library lỗi (`BookApiException`)

---

## 2.4.3 `GET /api/v1/books/works/{workKey}/editions`

- **Mục đích**: Lấy danh sách editions của một work.
- **Auth**: Bắt buộc.
- **Request body**: Không có.

### Path params
| Tên | Kiểu | Mô tả |
|---|---:|---|
| `workKey` | string | Key work, ví dụ `OL45804W` hoặc `\/works\/OL45804W` |

### Response success: `200 OK`
`EditionsListDTO`

```json
{
  "workKey": "/works/OL45804W",
  "editions": [
    {
      "editionKey": "/editions/OL12345M",
      "isbn": "9780007118359",
      "numberOfPages": 320,
      "publishDate": "2001",
      "publisherName": "HarperCollins"
    }
  ]
}
```

### Response fields
- `workKey` *(string)*: key của work.
- `editions` *(EditionDTO[])*: danh sách edition.

#### `EditionDTO`
- `editionKey` *(string)*: key của edition.
- `isbn` *(string)*: ISBN.
- `numberOfPages` *(int)*: số trang.
- `publishDate` *(string)*: ngày xuất bản.
- `publisherName` *(string)*: tên nhà xuất bản.

### Exception có thể gặp
- `404 Not Found` nếu work không tồn tại (`BookNotFound`)
- `502 Bad Gateway` hoặc status code do upstream trả về nếu Open Library lỗi (`BookApiException`)

---

## 2.4.4 `GET /api/v1/books/editions/{editionKey}`

- **Mục đích**: Lấy chi tiết một edition.
- **Auth**: Bắt buộc.
- **Request body**: Không có.

### Path params
| Tên | Kiểu | Mô tả |
|---|---:|---|
| `editionKey` | string | Key edition, ví dụ `OL12345M` hoặc `\/editions\/OL12345M` |

### Response success: `200 OK`
`EditionDTO`

```json
{
  "editionKey": "/editions/OL12345M",
  "isbn": "9780007118359",
  "numberOfPages": 320,
  "publishDate": "2001",
  "publisherName": "HarperCollins"
}
```

### Response fields
- `editionKey` *(string)*: key của edition.
- `isbn` *(string)*: ISBN.
- `numberOfPages` *(int)*: số trang.
- `publishDate` *(string)*: ngày xuất bản.
- `publisherName` *(string)*: nhà xuất bản.

### Exception có thể gặp
- `404 Not Found` nếu edition không tồn tại (`BookNotFound`)
- `502 Bad Gateway` hoặc status code do upstream trả về nếu Open Library lỗi (`BookApiException`)

---

## 3) DTO reference

## 3.1 DTO public dùng trực tiếp trong API

| DTO | Dùng ở API | Fields |
|---|---|---|
| `RegisterRequest` | `POST /api/v1/auth/register` | `username`, `password`, `email` |
| `LoginRequest` | `POST /api/v1/auth/login` | `email`, `password` |
| `TokenResponse` | response login | `accessToken`, `refreshToken` |
| `UserDto` | response register, response `/api/v1/me` | `email`, `userName` |
| `AuthorDTO` | response search authors | `id`, `name`, `birthDay`, `readCount`, `olKey`, `avatar` |
| `AuthorDetailDTO` | response author detail | `birthDate`, `fullName`, `bio`, `createdAt`, `lastModifiedAt`, `avatar` |
| `WorkDTO` | response author works | `workKey`, `title`, `description`, `coverUrl` |
| `SearchBookDTO` | response search books | `bookKey`, `title`, `authorNames`, `firstPublishYear`, `isbn`, `editionCount`, `coverUrl` |
| `BookDetailDTO` | response work detail | `workKey`, `title`, `description`, `coverUrl`, `authorKeys` |
| `EditionsListDTO` | response work editions | `workKey`, `editions` |
| `EditionDTO` | response edition detail, phần tử trong `EditionsListDTO` | `editionKey`, `isbn`, `numberOfPages`, `publishDate`, `publisherName` |
| `ExceptionResponse` | một số exception handler | `message`, `status` |

---

## 3.2 DTO hỗ trợ nội bộ / mapping từ Open Library

Các DTO dưới đây hiện không được trả trực tiếp từ controller, nhưng dùng để map dữ liệu từ Open Library:

| DTO | Vai trò | Fields chính |
|---|---|---|
| `AuthorListResponse` | response từ Open Library search author | `numFound`, `docs` |
| `OpenLibraryAuthorDTO` | 1 author record của Open Library | `name`, `birthDay`, `key`, `readingCount` |
| `AuthorDetailResponse` | response chi tiết author từ Open Library | `birthDate`, `fullName`, `bio`, `createdAt`, `lastModifiedAt`, `key` |
| `SearchBooksDTO` | wrapper search books từ Open Library | `numFound`, `docs` |
| `SearchBooksDTO.BookSearchEntry` | 1 book record search | `key`, `title`, `authorNames`, `firstPublishYear`, `isbn`, `coverId`, `editionCount`, `coverEditionKey` |
| `OpenLibraryWorkDTO` | response work detail từ Open Library | `key`, `title`, `description`, `covers`, `authorKeys`, `created`, `modify`, ... |
| `OpenLibraryEditionsDTO` | response editions của work | `entries` |
| `OpenLibraryEditionsDTO.EditionEntry` | 1 edition record | `key`, `title`, `isbn13`, `isbn10`, `numberOfPages`, `publishDate`, `publishers` |
| `OpenLibraryEditionDTO` | response detail edition | `key`, `title`, `isbn13`, `isbn10`, `numberOfPages`, `publishDate`, `publishers`, `works` |
| `OpenLibraryEditionDTO.WorkRef` | ref tới work | `key` |
| `AuthorWorksDTO` | response works by author | `entries` |
| `AuthorWorksDTO.WorkEntry` | 1 work record của author | `key`, `title`, `description`, `covers` |

---

## 4) Exception handling

## 4.1 Bảng tổng hợp exception

| Exception | HTTP status | Body response | Handler |
|---|---:|---|---|
| `MethodArgumentNotValidException` | `400 Bad Request` | `{"fieldName": "message"}` | `GlobalExceptionHandler` |
| `EmailExists` | `400 Bad Request` | `ExceptionResponse` | `AuthExceptionHandler` |
| `WrongCredentials` | `401 Unauthorized` | `ExceptionResponse` | `AuthExceptionHandler` |
| `AuthorNotExists` | `404 Not Found` | `ExceptionResponse` hoặc `{"error": "..."}` | `AuthorExceptionHandler` và `GlobalExceptionHandler` |
| `BookNotFound` | `404 Not Found` | `{"error": "..."}` | `GlobalExceptionHandler` |
| `BookApiException` | status code lấy từ exception, fallback `500` | `{"error": "...", "statusCode": n}` | `GlobalExceptionHandler` |
| `Exception` (fallback) | `500 Internal Server Error` | `{"error": "An unexpected error occurred. Please try again later."}` | `GlobalExceptionHandler` |

## 4.2 Ví dụ body lỗi

### Validation error (`MethodArgumentNotValidException`)

```json
{
  "email": "must be a well-formed email address",
  "password": "password must be at least 6 characters"
}
```

### `EmailExists`

```json
{
  "message": "Email already exists",
  "status": "BAD_REQUEST"
}
```

### `WrongCredentials`

```json
{
  "message": "email or password are wrong",
  "status": "UNAUTHORIZED"
}
```

### `BookNotFound`

```json
{
  "error": "Work with key /works/OL45804W not found."
}
```

### `BookApiException`

```json
{
  "error": "Failed to fetch work details: ...",
  "statusCode": 502
}
```

### Fallback exception

```json
{
  "error": "An unexpected error occurred. Please try again later."
}
```

## 4.3 Ghi chú quan trọng về `AuthorNotExists`

Hiện tại có **2 handler** cho cùng một exception `AuthorNotExists`:

1. `AuthorExceptionHandler` trả về `ExceptionResponse` với `404`.
2. `GlobalExceptionHandler` cũng có handler `AuthorNotExists` nhưng trả về map `{"error": ...}`.

Do không thấy cấu hình `@Order`/scoping rõ ràng trong code hiện tại, response thực tế có thể phụ thuộc vào cách Spring resolve handler. Khi tài liệu hóa hoặc refactor, nên hợp nhất về **một** format duy nhất để tránh ambiguity.

## 4.4 Lỗi authentication / authorization

- Các endpoint trừ `POST /api/v1/auth/register` và `POST /api/v1/auth/login` đều cần JWT.
- Lỗi thiếu token, token không hợp lệ hoặc không được xác thực bởi Spring Security **không có custom body riêng** trong các file handler đã đọc.
- Response cụ thể sẽ phụ thuộc vào Spring Security/JWT filter runtime.

---

## 5) Tóm tắt nhanh các endpoint

| Method | URL | Auth | Response chính |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Public | `UserDto` |
| `POST` | `/api/v1/auth/login` | Public | `TokenResponse` |
| `GET` | `/api/v1/me` | Bearer token | `UserDto` |
| `GET` | `/api/v1/author/search` | Bearer token | `List<AuthorDTO>` |
| `GET` | `/api/v1/author/{olkey}` | Bearer token | `AuthorDetailDTO` |
| `GET` | `/api/v1/author/{authorKey}/works` | Bearer token | `List<WorkDTO>` |
| `GET` | `/api/v1/books/search` | Bearer token | `List<SearchBookDTO>` |
| `GET` | `/api/v1/books/works/{workKey}` | Bearer token | `BookDetailDTO` |
| `GET` | `/api/v1/books/works/{workKey}/editions` | Bearer token | `EditionsListDTO` |
| `GET` | `/api/v1/books/editions/{editionKey}` | Bearer token | `EditionDTO` |

---

## 6) Lưu ý triển khai

- Các endpoint sách/tác giả đang map dữ liệu từ Open Library, nên một số field có thể trống hoặc `null` tùy upstream.
- `AuthorController.searchAuthors` nhận `page`, `sortBy`, `direction` nhưng hiện tại chưa dùng chúng trong service.
- `BookController` normalizes key dạng `/works/...`, `/editions/...`, `/authors/...` về canonical key trước khi lookup.
- Response `description` trong một số DTO có thể đến từ Open Library dưới nhiều dạng, nên backend đã chuẩn hóa thành chuỗi.

