# Quick Start: Testing Book APIs

## Running the Application

### Prerequisites
1. Java 17+
2. PostgreSQL (or H2 for testing)
3. Maven (or use `mvnw.cmd`)

### Start the Application

```bash
cd "F:\Dai hoc\Java\BookReadingApp"
.\mvnw.cmd spring-boot:run
```

The server will start on `http://localhost:8081`

---

## Testing the APIs

### Test 1: Get Book Work Details (Fresh Request)

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W" \
  -H "Content-Type: application/json"
```

**Expected Response (200 OK):**
```json
{
  "workKey": "/works/OL82563W",
  "title": "Harry Potter and the Philosopher's Stone",
  "description": "Harry Potter has never been the star of a Quidditch team, wasn't voted the most popular student in school, and can't walk into a room without the other employees of the Leaky Cauldron Harry is a wizard — a wizard in a magical world behind the scenes of an ordinary one, a world full of many colourful characters and villains and, most importantly, a world that really arms children against cheating by knowing how to take care of themselves.",
  "coverUrl": "https://covers.openlibrary.org/b/id/7725435-M.jpg",
  "authorKeys": ["/authors/OL34221A"]
}
```

**What happens internally:**
1. ✅ Database checked (no cache)
2. ✅ Open Library API called
3. ✅ Data returned immediately (200 OK)
4. ✅ Database save starts in background

---

### Test 2: Get Same Book Again (Cached Request)

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W" \
  -H "Content-Type: application/json"
```

**Expected Response (200 OK):** Same as Test 1, but faster!

**What happens internally:**
1. ✅ Database checked (cache HIT!)
2. ✅ Data returned from database immediately
3. ✅ No API call made
4. ✅ Much faster response time (~50ms vs ~500ms)

---

### Test 3: Get Book Editions

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W/editions" \
  -H "Content-Type: application/json"
```

**Expected Response (200 OK):**
```json
{
  "workKey": "/works/OL82563W",
  "editions": [
    {
      "editionKey": "/editions/OL7723857M",
      "isbn": "0439708184",
      "numberOfPages": 223,
      "publishDate": "1997-06-26",
      "publisherName": "Bloomsbury Publishing Plc"
    },
    {
      "editionKey": "/editions/OL7723858M",
      "isbn": "0747543828",
      "numberOfPages": 223,
      "publishDate": "1997-06-21",
      "publisherName": "Bloomsbury"
    }
    // ... more editions
  ]
}
```

---

### Test 4: Get Non-existent Book (404 Error)

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL99999999X" \
  -H "Content-Type: application/json"
```

**Expected Response (404 Not Found):**
```json
{
  "error": "Work with ID OL99999999X not found in Open Library."
}
```

**HTTP Status:** `404 Not Found`

---

### Test 5: Testing in PostMan

1. **Create New Request**
   - Method: `GET`
   - URL: `http://localhost:8081/api/v1/books/works/OL82563W`

2. **Headers Tab:**
   - Key: `Content-Type`
   - Value: `application/json`

3. **Click Send**
   - Response appears in bottom panel

---

## Valid OpenLibrary Work IDs to Test

| Book | Work ID | Notes |
|------|---------|-------|
| Harry Potter 1 | OL82563W | Very popular, large data |
| The Great Gatsby | OL45883W | Classic book |
| 1984 | OL45407W | Dystopian novel |
| Pride and Prejudice | OL45883W | Classic |
| The Hobbit | OL3031726W | Fantasy |

---

## Observing Logs

When running the application, check the console logs:

### First Request (API Call)
```
INFO - Fetching book detail for workId: OL82563W
INFO - Successfully fetched work from Open Library: OL82563W
INFO - Starting async save for work: /works/OL82563W
INFO - Successfully saved work to database: /works/OL82563W
```

### Second Request (Database Hit)
```
INFO - Fetching book detail for workId: OL82563W
INFO - Found work in database: OL82563W
```

### Not Found Case
```
ERROR - Work not found in Open Library: OL99999999X
```

---

## Using Other Tools

### Using VSCode REST Client Extension

Create a file `test.http`:
```http
### Test 1: Get book details
GET http://localhost:8081/api/v1/books/works/OL82563W
Content-Type: application/json

### Test 2: Get editions
GET http://localhost:8081/api/v1/books/works/OL82563W/editions
Content-Type: application/json

### Test 3: Test 404
GET http://localhost:8081/api/v1/books/works/OL99999999X
Content-Type: application/json
```

Then press "Send Request" to test each one.

---

## Monitoring Database

### Check if Data was Saved

```sql
-- Connect to PostgreSQL
psql -U postgres -d bookreadingapp

-- See saved works
SELECT * FROM works;

-- See saved editions
SELECT * FROM editions;

-- Count works cached
SELECT COUNT(*) as total_works FROM works;
```

---

## Troubleshooting

### Issue: 404 Not Found from API
**Cause:** Invalid work ID
**Solution:** Use valid OpenLibrary work IDs (e.g., OL82563W)

### Issue: Connection Timeout
**Cause:** OpenLibrary API is slow or unreachable
**Solution:** Check internet connection, try again

### Issue: Database Connection Error
**Cause:** PostgreSQL not running or wrong credentials
**Solution:** Check `application.yml` environment variables

### Issue: Very Slow Response
**Cause:** First request, waiting for API
**Solution:** This is normal for first request (~500ms). Second request will be fast.

---

## Performance Benchmarks

With Cache-Aside Pattern:

| Metric | First Request | Cached Request |
|--------|---------------|-----------------|
| API Calls | ✅ Yes | ❌ No |
| Database Used | ✅ Write (async) | ✅ Read (sync) |
| Response Time | ~500ms | ~50ms |
| Data Source | Open Library | PostgreSQL |

---

## Next API Calls to Implement (Future)

```bash
# Search for books
GET /api/v1/books/search?q=harry+potter&page=1

# Get books by subject/genre
GET /api/v1/books/subjects/science_fiction

# Get multiple works at once
POST /api/v1/books/works/batch
Body: { "workIds": ["OL82563W", "OL45883W"] }
```

---

## Debugging Tips

### Enable Debug Logging

Add to `application.yml`:
```yaml
logging:
  level:
    org.example.bookreadingapp: DEBUG
    org.springframework.web: DEBUG
```

### Check HTTP Requests/Responses

Use browser DevTools or Postman Network tab to see:
- Request headers
- Response headers
- Response time
- Response size

### Verify Async Execution

Look for log patterns:
```
[thread-name] INFO - Fetching book detail  <- Main thread
[SimpleAsyncTaskExecutor-N] INFO - Successfully saved  <- Async thread
```

---

## Important Notes

⚠️ **First Request is Slow**: The first request to a work will be ~500ms because it calls Open Library API and saves asynchronously.

✅ **Subsequent Requests are Fast**: Cached requests will be ~50ms from database.

✅ **Async Safe**: Database saves don't block the response. You get data immediately.

⚠️ **Timeout Risk**: If Open Library API is slow (>30s), the request will timeout. This is normal for public APIs.

✅ **Graceful Handling**: If API is down, you get a 503 error with proper error message.

---

## Support

For more details, see:
- `IMPLEMENTATION_GUIDE.md` - Complete API documentation
- `IMPLEMENTATION_SUMMARY.md` - Architecture & design overview
- Source code comments in `BookService.java`


