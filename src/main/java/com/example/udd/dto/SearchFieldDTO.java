package com.example.udd.dto;

import com.example.udd.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFieldDTO {
    private String field;

    private String value;

    private boolean regular;

    private Operator operator;
}
