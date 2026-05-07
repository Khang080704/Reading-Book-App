package org.example.bookreadingapp.client;

import org.example.bookreadingapp.dto.author.AuthorDetailResponse;
import org.example.bookreadingapp.dto.author.AuthorListResponse;
import org.example.bookreadingapp.dto.book.AuthorWorksDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authorApiClient", url = "https://openlibrary.org")
public interface AuthorApiClient {
    @GetMapping("/search/authors.json")
    AuthorListResponse getAuthors(@RequestParam String q);

    @GetMapping("/authors/{olkey}.json")
    AuthorDetailResponse getAuthorDetail(@PathVariable String olkey);

    /**
     * Get works by an author
     * API: https://openlibrary.org/dev/docs/api/authors (Works by an Author section)
     * Example: /authors/OL34221A/works.json
     * 
     * @param authorKey the author key (e.g., OL34221A or /authors/OL34221A)
     * @return works list by the author
     */
    @GetMapping("/authors/{authorKey}/works.json")
    AuthorWorksDTO getAuthorWorks(@PathVariable String authorKey);
}
