package com.project.jennys.flashcard;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Source Code from CodePath.com for the Mobile App Design Course, Android Track
 */


@Dao
public interface FlashcardDao {
    @Query("SELECT * FROM flashcard")
    List<com.project.jennys.flashcard.Flashcard> getAll();

    @Insert
    void insertAll(com.project.jennys.flashcard.Flashcard... flashcards);

    @Delete
    void delete(com.project.jennys.flashcard.Flashcard flashcard);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(com.project.jennys.flashcard.Flashcard flashcard);
}
