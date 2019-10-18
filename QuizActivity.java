package com.example.myquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extrascore";
    private  static  final long COUNTDOWN_IN_MILLIS=30000;
    private  static final String KEY_SCORE = "keyscore";
    private static final String KEY_QUESTION_COUNT = "keyQuestioncount";
    private static  final  String KEY_MILLIS_LEFT = "keymillisLeft";
    private static final String KEY_ANSWERD = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewscore;
    private TextView textViewquestion;
    private TextView textViewQuestioncount;
    private  TextView textViewcountdown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonconfirmNext;

    private ColorStateList textcolorDefaultRb;


    private ColorStateList getTextcolorDefaultcd;
    private CountDownTimer countDownTimer;
    private long timeleftInMillis;


    private ArrayList<question> QuestionList;
    private  int questionCounter;
    private  int QuestionCounterTotal;
    private question currentQuestion;

    private int Score;
    private boolean answerd;
    private long backpressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        textViewquestion = findViewById(R.id.text_view_question);
        textViewscore = findViewById(R.id.text_view_score);
        textViewQuestioncount = findViewById(R.id.text_view_question_count);
        textViewcountdown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonconfirmNext = findViewById(R.id.button_confirm_next);

        textcolorDefaultRb = rb1.getTextColors();

        getTextcolorDefaultcd = rb1.getTextColors();
        getTextcolorDefaultcd = textViewcountdown.getLinkTextColors();

        if (savedInstanceState == null) {

            quizdbhelper dbhelpar = new quizdbhelper(this);
            QuestionList = dbhelpar.getquestions("Medium");
            QuestionCounterTotal = QuestionList.size();
            Collections.shuffle(QuestionList);

            showNextQuestion();
        }else
        {
            QuestionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            if (QuestionList==null){
                finish();
            }
            QuestionCounterTotal = QuestionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = QuestionList.get(questionCounter -1);
            Score = savedInstanceState.getInt(KEY_SCORE);
            timeleftInMillis=savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answerd=savedInstanceState.getBoolean(KEY_ANSWERD);
            if (!answerd){
                StartCountDown();
            }else {
                updateCountDownText();
                showsolution();
            }
        }
        buttonconfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answerd) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        cheskAnswer();

                    } else {
                        Toast.makeText(QuizActivity.this,"please select answare",Toast.LENGTH_SHORT).show();
                    }
                }else {
                        showNextQuestion();
                    }
                }

        });

    }
















        private  void  showNextQuestion(){
        rb1.setTextColor(textcolorDefaultRb);
            rb2.setTextColor(textcolorDefaultRb);
            rb3.setTextColor(textcolorDefaultRb);
            rbGroup.clearCheck();

            if (questionCounter<QuestionCounterTotal){
                currentQuestion = QuestionList.get(questionCounter);
                textViewquestion.setText(currentQuestion.getQuestion());
                rb1.setText(currentQuestion.getOption1());
                rb2.setText(currentQuestion.getOption2());
                rb3.setText(currentQuestion.getOption3());
                questionCounter++;
                textViewQuestioncount.setText("Question: " + questionCounter + "/" + QuestionCounterTotal);
                answerd=false;
                buttonconfirmNext.setText("confirm");

                timeleftInMillis = COUNTDOWN_IN_MILLIS;
                StartCountDown();

            }
            else
            {
                finishQuiz();
            }

        }
            private void StartCountDown(){
        countDownTimer = new CountDownTimer(timeleftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeleftInMillis = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timeleftInMillis = 0;
                updateCountDownText();
                cheskAnswer();

            }
        }.start();
            }
            private void updateCountDownText(){
        int minutes = (int) (timeleftInMillis / 1000) / 60;
        int seconds  = (int) (timeleftInMillis/1000) % 60;

        String timeFomatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textViewcountdown.setText(timeFomatted);
        if (timeleftInMillis<10000){

            textViewcountdown.setTextColor(Color.RED);
        }else
        {
            textViewcountdown.setTextColor(getTextcolorDefaultcd);
        }
            }


        private  void cheskAnswer(){
        answerd=true;
        countDownTimer.cancel();
        RadioButton rbselected=findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbselected) +1;

        if (answerNr==currentQuestion.getAnswerNr()) {
            Score++;
            textViewscore.setText("score" + Score);
        }
            showsolution();
        }
           private void showsolution(){
            rb1.setTextColor(Color.RED);
               rb2.setTextColor(Color.RED);
               rb3.setTextColor(Color.RED);
               switch (currentQuestion.getAnswerNr()){
                   case 1:
                           rb1.setTextColor(Color.GREEN);
                    textViewquestion.setText("Answer1 is correct");
                    break;
                   case 2:
                       rb2.setTextColor(Color.GREEN);
                       textViewquestion.setText("Answer2 is correct");
                       break;
                   case 3:
                       rb3.setTextColor(Color.GREEN);
                       textViewquestion.setText("Answer3 is correct");
                       break;

               }
               if (questionCounter<QuestionCounterTotal){
                   buttonconfirmNext.setText("Next");
               }else{
                   buttonconfirmNext.setText("finish");
               }
        }
        private void finishQuiz(){
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SCORE,Score);
            setResult(RESULT_OK,resultIntent);
        finish();
        }

    @Override
    public void onBackPressed() {
        if (backpressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        }else{
            Toast.makeText(this, "press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backpressedTime = System.currentTimeMillis();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE,Score);
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putLong(KEY_MILLIS_LEFT,timeleftInMillis);
        outState.putBoolean(KEY_ANSWERD,answerd);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,QuestionList);
    }
}

