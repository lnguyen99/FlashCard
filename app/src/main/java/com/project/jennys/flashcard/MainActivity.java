package com.project.jennys.flashcard;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final static int ADD_CARD_REQUEST_CODE = 100;
    final static int EDIT_CARD_REQUEST_CODE = 150;
    final Random rand = new Random();

    boolean isShowingChoices = false;
    CountDownTimer countDownTimer;

    int currentCardDisplayedIndex = 0;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();
        displayCard();

        countDownTimer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.timer)).setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
            }
        };


        // Move to the next card in the database
        findViewById(R.id.next_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                if (allFlashcards != null && allFlashcards.size() > 1) {
                    getRandomCardIndex();

                    leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });

                    findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);
                    if (isShowingChoices) startTimer();
                }

                displayCard();
            }
        });


        // Delete the current card from the Database
        findViewById(R.id.delete_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();
                currentCardDisplayedIndex = allFlashcards.size() >= 0 ? allFlashcards.size() - 1 : 0;
                displayCard();
            }
        });


        // Flip between question and answer
        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFlashcards != null && allFlashcards.size() > 0) {
                    showAnswer();
                }
            }
        });
        findViewById(R.id.flashcard_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestion();
            }
        });


        // Choose the correct answer, green the choice
        findViewById(R.id.correct_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.correct_answer).setBackgroundColor(
                        getResources().getColor(R.color.green)
                );
                new ParticleSystem(MainActivity.this, 100, R.drawable.confetti, 3000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(findViewById(R.id.correct_answer), 100);
                countDownTimer.cancel();
            }
        });


        // Choose wrong answers, red the wrong, green the right
        findViewById(R.id.wrong_answer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.wrong_answer1).setBackgroundColor(
                        getResources().getColor(R.color.red)
                );
            }
        });

        findViewById(R.id.wrong_answer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.wrong_answer2).setBackgroundColor(
                        getResources().getColor(R.color.red)
                );
            }
        });


        // Toggle between learning and quizzing (showing or hiding multiple choice answers)
        findViewById(R.id.toogle_choices_visibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allFlashcards != null && allFlashcards.size() > 0) {

                    if (isShowingChoices) { // Hide multiple choices
                        hideChoices();
                        ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());

                    } else { // Show multiple choices
                        showChoices();
                    }

                } else {
                    displayEmptyState();
                }

            }
        });


        // Add new Card
        findViewById(R.id.add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateCardActivity.class);
                MainActivity.this.startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String question, correct_answer, wrong_answer1, wrong_answer2, text = "";

        if (((requestCode == EDIT_CARD_REQUEST_CODE) || (requestCode == ADD_CARD_REQUEST_CODE))
                && resultCode == RESULT_OK) {

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
            displayCard();
            Snackbar.make(findViewById(R.id.flashcard_question),
                    "Card successfully saved",
                    Snackbar.LENGTH_SHORT)
                    .show();

            startTimer();
        }

    }

    public void displayCard() {
        if (allFlashcards != null && allFlashcards.size() > 0) {
            displayCardInfo(allFlashcards.get(currentCardDisplayedIndex));
        } else {
            displayEmptyState();
        }
    }

    protected void displayCardInfo(Flashcard flashcard) {
        ((TextView) findViewById(R.id.flashcard_question)).setText(flashcard.getQuestion());
        ((TextView) findViewById(R.id.flashcard_answer)).setText(flashcard.getAnswer());
        ((TextView) findViewById(R.id.correct_answer)).setText(flashcard.getAnswer());
        ((TextView) findViewById(R.id.wrong_answer1)).setText(flashcard.getWrongAnswer1());
        ((TextView) findViewById(R.id.wrong_answer2)).setText(flashcard.getWrongAnswer2());

        showQuestion();
        resetChoiceColor();
        findViewById(R.id.edit_card).setVisibility(View.VISIBLE);
    }

    protected void displayEmptyState() {
        ((TextView) findViewById(R.id.flashcard_question)).setText("Hello There");
        showQuestion();
        hideChoices();
        findViewById(R.id.edit_card).setVisibility(View.INVISIBLE);
    }

    protected void showQuestion() {
        findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.flashcard_question)).setTextColor(Color.WHITE);
        findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
    }

    protected void showAnswer() {
        TextView answerSideView = findViewById(R.id.flashcard_answer);

        // get the center for the clipping circle
        int cx = answerSideView.getWidth() / 2;
        int cy = answerSideView.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);

        // hide the question and show the answer to prepare for playing the animation!
        findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
        answerSideView.setVisibility(View.VISIBLE);
        answerSideView.setTextColor(getResources().getColor(R.color.colorPrimary));

        anim.setDuration(450);
        anim.start();
    }

    protected void hideChoices() {
        ((ImageView) findViewById(R.id.toogle_choices_visibility)).setImageResource(R.drawable.show_icon);
        findViewById(R.id.correct_answer).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrong_answer1).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrong_answer2).setVisibility(View.INVISIBLE);
        findViewById(R.id.timer).setVisibility(View.INVISIBLE);
        isShowingChoices = false;
        resetChoiceColor();
    }

    protected void showChoices() {
        ((ImageView) findViewById(R.id.toogle_choices_visibility)).setImageResource(R.drawable.hide_icon);
        findViewById(R.id.correct_answer).setVisibility(View.VISIBLE);
        findViewById(R.id.wrong_answer1).setVisibility(View.VISIBLE);
        findViewById(R.id.wrong_answer2).setVisibility(View.VISIBLE);
        findViewById(R.id.timer).setVisibility(View.VISIBLE);
        isShowingChoices = true;
        startTimer();
    }

    protected void resetChoiceColor() {
        findViewById(R.id.correct_answer).setBackgroundColor(getResources().getColor(R.color.orange));
        findViewById(R.id.wrong_answer1).setBackgroundColor(getResources().getColor(R.color.orange));
        findViewById(R.id.wrong_answer2).setBackgroundColor(getResources().getColor(R.color.orange));
    }

    protected void getRandomCardIndex() {
        int rando = currentCardDisplayedIndex;
        while (rando == currentCardDisplayedIndex)
            rando = rand.nextInt(allFlashcards.size());
        currentCardDisplayedIndex = rando;
    }

    private void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }

}
