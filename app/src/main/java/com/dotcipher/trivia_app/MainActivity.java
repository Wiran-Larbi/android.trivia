package com.dotcipher.trivia_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcipher.trivia_app.controller.AppController;
import com.dotcipher.trivia_app.data.AnswerListAsyncResponse;
import com.dotcipher.trivia_app.data.QuestionBank;
import com.dotcipher.trivia_app.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionText;
    private TextView questionCounter;
    private Button rightBtn;
    private Button wrongBtn;
    private ImageButton prevBtn;
    private ImageButton nextBtn;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        rightBtn = findViewById(R.id.rightBtn);
        wrongBtn = findViewById(R.id.wrongBtn);
        questionCounter = findViewById(R.id.counterText);
        questionText = findViewById(R.id.questionText);

        // Setting CLick events :
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        wrongBtn.setOnClickListener(this);

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionList = questionArrayList;
                updateCounter();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prevBtn:
                if (currentQuestionIndex > 0){
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                    updateCounter();
                }
                break;
            case R.id.nextBtn:
                if (currentQuestionIndex < questionList.size()){
                    currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                    updateQuestion();
                    updateCounter();
                }
                break;
            case R.id.rightBtn:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.wrongBtn:
                checkAnswer(false);
                updateQuestion();
                break;
        }
    }

    private void checkAnswer(boolean userAnswer) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId = 0;
        if (userAnswer == answerIsTrue) {
            toastMessageId = R.string.right_answer;
            fadeAnimation();
        }else {
            shakeAnimation();
            toastMessageId = R.string.wrong_answer;
        }
        Toast.makeText(MainActivity.this,toastMessageId,Toast.LENGTH_LONG).show();

    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionText.setText(question);
    }
    private void updateCounter() {
        questionCounter.setText( currentQuestionIndex + " out of " + questionList.size());
    }

    private void fadeAnimation() {
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}