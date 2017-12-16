package com.example.vlad.quiz.Server;

import android.os.AsyncTask;

import com.example.vlad.quiz.Question;
import com.example.vlad.quiz.Quiz;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by User on 16.12.2017.
 */

public class LoadQuizEntitiesAT extends AsyncTask<Void,Void, Void> {
    private final String URL="https://quizappby.herokuapp.com/api/entities";
    Quiz quiz;


    public LoadQuizEntitiesAT(Quiz quiz) {
        this.quiz = quiz;
    }

    private void parseEntities(String rowData){
        ArrayList<JSONObject> jsonObjects=new ArrayList<>();
        String trimedData=rowData.substring(1,rowData.length()-1);
        while(true){
            int index=trimedData.indexOf("}");
            if(index==-1)
                break;
            String rowObj= trimedData.substring(0,index+1);
            if(rowObj.startsWith(","))
                rowObj=rowObj.substring(1);
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(rowObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonObjects.add(jsonObject);
            trimedData=trimedData.substring(index+1);
        }
        setQuizQuestions(jsonObjects);
    }

    private void setQuizQuestions(ArrayList<JSONObject> jsonObjects){
        ArrayList<Question> questions=new ArrayList<>();
        try {

            for (JSONObject jsonObject : jsonObjects) {
                String photoPath = jsonObject.getString("picUrl");
                String correctAnswer = jsonObject.getString("correctAnswer");
                String[] answers = jsonObject.getString("allAnswers").split(" ");
                ArrayList<String> answersList=new ArrayList<>();
                Collections.addAll(answersList, answers);
                Question question = new Question(photoPath,correctAnswer,answersList) ;
                questions.add(question);
            }
        }catch (JSONException e){

        }
        quiz.setQuestions(questions);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try
        {
            URL url = new URL (URL);
            URLConnection conn = url.openConnection();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s=reader.readLine();
            parseEntities(s);
        }
        catch (IOException e) {
        }
        return null;
    }
}

