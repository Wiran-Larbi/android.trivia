package com.dotcipher.trivia_app.data;

import com.dotcipher.trivia_app.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);

}
