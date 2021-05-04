package com.example.udd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointDTO {

    private double longitude;
    private double latitude;

    private int distance;
}
