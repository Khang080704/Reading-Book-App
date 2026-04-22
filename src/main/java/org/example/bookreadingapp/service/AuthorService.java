package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.client.AuthorApiClient;
import org.example.bookreadingapp.dto.AuthorListResponse;
import org.example.bookreadingapp.dto.OpenLibraryAuthorDTO;
import org.example.bookreadingapp.entity.Author;
import org.example.bookreadingapp.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorApiClient authorApiClient;

    public List<Author> getAuthors(String authorName) {
        List<Author> result = new ArrayList<>();

        result = authorRepository.findByNameContainingIgnoreCase(authorName);
        System.out.println(result);
        if(!result.isEmpty()){
            return result;
        }

        AuthorListResponse response = authorApiClient.getAuthors(authorName);

        if(response.getNumFound() == 0 || response.getDocs().isEmpty()) {
            return result;
        }




        for(OpenLibraryAuthorDTO author : response.getDocs()) {
            Optional<Author> existingAuthor = authorRepository.findByOlKey(author.getKey());
            if(existingAuthor.isPresent()) {
                result.add(existingAuthor.get());
            }
            else {
                Author authorEntity = new Author();
                authorEntity.setName(author.getName());
                authorEntity.setBirthDay(author.getBirthDay());
                authorEntity.setOlKey(author.getKey());
                authorEntity.setReadCount(author.getReadingCount());
                authorRepository.save(authorEntity);
                result.add(authorEntity);
            }


        }

        return result;
    }
}
