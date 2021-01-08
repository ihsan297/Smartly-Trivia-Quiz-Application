package com.example.smartly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class OptActivity extends AppCompatActivity {
    Spinner lvlSpinner,qSpinner,catSpinner,typSpinner;
    ArrayAdapter<String> adapter;
    String selLvl,selCat,selQs,selTyp;
    Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt);
        startGame=(Button) findViewById(R.id.btn_startGame);
        lvlSpinner=findViewById(R.id.level);
        qSpinner=findViewById(R.id.questions);
        catSpinner=findViewById(R.id.category);
        typSpinner=findViewById(R.id.typeOFQs);

        String[] lvl = new String[]{"Easy", "Medium","Hard"}; //string array of cities

        String[] type = new String[]{"MCQs","T/F"}; //string array of cities

        String[] qs = new String[]{"5", "10","15","20"}; //string array of cities

        String[] catr = new String[]{"General Knowledge","Sports","Geography","History","Politics","Art","Celebrities","Animals","Vehicles"}; //string array of cities

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lvl);
        lvlSpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, type);
        typSpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, qs);
        qSpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, catr);
        catSpinner.setAdapter(adapter);



        lvlSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //to collect selected city from dropdown
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selLvl=parent.getSelectedItem().toString(); //assigning selectedcity to variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //to collect selected city from dropdown
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selTyp=parent.getSelectedItem().toString(); //assigning selectedcity to variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        qSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //to collect selected city from dropdown
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selQs=parent.getSelectedItem().toString(); //assigning selectedcity to variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //to collect selected city from dropdown
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selCat=parent.getSelectedItem().toString(); //assigning selectedcity to variable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selTyp.equals("T/F")){
                    Intent intent0=new Intent(getApplicationContext(),TFQsActivity.class);
                    intent0.putExtra("qs",selQs);
                    startActivity(intent0);
                }
                else {
                    Intent intent=new Intent(getApplicationContext(),showQuestionActivity.class);
                    intent.putExtra("qs",selQs);
                    intent.putExtra("cat",selCat);
                    intent.putExtra("lvl",selLvl);
                    startActivity(intent);
                }

            }
        });


    }
}