# 📲 Quick Reference Card - Book APIs

## API Endpoints Summary

### Get Book Details
```
GET /api/v1/books/works/{workId}

Example:
GET /api/v1/books/works/OL82563W

Response (200):
{
  "workKey": "/works/OL82563W",
  "title": "Harry Potter and the Philosopher's Stone",
  "description": "...",
  "coverUrl": "https://covers.openlibrary.org/...",
  "authorKeys": ["/authors/OL34221A"]
}

Error (404): { "error": "Work not found..." }
```

---

### Get Editions
```
GET /api/v1/books/works/{workId}/editions

Example:
GET /api/v1/books/works/OL82563W/editions

Response (200):
{
  "workKey": "/works/OL82563W",
  "editions": [
    {
      "editionKey": "/editions/OL7723857M",
      "isbn": "0439708184",
      "numberOfPages": 223,
      "publishDate": "1997-06-26",
      "publisherName": "Bloomsbury"
    }
  ]
}

Error (404): { "error": "Editions not found..." }
```

---

## 🔄 Cache-Aside Pattern

```
Request → Check Database
         ├─ Found → Return (50ms, cached)
         └─ Not found → Hit API (500ms)
              ↓
         Return response
              ↓
         Save to DB asynchronously (background)
```

**Key points:**
- ✅ Subsequent requests are 10x faster
- ✅ Response returned immediately (no waiting for save)
- ✅ Fewer API calls to external service
- ✅ Always fresh on first load

---

## 📝 Examples

### cURL - Get Book Details
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W" \
  -H "Content-Type: application/json"
```

### cURL - Get Editions  
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W/editions" \
  -H "Content-Type: application/json"
```

### Postman
1. New request → GET
2. URL: `http://localhost:8081/api/v1/books/works/OL82563W`
3. Headers: `Content-Type: application/json`
4. Send

### Python
```python
import requests

url = "http://localhost:8081/api/v1/books/works/OL82563W"
response = requests.get(url)
print(response.json())
```

### JavaScript
```javascript
fetch("http://localhost:8081/api/v1/books/works/OL82563W")
  .then(r => r.json())
  .then(data => console.log(data))
```

---

## 🚀 Performance

| Metric | Value |
|--------|-------|
| First request | ~500ms (API) |
| Cached request | ~50ms (DB) |
| Speedup | **10x faster** |
| Async save | Background |

---

## 🧪 Test Data (Valid Work IDs)

| Title | ID |
|-------|-----|
| Harry Potter 1 | OL82563W |
| The Great Gatsby | OL45883W |
| 1984 | OL45407W |
| Pride and Prejudice | OL45883W |
| The Hobbit | OL3031726W |

---

## ⚙️ Commands

### Start Application
```bash
cd F:\Dai hoc\Java\BookReadingApp
.\mvnw.cmd spring-boot:run
```

### Run Tests
```bash
.\mvnw.cmd test
```

### Compile Only
```bash
.\mvnw.cmd compile -DskipTests
```

### Clean Build
```bash
.\mvnw.cmd clean install
```

---

## 📂 Key Files

| File | Purpose |
|------|---------|
| `BookService.java` | Cache-Aside logic |
| `BookController.java` | REST endpoints |
| `BookApiClient.java` | Open Library client |
| `Work.java` | Work entity |
| `Edition.java` | Edition entity |
| `GlobalExceptionHandler.java` | Error handling |

---

## 🔐 Error Codes

| Status | Meaning | Response |
|--------|---------|----------|
| 200 | Success | Book/edition data |
| 404 | Not found | `{ "error": "Work not found..." }` |
| 503 | API error | `{ "error": "Failed to fetch...", "statusCode": 503 }` |
| 500 | Server error | `{ "error": "Unexpected error..." }` |

---

## 📊 HTTP Status Codes

- **200 OK** - Request successful, data returned
- **404 Not Found** - Book/edition not found in Open Library
- **503 Service Unavailable** - Open Library API unavailable
- **500 Internal Server Error** - Unexpected server error

---

## 🗄️ Database Info

### Connect to PostgreSQL
```sql
psql -U postgres -d bookreadingapp

-- Check works
SELECT * FROM works;

-- Check editions  
SELECT * FROM editions;

-- Check saved count
SELECT COUNT(*) FROM works;
```

---

## 📋 Request/Response Structure

### Request Headers
```
Content-Type: application/json
```

### Response Headers
```
Content-Type: application/json;charset=UTF-8
Date: [timestamp]
Transfer-Encoding: chunked
```

### Error Response Format
```json
{
  "error": "Description of what went wrong"
}
```

---

## 🔍 Debugging

### Check Logs
- Look for `INFO` messages: normal operations
- Look for `ERROR` messages: failures
- Look for `[SimpleAsyncTaskExecutor]`: async operations

### Browser DevTools
- Network tab: See requests/responses
- Console: Check for errors
- Response: Verify JSON format

### Database Check
```sql
-- See if data was saved
SELECT work_key, title FROM works LIMIT 5;

-- Check specific work
SELECT * FROM works WHERE work_key = '/works/OL82563W';
```

---

## ⚡ Performance Tips

1. **Use cached requests** - Same work ID = faster response
2. **Batch requests** - Avoid repeated API calls
3. **Check logs** - Monitor async save progress
4. **Database indexes** - Already configured on work_key

---

## ❌ Common Issues

### 404 Error
**Problem**: Book not found  
**Solution**: Check work ID is correct (e.g., OL82563W)

### 503 Error  
**Problem**: Open Library API unavailable  
**Solution**: Wait a moment, try again

### Slow First Request
**Problem**: Takes ~500ms  
**Solution**: This is normal! Async save = fast response

### Database Connection Error
**Problem**: Cannot connect to PostgreSQL  
**Solution**: Check POSTGRES_URL, username, password in env vars

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `README_IMPLEMENTATION.md` | Overview & features |
| `IMPLEMENTATION_GUIDE.md` | Detailed API docs |
| `TESTING_GUIDE.md` | How to test |
| `IMPLEMENTATION_SUMMARY.md` | Architecture |
| `IMPLEMENTATION_CHECKLIST.md` | Complete checklist |
| `QUICK_REFERENCE.md` | This file! |

---

## 🎯 Common Tasks

### Test API is Working
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL82563W"
# Should return book details (200 OK)
```

### Test Error Handling
```bash
curl -X GET "http://localhost:8081/api/v1/books/works/OL99999999X"
# Should return 404 error
```

### Check Database Was Updated
```bash
# After first request, data should be in DB
SELECT COUNT(*) FROM works;
# Should return: 1 (or more)
```

### Monitor Async Operations
```bash
# Check application console logs
# Look for: "Starting async save" and "Successfully saved"
```

---

## 💡 Pro Tips

1. **Use Postman Collections** - Save API calls for quick testing
2. **Set up saved requests** - Bookmark common work IDs
3. **Monitor response times** - Postman shows request duration
4. **Check database after requests** - Verify async save worked
5. **Review logs during testing** - Understand the flow

---

## ✅ Success Checklist

Before deploying:
- [ ] Application starts without errors
- [ ] First request returns 200 with data
- [ ] Second request is faster (cached)
- [ ] 404 error works correctly
- [ ] Database tables created
- [ ] Tests pass

---

## 🔗 External Resources

- **Open Library API**: https://openlibrary.org/api/docs/books
- **Open Library Search**: https://openlibrary.org/search
- **Work ID Format**: `/works/OL{number}W`

---

## 📞 Support

- Check `TESTING_GUIDE.md` for troubleshooting
- Review `IMPLEMENTATION_GUIDE.md` for architecture
- Check `BookService.java` comments for code details

---

**Last Updated**: May 7, 2026  
**Status**: ✅ Ready to Use  
**Print this for quick reference!** 📋


