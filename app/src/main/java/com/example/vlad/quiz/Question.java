package com.example.vlad.quiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by User on 16.12.2017.
 */

public class Question implements Parcelable {
    String photoPath;
    String correctAnswer;
    ArrayList<String> answers;

    public Question(String photoPath, String correctAnswer, ArrayList<String> answers) {
        this.photoPath = photoPath;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(photoPath);
        parcel.writeString(correctAnswer);
        parcel.writeList(answers);
    }

    public final Parcelable.Creator<Question> CREATOR
            = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    private Question(Parcel in) {
        photoPath = in.readString();
        correctAnswer = in.readString();
        answers = in.readArrayList(Question.class.getClassLoader());
    }
}