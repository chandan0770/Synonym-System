package com.synonyms.assignment.controller;

import com.synonyms.assignment.models.api.ApiResponse;
import com.synonyms.assignment.models.api.SynonymDto;
import com.synonyms.assignment.services.SynonymServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(SynonymController.BASE_URL)
@RequiredArgsConstructor
public class SynonymController {

    public static final String BASE_URL = "/api/v1";

    private final SynonymServiceImpl synonymService;

    @PostMapping("/addWord")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WRITER')")
    public ResponseEntity<ApiResponse> addWord(@RequestBody @Valid SynonymDto synonymDto) {
        synonymService.addWord(synonymDto.getWord());
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/updateWord")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WRITER')")
    public ResponseEntity<ApiResponse> updateWord(@RequestBody @Valid SynonymDto synonymDto) {
        synonymService.updateWord(synonymDto.getWord(), synonymDto.getNewWord());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/deleteWord")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WRITER')")
    public ResponseEntity<ApiResponse> deleteWord(@RequestBody @Valid SynonymDto synonymDto) {
        synonymService.deleteWord(synonymDto.getWord());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/addSynonyms")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WRITER')")
    public ResponseEntity<ApiResponse> addSynonyms(@RequestBody @Valid SynonymDto synonymDto) {
        synonymService.addSynonyms(synonymDto.getWord(), synonymDto.getSynonyms());
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/updateSynonyms")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WRITER')")
    public ResponseEntity<ApiResponse> updateSynonyms(@RequestBody @Valid SynonymDto synonymDto) {
        synonymService.updateSynonymName(synonymDto.getWord(), synonymDto.getOldSynonymName(),
                synonymDto.getNewSynonymName());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'READER', 'WRITER')")
    @GetMapping("/getSynonyms")
    public ResponseEntity<List<String>> getSynonyms(@RequestParam("word") String word) {
        List<String> synonyms = synonymService.getSynonyms(word);
        return new ResponseEntity<>(synonyms, HttpStatus.OK);
    }
}