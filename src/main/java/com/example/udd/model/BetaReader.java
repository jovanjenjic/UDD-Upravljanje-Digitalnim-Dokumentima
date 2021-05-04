package com.example.udd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@NoArgsConstructor
public class BetaReader {
    public static final String INDEX_NAME = "beta-reader";

    private Long readerId;
    private GeoPoint point;
    private String email;
    private String username;
}
