package org.example.bookreadingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.reading.ChapterDto;
import org.example.bookreadingapp.entity.Chapter;
import org.example.bookreadingapp.service.ReadingResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reading-resources")
@RequiredArgsConstructor
public class ReadingResourceController {
    private final ReadingResourceService readingResourceService;

    @GetMapping("/{resource_id}")
    public ResponseEntity<List<ChapterDto>> getChapters(@PathVariable String resource_id) {
        return ResponseEntity.ok(readingResourceService.getChaptersByReadingResourceId(resource_id));
    }
}
