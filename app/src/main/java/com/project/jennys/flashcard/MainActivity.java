package com.project.jennys.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView flash_card_image_view;

    boolean is_showing_choices = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Flip between question and correct answer
        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcard_answer).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.flashcard_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
            }
        });

        // Choose the correct answer
        findViewById(R.id.correct_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.correct_answer).setBackgroundColor(
                        getResources().getColor(R.color.green)
                );
            }
        });

        // Choose wrong answers
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

                if (is_showing_choices) {
                    flash_card_image_view.setImageResource(R.drawable.hide_icon);
                    findViewById(R.id.correct_answer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.wrong_answer1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.wrong_answer2).setVisibility(View.INVISIBLE);
                    is_showing_choices = false;
                } else {
                    flash_card_image_view.setImageResource(R.drawable.show_icon);
                    findViewById(R.id.correct_answer).setVisibility(View.VISIBLE);
                    findViewById(R.id.wrong_answer1).setVisibility(View.VISIBLE);
                    findViewById(R.id.wrong_answer2).setVisibility(View.VISIBLE);
                    is_showing_choices = true;
                }

            }
        });

        // Move to AddCardActivity
        findViewById(R.id.add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 100);
            }
        });

        // Move to EditCardActivity
        findViewById(R.id.edit_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditCardActivity.class);

                intent.putExtra("question", ((TextView) findViewById(R.id.flashcard_question)).getText());
                intent.putExtra("correct_answer", ((TextView) findViewById(R.id.flashcard_answer)).getText());
                intent.putExtra("wrong_answer1", ((TextView) findViewById(R.id.wrong_answer1)).getText());
                intent.putExtra("wrong_answer2", ((TextView) findViewById(R.id.wrong_answer2)).getText());

                MainActivity.this.startActivityForResult(intent, 150);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String question, correct_answer, wrong_answer1, wrong_answer2, text = "";

        if (((requestCode == 100) || (requestCode == 150)) && resultCode == RESULT_OK) {
            question = data.getExtras().getString("question");
            correct_answer = data.getExtras().getString("correct_answer");
            wrong_answer1 = data.getExtras().getString("wrong_answer1");
            wrong_answer2 = data.getExtras().getString("wrong_answer2");

            ((TextView) findViewById(R.id.flashcard_question)).setText(question);
            ((TextView) findViewById(R.id.flashcard_answer)).setText(correct_answer);
            ((TextView) findViewById(R.id.correct_answer)).setText(correct_answer);
            ((TextView) findViewById(R.id.wrong_answer1)).setText(wrong_answer1);
            ((TextView) findViewById(R.id.wrong_answer2)).setText(wrong_answer2);

            if (requestCode == 100) text = "Card successfully created";
            else text = "Card successfully updated";

            Snackbar.make(findViewById(R.id.flashcard_question),
                    text,
                    Snackbar.LENGTH_SHORT)
                    .show();

        }

    }

}
