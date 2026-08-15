package org.example.bookreadingapp.service.provider;


import org.example.bookreadingapp.dto.book.ProviderWorkPage;
import org.example.bookreadingapp.dto.book.SearchBookDTO;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.entity.Work;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchProvider<T> {
    List<SearchBookDTO> search(String query, int page, int limit);
    List<SearchBookDTO> mapEntriesToSearchDTO(List<T> data);

    ProviderWorkPage getWorksByAuthor(AuthorDetail authorDetail, int page, int limit);
}
