package org.example.bookreadingapp.client;

import org.example.bookreadingapp.dto.AuthorDetailResponse;
import org.example.bookreadingapp.dto.AuthorListResponse;
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
}
