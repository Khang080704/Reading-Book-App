package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.client.AuthorApiClient;
import org.example.bookreadingapp.dto.*;
import org.example.bookreadingapp.entity.Author;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.exception.definitions.AuthorNotExists;
import org.example.bookreadingapp.repository.AuthorRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorApiClient authorApiClient;

    public List<AuthorDTO> searchAuthors(String authorName) {
        List<Author> result = authorRepository.findByNameContainingIgnoreCase(authorName);
        if(!result.isEmpty()){
            return getAuthorDTOS(result);
        }

        AuthorListResponse response = authorApiClient.getAuthors(authorName);

        if(response.getNumFound() == 0 || response.getDocs().isEmpty()) {
            return getAuthorDTOS(result);
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

        return getAuthorDTOS(result);
    }

    private List<AuthorDTO> getAuthorDTOS(List<Author> result) {
        return result.stream().map(author -> {
            AuthorDTO dto = new AuthorDTO();
            dto.setId(author.getId());
            dto.setName(author.getName());
            dto.setBirthDay(author.getBirthDay());
            dto.setReadCount(author.getReadCount());
            dto.setOlKey(author.getOlKey());
            return dto;
        }).toList();
    }

    public List<AuthorDTO> getAuthors(PageRequest pageRequest) {
        List<Author> authorList = authorRepository.findAll(pageRequest).getContent();
        log.info("Fetched authors from database: {}", authorList);
        return getAuthorDTOS(authorList);
    }

    public AuthorDetailDTO getAuthorDetail(String olKey) throws AuthorNotExists {
        Optional<Author> author = authorRepository.findByOlKey(olKey);
        if(author.isPresent()) {
            AuthorDetailDTO detailDTO = new AuthorDetailDTO();
            Author authorEntity = author.get();

            if(authorEntity.getAuthorDetail() != null) {
                detailDTO.setBio(authorEntity.getAuthorDetail().getBio());
                detailDTO.setCreatedAt(authorEntity.getAuthorDetail().getCreatedAt());
                detailDTO.setFullName(authorEntity.getAuthorDetail().getFullName());
                detailDTO.setBirthDate(authorEntity.getAuthorDetail().getBirthDay());
                detailDTO.setLastModifiedAt(authorEntity.getAuthorDetail().getLastModify());
                return detailDTO;
            }

            AuthorDetailResponse authorDetailResponse = authorApiClient.getAuthorDetail(olKey);
            log.info("Fetched author detail from API for olKey {}: {}", olKey, authorDetailResponse);
            log.info("Bio length: {}", authorDetailResponse.getBio().length());

            AuthorDetail authorDetail = new AuthorDetail();
            authorDetail.setBirthDay(authorDetailResponse.getBirthDate());
            authorDetail.setBio(authorDetailResponse.getBio() == null ? "Bio is updating" : authorDetailResponse.getBio());
            authorDetail.setFullName(authorDetailResponse.getFullName());
            authorDetail.setCreatedAt(LocalDateTime.parse(authorDetailResponse.getCreatedAt()));
            authorDetail.setLastModify(LocalDateTime.parse(authorDetailResponse.getLastModifiedAt()));

            authorEntity.setAuthorDetail(authorDetail);

            detailDTO.setBio(authorDetail.getBio());
            detailDTO.setFullName(authorDetail.getFullName());
            detailDTO.setBirthDate(authorDetail.getBirthDay());
            detailDTO.setCreatedAt(authorDetail.getCreatedAt());
            detailDTO.setLastModifiedAt(authorDetail.getLastModify());

            authorRepository.save(authorEntity);


            return detailDTO;
        }
        else {
            throw new AuthorNotExists("Author with olKey " + olKey + " does not exist.");
        }
    }
}
