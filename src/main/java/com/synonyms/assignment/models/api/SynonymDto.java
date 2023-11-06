package com.synonyms.assignment.models.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder@AllArgsConstructor
@NoArgsConstructor
public class SynonymDto {
    private String word;
    private String newWord;
    private List<String> synonyms;
    private String oldSynonymName;
    private String newSynonymName;
}
