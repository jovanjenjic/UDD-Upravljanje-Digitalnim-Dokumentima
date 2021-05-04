package com.example.udd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetaReaderDTO {

    private double latitude;
    private double longitude;

    private Long readerId;

    private String email;
    private String username;
}
