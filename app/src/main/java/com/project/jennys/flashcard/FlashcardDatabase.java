package com.project.jennys.flashcard;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

/**
 * Source Code from CodePath.com for the Mobile App Design Course, Android Track
 */


public class FlashcardDatabase {
    private final com.project.jennys.flashcard.AppDatabase db;

    FlashcardDatabase(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(),
                com.project.jennys.flashcard.AppDatabase.class, "flashcard-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public List<com.project.jennys.flashcard.Flashcard> getAllCards() {
        return db.flashcardDao().getAll();
    }

    public void insertCard(com.project.jennys.flashcard.Flashcard flashcard) {
        db.flashcardDao().insertAll(flashcard);
    }

    public void deleteCard(String flashcardQuestion) {
        List<com.project.jennys.flashcard.Flashcard> allCards = db.flashcardDao().getAll();
        for (com.project.jennys.flashcard.Flashcard f : allCards) {
            if (f.getQuestion().equals(flashcardQuestion)) {
                db.flashcardDao().delete(f);
            }
        }
    }

    public void updateCard(com.project.jennys.flashcard.Flashcard flashcard) {
        db.flashcardDao().update(flashcard);
    }
}
