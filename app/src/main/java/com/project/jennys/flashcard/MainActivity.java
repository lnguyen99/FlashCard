package com.project.jennys.flashcard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView flash_card_image_view;
    boolean is_showing_choices = false;
    boolean is_showing_question = true;
    int currentCardDisplayedIndex = 0;

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;

    final static int ADD_CARD_REQUEST_CODE = 100;
    final static int EDIT_CARD_REQUEST_CODE = 150;
    final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();


        // Display saved flashcard in the database
        if (allFlashcards != null && allFlashcards.size() > 0) {
            displayCard(allFlashcards.get(currentCardDisplayedIndex));
        }


        // Move to the next card in the database
        findViewById(R.id.next_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newNum = getRandomNumber(allFlashcards.size());
                while (newNum == currentCardDisplayedIndex)
                    newNum = getRandomNumber(allFlashcards.size());

                currentCardDisplayedIndex = newNum;
                displayCard(allFlashcards.get(currentCardDisplayedIndex));
            }
        });

        // Delete the current card from the Database
        findViewById(R.id.delete_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();

                if (allFlashcards != null && allFlashcards.size() > 0) {
                    currentCardDisplayedIndex = allFlashcards.size() - 1;
                    displayCard(allFlashcards.get(currentCardDisplayedIndex));
                }

            }
        });


        // Flip between question and answer
        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView flash_card_view = findViewById(R.id.flashcard_question);
                if (is_showing_question) {
                    flash_card_view.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                    flash_card_view.setBackgroundColor(Color.WHITE);
                    flash_card_view.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    is_showing_question = false;
                } else {
                    flash_card_view.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    flash_card_view.setTextColor(Color.WHITE);
                    flash_card_view.setBackground(getResources().getDrawable(R.drawable.card_background));
                    is_showing_question = true;
                }
            }
        });


        // Choose the correct answer, green the choice
        findViewById(R.id.correct_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.correct_answer).setBackgroundColor(
                        getResources().getColor(R.color.green)
                );
            }
        });


        // Choose wrong answers, red the wrong, green the right
        findViewById(R.id.wrong_answer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.wrong_answer1).setBackgroundColor(
                        getResources().getColor(R.color.red)
                );
                findViewById(R.id.correct_answer).setBackgroundColor(
                        getResources().getColor(R.color.green)
                );
            }
        });

        findViewById(R.id.wrong_answer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.wrong_answer2).setBackgroundColor(
                        getResources().getColor(R.color.red)
                );

                findViewById(R.id.correct_answer).setBackgroundColor(
                        getResources().getColor(R.color.green)
                );
            }
        });


        // Toggle between learning and quizzing (showing or hiding multiple choice answers)
        findViewById(R.id.toogle_choices_visibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flash_card_image_view = findViewById(R.id.toogle_choices_visibility);

                if (is_showing_choices) { // Show multiple choices
                    flash_card_image_view.setImageResource(R.drawable.show_icon);
                    ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    findViewById(R.id.correct_answer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.wrong_answer1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.wrong_answer2).setVisibility(View.INVISIBLE);
                    is_showing_choices = false;

                } else { // Hide multiple choices
                    flash_card_image_view.setImageResource(R.drawable.hide_icon);
                    findViewById(R.id.correct_answer).setVisibility(View.VISIBLE);
                    findViewById(R.id.wrong_answer1).setVisibility(View.VISIBLE);
                    findViewById(R.id.wrong_answer2).setVisibility(View.VISIBLE);
                    is_showing_choices = true;
                }

            }
        });


        // Add new Card
        findViewById(R.id.add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateCardActivity.class);
                MainActivity.this.startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
            }
        });

        //Update Current Card
        findViewById(R.id.edit_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateCardActivity.class);

                Flashcard currentCard = allFlashcards.get(currentCardDisplayedIndex);
                intent.putExtra("question", currentCard.getQuestion());
                intent.putExtra("correct_answer", currentCard.getAnswer());
                intent.putExtra("wrong_answer1", currentCard.getWrongAnswer1());
                intent.putExtra("wrong_answer2", currentCard.getWrongAnswer2());

                MainActivity.this.startActivityForResult(intent, EDIT_CARD_REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String question, correct_answer, wrong_answer1, wrong_answer2, text = "";

        if (((requestCode == EDIT_CARD_REQUEST_CODE) || (requestCode == ADD_CARD_REQUEST_CODE))
                && resultCode == RESULT_OK)
        {
            question = data.getExtras().getString("question");
            correct_answer = data.getExtras().getString("correct_answer");
            wrong_answer1 = data.getExtras().getString("wrong_answer1");
            wrong_answer2 = data.getExtras().getString("wrong_answer2");

            if (requestCode == ADD_CARD_REQUEST_CODE) {
                flashcardDatabase.insertCard(new Flashcard(question, correct_answer, wrong_answer1, wrong_answer2));
                currentCardDisplayedIndex = allFlashcards.size();

            } else {
                Flashcard cardToEdit = allFlashcards.get(currentCardDisplayedIndex);

                cardToEdit.setQuestion(question);
                cardToEdit.setAnswer(correct_answer);
                cardToEdit.setWrongAnswer1(wrong_answer1);
                cardToEdit.setWrongAnswer2(wrong_answer2);

                flashcardDatabase.updateCard(cardToEdit);
            }

            allFlashcards = flashcardDatabase.getAllCards();
            displayCard(allFlashcards.get(currentCardDisplayedIndex));
            Snackbar.make(findViewById(R.id.flashcard_question),
                    "Card successfully saved",
                    Snackbar.LENGTH_SHORT)
                    .show();

        }

    }

    protected void displayCard(Flashcard flashcard) {
        ((TextView) findViewById(R.id.flashcard_question)).setText(flashcard.getQuestion());
        ((TextView) findViewById(R.id.correct_answer)).setText(flashcard.getAnswer());
        ((TextView) findViewById(R.id.wrong_answer1)).setText(flashcard.getWrongAnswer1());
        ((TextView) findViewById(R.id.wrong_answer2)).setText(flashcard.getWrongAnswer2());
    }

    protected int getRandomNumber(int maxNumber) {
        return rand.nextInt(maxNumber);
    }

}
