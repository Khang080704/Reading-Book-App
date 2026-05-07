# Book APIs Implementation - Cache-Aside Pattern

## Overview

This implementation adds two new book-related endpoints to the Book Reading App with the following features:

- **Cache-Aside Pattern**: Checks database first before calling Open Library API
- **Asynchronous Saving**: Non-blocking database saves so API responses are fast
- **Comprehensive Error Handling**: Proper handling of API timeouts and 404 responses

## New Endpoints

### 1. Get Book Work Details
```
GET /api/v1/books/works/{workId}
```

**Description**: Retrieve detailed information about a book work from Open Library, with caching.

**Parameters**:
- `workId` (path): OpenLibrary work ID (e.g., `OL82563W`)

**Response** (200 OK):
```json
{
  "workKey": "OL82563W",
  "title": "Harry Potter and the Philosopher's Stone",
  "description": "The first book in the Harry Potter series...",
  "coverUrl": "https://covers.openlibrary.org/b/id/7725435-M.jpg",
  "authorKeys": ["/authors/OL34221A"]
}
```

**Error Responses**:
- `404 Not Found`: Work not found in Open Library
- `500 Internal Server Error`: API call failed or timeout

**Example Request**:
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W"
```

---

### 2. Get Book Editions
```
GET /api/v1/books/works/{workId}/editions
```

**Description**: Retrieve all editions/versions of a particular work with ISBN, page count, publisher info, etc.

**Parameters**:
- `workId` (path): OpenLibrary work ID (e.g., `OL82563W`)

**Response** (200 OK):
```json
{
  "workKey": "OL82563W",
  "editions": [
    {
      "editionKey": "OL7723857M",
      "isbn": "0439708184",
      "numberOfPages": 223,
      "publishDate": "1997-06-26",
      "publisherName": "Bloomsbury Publishing Plc"
    },
    {
      "editionKey": "OL7723858M",
      "isbn": "0747532699",
      "numberOfPages": 223,
      "publishDate": "1997-06-26",
      "publisherName": "Bloomsbury"
    }
  ]
}
```

**Error Responses**:
- `404 Not Found`: Editions not found in Open Library
- `500 Internal Server Error`: API call failed or timeout

**Example Request**:
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W/editions"
```

---

## Architecture

### Cache-Aside Pattern Flow

```
1. Request arrives at endpoint
   ↓
2. Check if data exists in Database
   ├─ YES → Return cached data immediately
   └─ NO → 
      3. Call Open Library API
      4. Return API response to client
      5. Asynchronously save to database
         (background task, doesn't block response)
```

### Database Schema

#### `works` table
| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| work_key | VARCHAR(255) | Unique work identifier from Open Library |
| title | TEXT | Book title |
| description | TEXT | Book description |
| cover_id | VARCHAR(255) | Cover image ID |
| author_keys | TEXT | Comma-separated author keys |

#### `editions` table
| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| edition_key | VARCHAR(255) | Unique edition identifier |
| isbn | VARCHAR(255) | ISBN number |
| number_of_pages | INT | Number of pages |
| publish_date | VARCHAR(255) | Publication date |
| publisher_name | VARCHAR(255) | Publisher name |
| work_id | UUID | Foreign key to works table |

---

## Exception Handling

### BookNotFound (404)
Thrown when:
- Work not found in Open Library
- Edition not found in Open Library

Response:
```json
{
  "error": "Work with ID OL00000X not found in Open Library."
}
```

### BookApiException
Thrown when:
- API call fails (connection error, timeout)
- API returns error status (5xx, etc.)

Response:
```json
{
  "error": "Failed to fetch work from Open Library: ...",
  "statusCode": 503
}
```

---

## Implementation Details

### Entities

#### Work.java
- Maps `works` table in database
- Stores work metadata: title, description, cover ID, author keys
- One-to-Many relationship with Edition

#### Edition.java
- Maps `editions` table in database
- Stores edition metadata: ISBN, page count, publisher, publish date
- Many-to-One relationship with Work

### Services

#### BookService.java
Main service implementing Cache-Aside pattern:

```java
public BookDetailDTO getBookDetail(String workId) {
    // 1. Check DB first
    // 2. If not found, call API
    // 3. Save asynchronously
    // 4. Return result to client
}

public EditionsListDTO getEditions(String workId) {
    // Similar flow for editions
}

@Async
protected void saveWorkAsync(OpenLibraryWorkDTO dto) {
    // Asynchronous database save
}

@Async
protected void saveEditionsAsync(String workKey, OpenLibraryEditionsDTO dto) {
    // Asynchronous database save
}
```

Key annotations:
- `@Async`: Enables asynchronous execution (requires `@EnableAsync` on main class)
- Non-blocking saves improve API response times
- Exceptions logged but not thrown (async context)

### API Client

#### BookApiClient.java
OpenFeign client for calling Open Library:

```java
@FeignClient(name = "bookApiClient", url = "https://openlibrary.org")
public interface BookApiClient {
    @GetMapping("/works/{workId}.json")
    OpenLibraryWorkDTO getWorkDetail(@PathVariable String workId);

    @GetMapping("/works/{workId}/editions.json")
    OpenLibraryEditionsDTO getEditions(@PathVariable String workId);
}
```

---

## Error Handling Flow

```
API Request
   ↓
BookService method called
   ↓
Check Database
   ├─ Found → Return DTO
   └─ Not Found →
      Call BookApiClient
      ├─ Success (200) → Save Async + Return DTO
      ├─ Not Found (404) → Throw BookNotFound
      ├─ API Error → Throw BookApiException
      └─ Network Error → Throw BookApiException
         ↓
      GlobalExceptionHandler catches exception
      ├─ BookNotFound → Return 404 with error message
      ├─ BookApiException → Return error code + message
      └─ Other Exception → Return 500 error
```

---

## Testing Examples

### Test 1: Fetch book detail (first time - from API)
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W"
```
**Result**: API is called, data is returned, and DB save happens in background

### Test 2: Fetch book detail (second time - from cache)
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W"
```
**Result**: Data is returned instantly from database (no API call)

### Test 3: Fetch non-existent book
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL99999999X"
```
**Result**: Returns 404 with error message

### Test 4: Fetch editions
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W/editions"
```
**Result**: All editions are returned (from DB cache or API)

---

## Performance Considerations

### Benefits of Cache-Aside Pattern
- **First request**: Slow (API call required)
- **Subsequent requests**: Fast (database cache hit)
- **Asynchronous saves**: Response time not affected by database writes
- **Reduced API calls**: Fewer requests to Open Library API

### Database Indexes
Recommended indexes for performance:
```sql
CREATE UNIQUE INDEX idx_work_key ON works(work_key);
CREATE INDEX idx_edition_work ON editions(work_id);
```

---

## Logs

The implementation includes comprehensive logging:

```
INFO - Fetching book detail for workId: OL82563W
INFO - Found work in database: OL82563W
INFO - Fetching editions for workId: OL82563W
INFO - Successfully fetched 5 editions from Open Library for workId: OL82563W
INFO - Starting async save for editions of work: OL82563W
INFO - Successfully saved 5 editions to database for work: OL82563W
```

---

## Dependencies

The following dependencies are used (already in pom.xml):
- `spring-boot-starter-data-jpa`: For JPA/Hibernate ORM
- `spring-cloud-starter-openfeign`: For OpenFeign API client
- `spring-boot-starter-webmvc`: For REST controllers
- `postgresql`: For PostgreSQL database driver
- `lombok`: For @Async annotation support

---

## Environment Setup

Make sure PostgreSQL environment variables are set:
```bash
POSTGRES_URL=jdbc:postgresql://localhost:5432/bookreadingapp
POSTGRES_USER=postgres
POSTGRES_PASSWORD=yourpassword
```

And enable async processing is already done in `BookReadingAppApplication.java`:
```java
@EnableAsync
@EnableFeignClients
@SpringBootApplication
public class BookReadingAppApplication { ... }
```

---

## Future Enhancements

1. Add caching decorator (Redis) for even faster responses
2. Implement retry logic with exponential backoff for API failures
3. Add rate limiting for Open Library API calls
4. Implement batch editions saving for better performance
5. Add search functionality with `GET /api/v1/books/search`
6. Add subject/genre filtering with `GET /api/v1/books/subjects/{subject}`


