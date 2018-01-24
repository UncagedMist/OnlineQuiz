package com.techbytecare.kk.onlinequiz;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.techbytecare.kk.onlinequiz.Common.Common;

public class Playing extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000;  //1 sec
    final static long TIMEOUT = 15000;   //15 sec

    int progressValue = 0;

    CountDownTimer mCountDown;

    int index = 0,score = 0,thisQuestion = 0,totalQuestion,correctAnswer;

    ProgressBar progressBar;

    ImageView question_image;
    Button btnA,btnB,btnC,btnD;
    TextView txtScore,txtQuestionNum,question_text,image_question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        txtScore = (TextView)findViewById(R.id.txtScore);
        txtQuestionNum = (TextView)findViewById(R.id.txtTotalQuestion);
        question_text = (TextView)findViewById(R.id.question_text);
        question_image = (ImageView)findViewById(R.id.question_image);
        image_question_text = (TextView)findViewById(R.id.image_question_text);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnA = (Button)findViewById(R.id.btnAnswerA);
        btnB = (Button)findViewById(R.id.btnAnswerB);
        btnC = (Button)findViewById(R.id.btnAnswerC);
        btnD = (Button)findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        mCountDown.cancel();

        if (index < totalQuestion) { //still have question in list

            Button clickedButton = (Button)view;

            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer()))  {

                //choose correct answer
                score += 10;
                correctAnswer++;
                showQuestion(++index);  //next question
            }
            else    {
                //choose wrong answer
                Intent intent = new Intent(this,Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE",score);
                dataSend.putInt("TOTAL",totalQuestion);
                dataSend.putInt("CORRECT",correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
            }
            txtScore.setText(String.format("%d",score));
        }
    }

    private void showQuestion(int index) {

        if (index < totalQuestion)  {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d",thisQuestion,totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {

                //if it is image
                Picasso.with(getBaseContext()).load(Common.questionList.get(index).getQuestion()).into(question_image);
                image_question_text.setText(Common.questionList.get(index).getImageQuestionText());

                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
                image_question_text.setVisibility(View.VISIBLE);
            }
            else    {

                question_text.setText(Common.questionList.get(index).getQuestion());

                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
                image_question_text.setVisibility(View.INVISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mCountDown.start(); //start timer
        }
        else    {

            //if it is final question
            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();

        mCountDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long milliSec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }
}
