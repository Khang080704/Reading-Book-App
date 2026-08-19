package org.example.bookreadingapp.service.provider;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.book.ProviderWorkPage;
import org.example.bookreadingapp.dto.book.SearchBookDTO;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.repository.WorkRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("database")
@RequiredArgsConstructor
public class DatabaseProvider implements SearchProvider<Work> {
    private final WorkRepository workRepository;


    @Override
    public List<SearchBookDTO> search(String query, int page, int limit) {
        List<Work> works = workRepository.findAll();
        return mapEntriesToSearchDTO(works);
    }

    @Override
    public List<SearchBookDTO> mapEntriesToSearchDTO(List<Work> data) {
        if(data == null || data.isEmpty()) {
            return new ArrayList<>();
        }
        return data.stream().map(item ->
                SearchBookDTO.builder()
                        .bookKey(item.getWorkKey())
                        .title(item.getTitle())
                        .authorNames(item.getAuthors().stream()
                                .map(AuthorDetail::getFullName)
                                .toArray(String[]::new))
                        .editionCount(item.getEditions().size())
                        .coverUrl(item.getCoverId() != null ? "https://covers.openlibrary.org/b/id/" + item.getCoverId() + "-M.jpg" : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ProviderWorkPage getWorksByAuthor(AuthorDetail authorDetail, int page, int limit) {
        return null;
    }


}
