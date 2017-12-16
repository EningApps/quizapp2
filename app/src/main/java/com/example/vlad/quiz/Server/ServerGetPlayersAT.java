package com.example.vlad.quiz.Server;


import android.os.AsyncTask;
import android.util.Log;

import com.example.vlad.quiz.Quiz;
import com.example.vlad.quiz.ResultActivity;

import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by User on 10.12.2017.
 */

public class ServerGetPlayersAT extends AsyncTask<Void ,Void ,Void> {
    private static final String URLadress="https://quizappby.herokuapp.com/api/";

    ResultActivity activity;

    public ServerGetPlayersAT(ResultActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try
        {
            URL url = new URL (URLadress+"notes/");
            URLConnection conn = url.openConnection();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s=reader.readLine();
            Log.d("MYTAG", s);
            activity.setResFromServer(getParsedPlayers(s));
        }
        catch (IOException e) {
        }
        return null;
    }

    private ArrayList<String> getParsedPlayers(String s){
        ArrayList<String> result=new ArrayList<>();
        String trimed=s.substring(1,s.length()-1);
        String[] jsonObjects=trimed.split("playerName");
        for (int i = 1; i < jsonObjects.length; i++) {
            String obj=jsonObjects[i];
            String name = obj.substring(3, obj.indexOf("\"",3));
            int pos=obj.indexOf(":",5);
            String score = obj.substring(pos+2,obj.indexOf("\"",pos+2));
            result.add(score+" "+name);
        }
        return result;
    }
}
