package org.example.bookreadingapp.dto.author;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO implements Serializable {
    private String id;
    private String name;
    private String birthDay;
    private int readCount;
    private String olKey;
    private String avatar;
}
