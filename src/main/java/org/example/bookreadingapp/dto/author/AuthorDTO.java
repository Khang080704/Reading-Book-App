package org.example.bookreadingapp.dto.author;

import lombok.Data;

@Data
public class AuthorDTO {
    private String id;
    private String name;
    private String birthDay;
    private int readCount;
    private String olKey;
    private String avatar;
}
