package com.example.udd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class DocumentDTO {

    private MultipartFile file;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String isbn;
    private String genre;
    private Long authorId;
    private double latitude;
    private double longitude;

}
