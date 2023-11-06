package com.synonyms.assignment.service;

import com.synonyms.assignment.controller.SynonymController;
import com.synonyms.assignment.core.exception.handler.AlreadyExistingException;
import com.synonyms.assignment.models.domain.Word;
import com.synonyms.assignment.repositories.WordRepo;
import com.synonyms.assignment.services.SynonymServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SynonymServiceImplTest {

    @Mock private WordRepo repo;

    private SynonymServiceImpl synonymService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SynonymServiceImpl synonymService = new SynonymServiceImpl(repo);
    }

    @Test
    public void test_addWord_shouldThrowAlreadyExistingException() {
        // Arrange
        SynonymServiceImpl synonymService = new SynonymServiceImpl(repo);
        String wordName = "existingWord";
        Word existingWord = new Word();
        existingWord.setName(wordName);
        doReturn(existingWord)
                .when(repo)
                .findByName(wordName);

        // Act and Assert
        assertThrows(AlreadyExistingException.class, () -> {
            synonymService.addWord(wordName);
        });
    }

    // Updating a word that does not exist should do nothing.
    @Test
    public void test_updateWord_shouldDoNothingWhenWordDoesNotExist() {
        // Arrange
        String wordName = "testWord";
        String newWordName = "updatedTestWord";
        SynonymServiceImpl service = new SynonymServiceImpl(repo);

        // Act
        service.updateWord(wordName, newWordName);

        // Assert
        assertNull(repo.findByName(wordName));
        assertNull(repo.findByName(newWordName));
    }

    // Deleting an existing word should remove the Word object with the given name from the repository, along with all its synonyms.
    @Test
    public void test_deleteWord_shouldRemoveExistingWordAndItsSynonymsFromRepository() {
        // Arrange
        String wordName = "testWord";
        String synonymName = "testSynonym";
        SynonymServiceImpl service = new SynonymServiceImpl(repo);
        Word word = new Word();
        word.setName(wordName);
        Word synonym = new Word();
        synonym.setName(synonymName);
        word.getSynonyms().add(synonym);
        synonym.getSynonyms().add(word);
        repo.save(word);
        repo.save(synonym);

        // Act
        service.deleteWord(wordName);
        Word deletedWord = repo.findByName(wordName);
        Word deletedSynonym = repo.findByName(synonymName);

        // Assert
        assertNull(deletedWord);
        assertNull(deletedSynonym);
    }

    // Deleting a word that does not exist should do nothing.
    @Test
    public void test_deleteWord_shouldDoNothingWhenWordDoesNotExist() {
        // Arrange
        String wordName = "testWord";
        SynonymServiceImpl service = new SynonymServiceImpl(repo);

        // Act
        service.deleteWord(wordName);

        // Assert
        assertNull(repo.findByName(wordName));
    }

    @Test
    public void test_return_synonyms_for_given_word() {
        // Arrange
        SynonymServiceImpl synonymService = mock(SynonymServiceImpl.class);
        List<String> expectedSynonyms = Arrays.asList("synonym1", "synonym2");
        when(synonymService.getSynonyms("word")).thenReturn(expectedSynonyms);
        SynonymController synonymController = new SynonymController(synonymService);

        // Act
        ResponseEntity<List<String>> response = synonymController.getSynonyms("word");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSynonyms, response.getBody());
    }
}