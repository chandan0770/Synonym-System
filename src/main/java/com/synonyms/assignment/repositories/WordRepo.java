package com.synonyms.assignment.repositories;

import com.synonyms.assignment.models.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepo extends JpaRepository<Word, Long> {
    Word findByName(String name);


    @Query(value = "select name from word w inner join word_synonyms ws on (w.id = ws.word_id) where synonyms_id = (select id from word where name = :name)", nativeQuery = true)
    List<SynonymDto> getSynonyms(String name);


    interface SynonymDto{
        String getName();
    }
}
