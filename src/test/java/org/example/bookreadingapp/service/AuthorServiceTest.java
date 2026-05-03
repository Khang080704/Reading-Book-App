package org.example.bookreadingapp.service;

import org.example.bookreadingapp.client.AuthorApiClient;
import org.example.bookreadingapp.dto.AuthorDTO;
import org.example.bookreadingapp.entity.Author;
import org.example.bookreadingapp.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorApiClient authorApiClient;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void getAuthors_should_return_paginated_authors_from_repository() {
        PageRequest pageRequest = PageRequest.of(1, 2, Sort.by("name").descending());
        List<Author> authors = List.of(author("1", "Author A"), author("2", "Author B"));

        when(authorRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(authors, pageRequest, 10));

        List<AuthorDTO> result = authorService.getAuthors(pageRequest);


        verify(authorRepository).findAll(pageRequest);
    }

    private Author author(String id, String name) {
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        return author;
    }
}

