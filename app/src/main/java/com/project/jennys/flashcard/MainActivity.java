package com.project.jennys.flashcard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView flash_card_image_view;
    boolean isShowingAnswer = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.correct_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.correct_answer).setBackgroundColor(
                        getResources().getColor(R.color.green)
                );
            }
        });

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


        findViewById(R.id.toogle_choices_visibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flash_card_image_view = findViewById(R.id.toogle_choices_visibility);
                if (isShowingAnswer) {

                    flash_card_image_view.setImageResource(R.drawable.hide_icon);
                    findViewById(R.id.correct_answer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.wrong_answer1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.wrong_answer2).setVisibility(View.INVISIBLE);

                    isShowingAnswer = false;

                } else {
                    flash_card_image_view.setImageResource(R.drawable.show_icon);
                    findViewById(R.id.correct_answer).setVisibility(View.VISIBLE);
                    findViewById(R.id.wrong_answer1).setVisibility(View.VISIBLE);
                    findViewById(R.id.wrong_answer2).setVisibility(View.VISIBLE);

                    isShowingAnswer = true;
                }

            }
        });
    }
}
