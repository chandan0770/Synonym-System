package com.synonyms.assignment.controller;

import com.synonyms.assignment.core.exception.handler.AlreadyExistingException;
import com.synonyms.assignment.models.api.ApiResponse;
import com.synonyms.assignment.models.api.SynonymDto;
import com.synonyms.assignment.models.domain.Word;
import com.synonyms.assignment.repositories.WordRepo;
import com.synonyms.assignment.services.SynonymServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SynonymControllerTest {
    @Mock
    private SynonymServiceImpl synonymService;

    @Mock
    private WordRepo repo;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SynonymController synonymController = new SynonymController(synonymService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(synonymController).build();
    }

    // The 'addWord' endpoint should add a new word to the repository and return a 201 HTTP status code.
    @Test
    public void test_addWord_shouldAddNewWordAndReturn201StatusCode() {
        // Arrange
        SynonymController synonymController = new SynonymController(synonymService);
        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("testWord");

        // Act
        ResponseEntity<ApiResponse> response = synonymController.addWord(synonymDto);

        // Assert
        verify(synonymService, times(1)).addWord("testWord");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_addWord_shouldThrowAlreadyExistingException() {
        // Arrange
        SynonymController controller = new SynonymController(synonymService);

        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("testWord");

        Word word = new Word();
        word.setName("testWord");
        doReturn(word)
                .when(repo)
                .findByName(synonymDto.getWord());

        // Mock the behavior of the service
        doThrow(
                AlreadyExistingException.class)
                .when(synonymService)
                .addWord(synonymDto.getWord());

        // Act and Assert
        assertThrows(AlreadyExistingException.class, () -> {
            controller.addWord(synonymDto);
        });
    }

    // The 'updateWord' endpoint should update an existing word in the repository and return a 200 HTTP status code.
    @Test
    public void test_updateWord_shouldUpdateExistingWordAndReturn200StatusCode() {
        // Arrange
        SynonymController synonymController = new SynonymController(synonymService);
        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("testWord");
        synonymDto.setNewWord("newTestWord");

        // Act
        ResponseEntity<ApiResponse> response = synonymController.updateWord(synonymDto);

        // Assert
        verify(synonymService, times(1)).updateWord("testWord", "newTestWord");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // The 'deleteWord' endpoint should delete an existing word from the repository and return a 200 HTTP status code.
    @Test
    public void test_deleteWord_shouldDeleteExistingWordAndReturn200StatusCode() {
        // Arrange
        SynonymController synonymController = new SynonymController(synonymService);
        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("testWord");

        // Act
        ResponseEntity<ApiResponse> response = synonymController.deleteWord(synonymDto);

        // Assert
        verify(synonymService, times(1)).deleteWord("testWord");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_addSynonyms_existingWord_validInput() {
        SynonymController synonymController = new SynonymController(synonymService);
        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("existingWord");
        synonymDto.setSynonyms(Arrays.asList("synonym1", "synonym2"));

        ResponseEntity<ApiResponse> response = synonymController.addSynonyms(synonymDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_addSynonyms_newWord_validInput() {
        SynonymController synonymController = new SynonymController(synonymService);
        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("newWord");
        synonymDto.setSynonyms(Arrays.asList("synonym1", "synonym2"));

        ResponseEntity<ApiResponse> response = synonymController.addSynonyms(synonymDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_addSynonyms_existingWord_emptySynonymsList() {
        SynonymController synonymController = new SynonymController(synonymService);
        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("existingWord");
        synonymDto.setSynonyms(Collections.emptyList());

        ResponseEntity<ApiResponse> response = synonymController.addSynonyms(synonymDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_addSynonyms_synonymDoesNotExist() {
        SynonymController synonymController = new SynonymController(synonymService);
        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("existingWord");
        synonymDto.setSynonyms(Arrays.asList("synonym1", "nonExistingSynonym"));

        ResponseEntity<ApiResponse> response = synonymController.addSynonyms(synonymDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_updateSynonyms_success() {
        SynonymController synonymController = new SynonymController(synonymService);
        SynonymDto synonymDto = new SynonymDto();
        synonymDto.setWord("word");
        synonymDto.setOldSynonymName("oldSynonym");
        synonymDto.setNewSynonymName("newSynonym");

        ResponseEntity<ApiResponse> response = synonymController.updateSynonyms(synonymDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_returns_synonyms_for_given_word() {
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