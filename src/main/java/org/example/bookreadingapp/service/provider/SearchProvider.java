package org.example.bookreadingapp.service.provider;


import org.example.bookreadingapp.dto.book.SearchBookDTO;

import java.util.List;

public interface SearchProvider<T> {
    List<SearchBookDTO> search(String query, int page, int limit);
    List<SearchBookDTO> mapEntriesToSearchDTO(List<T> data);
}
