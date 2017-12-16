package com.example.vlad.quiz;

import android.content.Context;
import android.content.Intent;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.vlad.quiz.Server.LoadQuizEntitiesAT;
import java.util.ArrayList;
import java.util.Collections;


public class Quiz implements Parcelable {
    private Context context;

    private ArrayList<Question> questions;
    private int guessesCount = 0;
    private int correctGuesses = 0;

    public Quiz(Context ctx, int numAnswers) {
        context = ctx;
        LoadQuizEntitiesAT loadQuizEntitiesAT=new LoadQuizEntitiesAT(this);
        loadQuizEntitiesAT.execute();
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){

        }
        Collections.shuffle(getQuestions());
    }

    public void registerGuess(boolean isCorrect) {
        guessesCount++;
        if (isCorrect) {
            correctGuesses++;
        }

        if (guessesCount == questions.size()) {
            Intent intent = new Intent(context, ResultActivity.class);
            intent.putExtra(ResultActivity.GUESSES, correctGuesses);
            context.startActivity(intent);
        }
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public int getQuestionsNum() {
        return questions.size();
    }

    public int getResult() {
        return correctGuesses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(guessesCount);
        parcel.writeInt(correctGuesses);
        parcel.writeList(questions);
    }

    public static final Parcelable.Creator<Quiz> CREATOR
            = new Parcelable.Creator<Quiz>() {
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    private Quiz(Parcel in) {
        guessesCount = in.readInt();
        correctGuesses = in.readInt();
        questions = in.readArrayList(Question.class.getClassLoader());
    }

    public String toString() {
        return "Quiz: " + guessesCount + " " + correctGuesses;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
