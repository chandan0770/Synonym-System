package com.synonyms.assignment.models.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    @Getter
    @Setter
    private Object data;
}
