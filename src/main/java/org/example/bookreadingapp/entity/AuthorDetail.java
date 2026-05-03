package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "authorDetail")
    private Author author;

    private String birthDay;
    private String website;
    private String fullName;
    @Column(length = 2000)
    private String bio;

    private LocalDateTime createdAt;
    private LocalDateTime lastModify;



}
