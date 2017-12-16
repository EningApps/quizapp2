package com.example.vlad.quiz;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.example.vlad.quiz.Server.ServerGetPlayersAT;
import com.example.vlad.quiz.Server.ServerSaveResultAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    public static final String GUESSES = "guesses";

    ListView resultsListView;

    ArrayAdapter<String> adapter;

    ArrayList<String> results;
    TextView resultsTextView;
    Button saveResultsButton;

    private ArrayList<String> resFromServer;

    int correctAnswersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        correctAnswersCount = intent.getIntExtra(GUESSES, 0);



        resultsTextView = (TextView) findViewById(R.id.resultsTextView);
        resultsTextView.setText(String.valueOf(correctAnswersCount));

        saveResultsButton = (Button) findViewById(R.id.saveResultsButton);
        saveResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);

                builder.setTitle(R.string.input_name);
                final EditText nameEditText = new EditText(ResultActivity.this);
                builder.setView(nameEditText);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = nameEditText.getText().toString();
                        if (name.equals("")) {
                            name = "Anonymous";
                        }
                        ServerSaveResultAT saveResultAT = new ServerSaveResultAT();
                        saveResultAT.execute(name, String.valueOf(correctAnswersCount));
                        ResultActivity.this.ReadResultsFromServer();
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.create().show();
                ((Button) view).setEnabled(false);
            }
        });

        results = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, R.layout.results_item, results);
        resultsListView = (ListView) findViewById(R.id.resultsListView);
        resultsListView.setAdapter(adapter);

        this.ReadResultsFromServer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ReadResultsFromServer(){
        ServerGetPlayersAT getPlayersAT=new ServerGetPlayersAT(this);
        getPlayersAT.execute();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){

        }
        adapter.clear();
        for(String line: resFromServer){
            adapter.add(line);
        }
    }

//    private void readResults() {
//        adapter.clear();
//        Cursor cursor = db.query(dbHelper.TABLE_NAME, null, null, null, null, null, ResultsDbHelper.KEY_VALUE + " DESC", "10");
//        if (cursor.moveToFirst()) {
//            int nameIndex = cursor.getColumnIndex(ResultsDbHelper.KEY_NAME);
//            int valueIndex = cursor.getColumnIndex(ResultsDbHelper.KEY_VALUE);
//            do {
//                String value = String.valueOf(cursor.getInt(valueIndex));
//                if (value.length() == 1) value = " " + value;
//                adapter.add(value + "   " + cursor.getString(nameIndex));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }

    public ArrayList<String> getResFromServer() {
        return resFromServer;
    }

    public void setResFromServer(ArrayList<String> resFromServer) {
        this.resFromServer = resFromServer;
    }
}
