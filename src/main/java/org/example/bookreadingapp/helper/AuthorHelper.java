package org.example.bookreadingapp.helper;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.repository.AuthorDetailRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthorHelper {
    private final AuthorDetailRepository authorDetailRepository;

    @Async
    public synchronized void saveAuthorDetail(AuthorDetail data) {
        authorDetailRepository.save(data);
    }
}
