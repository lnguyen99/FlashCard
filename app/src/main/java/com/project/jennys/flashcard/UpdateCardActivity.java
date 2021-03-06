package com.project.jennys.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ((EditText) findViewById(R.id.new_question)).setText(getIntent().getStringExtra("question"));
        ((EditText) findViewById(R.id.new_correct_answer)).setText(getIntent().getStringExtra("correct_answer"));
        ((EditText) findViewById(R.id.new_wrong_answer1)).setText(getIntent().getStringExtra("wrong_answer1"));
        ((EditText) findViewById(R.id.new_wrong_answer2)).setText(getIntent().getStringExtra("wrong_answer2"));


        //Dismiss the UpdateCardActivity
        //Go back to the MainActivity (Homepage)
        findViewById(R.id.cancel_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Save card
        findViewById(R.id.save_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String question = ((EditText) findViewById(R.id.new_question)).getText().toString();
                String answer = ((EditText) findViewById(R.id.new_correct_answer)).getText().toString();

                if (question.length() <= 0 && answer.length() <= 0) {
                    displayToast("Must Enter Both Question and Correct Answer to Create Card");
                } else if (question.length() <= 0) {
                    displayToast("Must Enter Question to Create Card");
                } else if (answer.length() <= 0) {
                    displayToast("Must Enter Correct Answer to Create Card");
                } else {
                    Intent data = new Intent();

                    data.putExtra("question", question);
                    data.putExtra("correct_answer", answer);
                    data.putExtra("wrong_answer1", ((EditText) findViewById(R.id.new_wrong_answer1)).getText().toString());
                    data.putExtra("wrong_answer2", ((EditText) findViewById(R.id.new_wrong_answer2)).getText().toString());

                    setResult(RESULT_OK, data);
                    finish();
                }

            }
        });

    }

    private void displayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


}
