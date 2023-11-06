package com.synonyms.assignment.services;

import java.util.List;

public interface SynonymService {
    void addWord(String wordName);
    void updateWord(String wordName, String newWordName);
    void deleteWord(String wordName);
    void addSynonyms(String wordName, List<String> synonymName);
    void updateSynonymName(String wordName, String oldSynonymName, String newSynonymName);
    List<String> getSynonyms(String wordName);

}
