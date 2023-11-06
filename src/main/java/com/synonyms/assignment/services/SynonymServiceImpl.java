package com.synonyms.assignment.services;

import com.synonyms.assignment.core.exception.handler.AlreadyExistingException;
import com.synonyms.assignment.models.domain.Word;
import com.synonyms.assignment.repositories.WordRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SynonymServiceImpl implements SynonymService{

    private final WordRepo repo;

    @Transactional
    public void addWord(String wordName) {
        try {
            Word existingWord = repo.findByName(wordName);
            if (existingWord != null) {
                throw new AlreadyExistingException("Word already exists: " + wordName);
            }
            Word word = new Word();
            word.setName(wordName);
            repo.save(word);
        } catch (AlreadyExistingException exception) {
            log.info(exception.getError());
            throw exception;
        }
    }

    @Transactional
    public void updateWord(String wordName, String newWordName) {
        try {
            Word word = repo.findByName(wordName);
            if (word != null) {
                word.setName(newWordName);
                repo.save(word);
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw exception;
        }
    }

    @Transactional
    public void deleteWord(String wordName) {
        try {
            Word word = repo.findByName(wordName);
            if (word != null) {
                Set<Word> synonyms = new HashSet<>(word.getSynonyms());
                for (Word synonym : synonyms) {
                    word.getSynonyms().remove(synonym);
                    synonym.getSynonyms().remove(word);
                    repo.save(synonym);
                }
                word.getSynonyms().clear();
                repo.delete(word);
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw exception;
        }

    }

    @Transactional
    public void addSynonyms(String wordName, List<String> synonymNames) {
        try {
            Word word = repo.findByName(wordName);
            if (word == null) {
                word = new Word();
                word.setName(wordName);
                repo.save(word);
            }
            for (String synonymName : synonymNames) {
                Word synonym = repo.findByName(synonymName);
                if (synonym == null) {
                    synonym = new Word();
                    synonym.setName(synonymName);
                    repo.save(synonym);
                }

                word.getSynonyms().add(synonym);
                synonym.getSynonyms().add(word);
                repo.save(word);
                repo.save(synonym);

                propagateSynonyms(word, synonym);
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw exception;
        }
    }

    @Transactional
    public void updateSynonymName(String wordName, String oldSynonymName, String newSynonymName) {
        try {
            Word word = repo.findByName(wordName);
            if (word == null) {
                throw new AlreadyExistingException("Word does not exists: " + wordName);
            }
            for (Word synonym : word.getSynonyms()) {
                if (synonym.getName().equals(oldSynonymName)) {
                    synonym.setName(newSynonymName);
                    repo.save(synonym);
                }
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw exception;
        }
    }

    public List<String> getSynonyms(String wordName) {
        try {
            return repo.getSynonyms(wordName).stream().map(WordRepo.SynonymDto::getName).toList();
        }
        catch(Exception exception){
            log.info(exception.getMessage());
            throw exception;
        }
    }

    private void propagateSynonyms(Word source, Word newSynonym) {
        try {
            Set<Word> processedWords = new HashSet<>();
            Queue<Word> queue = new LinkedList<>();
            queue.add(source);
            processedWords.add(source);

            while (!queue.isEmpty()) {
                Word currentWord = queue.poll();
                Set<Word> synonyms = currentWord.getSynonyms().stream().filter(s -> s.getName() != null).collect(Collectors.toSet());

                for (Word synonym : synonyms) {
                    if (!synonym.equals(newSynonym) && !processedWords.contains(synonym)) {
                        synonym.getSynonyms().add(newSynonym);
                        newSynonym.getSynonyms().add(synonym);
                        repo.save(synonym);
                        queue.add(synonym);
                        processedWords.add(synonym);
                    }
                }
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw exception;
        }
    }
}
