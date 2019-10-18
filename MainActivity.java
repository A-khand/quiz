package com.example.myquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;

    public static final String SHARED_PREFS = "sharedprefs";
    public static final  String KEY_HIGHSCORE = "keyhighscore";

    private TextView textviewHighscore;
    private  int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonstartquiz = findViewById(R.id.button_start_quiz);


        textviewHighscore = findViewById(R.id.text_view_highscore);
        loadHighscore();

        buttonstartquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });
    }
    private void startQuiz(){
        Intent intent = new Intent(MainActivity.this,QuizActivity.class);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
                if(score>highscore){
                    updateHighscore(score);
                }
            }
        }
    }
        private void loadHighscore(){
            SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
            highscore=prefs.getInt(KEY_HIGHSCORE, 0 );
            textviewHighscore.setText("Highscore: " + highscore);
        }
        private void updateHighscore(int highscorenew ){
        highscore = highscorenew;
        textviewHighscore.setText("Highscore: " + highscore);
            SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_HIGHSCORE,highscore);
            editor.apply();
        }
}
