package org.example.bookreadingapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.reading.ChapterDto;
import org.example.bookreadingapp.service.ChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chapters")
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping("/{chapter_id}")
    public ResponseEntity<ChapterDto> getChapterContent(@PathVariable String chapter_id) {
        return ResponseEntity.ok(chapterService.getChapterById(chapter_id));
    }
}
