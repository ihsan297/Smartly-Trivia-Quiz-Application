package com.example.smartly;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class showQuestionActivity extends AppCompatActivity {
    TextView txt_qs,optA,optB,optC,optD,qn;
    Button btn_next;
    int questionNumber;
    RadioGroup radioGroup;
    String userAns,level,cat;
    int noOfQs,score;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question);
        userAns="";
        score=0;

        radioGroup=findViewById(R.id.radGrp);
        qn=findViewById(R.id.numbering);
        txt_qs=findViewById(R.id.txt_qs);
        optA=findViewById(R.id.optA);
        optB=findViewById(R.id.optB);
        optC=findViewById(R.id.optC);
        optD=findViewById(R.id.optD);
        btn_next=findViewById(R.id.next);
        questionNumber=0;
        Intent intent=getIntent();
        level=intent.getStringExtra("lvl");
        cat=intent.getStringExtra("cat");
        noOfQs=Integer.parseInt(intent.getStringExtra("qs"));
         new GetQuestions().execute();

    }






    private class GetQuestions extends AsyncTask<Void, Void, Void> {
        ArrayList<Question> questions;
        GetQuestions(){
            questions=new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "https://opentdb.com/api.php?amount="+noOfQs+"&category=9&difficulty"+level+"&type=multiple";


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            getQuestions(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", "Etrooroor");
                }
            });
            queue.add(stringRequest);

            return null;

        }
        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);




        }
        void getQuestions(String response){

            try {
                JSONObject jobj = new JSONObject(response);
                JSONArray reader=jobj.getJSONArray("results");


                for(int k=0;k<noOfQs;++k){

                    StringTokenizer st = new StringTokenizer(reader.getString(k),":");
                    String[] type=new String[st.countTokens()];
                    int i=0;
                    while(st.hasMoreTokens()){
                        type[i]=st.nextToken();
                        i=i+1;
                    }
                    String question=type[4];
                    st=new StringTokenizer(question,",");
                    String[] qs=new String[st.countTokens()];
                    i=0;
                    while (st.hasMoreTokens()){
                        qs[i]=st.nextToken();
                        i++;
                    }
                    question=qs[0];
                    question=question.substring(1,question.length()-1);

                    String correctAns=type[5];
                    st=new StringTokenizer(correctAns,",");
                    qs=new String[st.countTokens()];
                    i=0;
                    while (st.hasMoreTokens()){
                        qs[i]=st.nextToken();
                        i++;
                    }
                    correctAns=qs[0];
                    correctAns=correctAns.substring(1,correctAns.length()-1);



                    String incor=type[6];
                    st=new StringTokenizer(incor,",");
                    qs=new String[st.countTokens()];
                    i=0;
                    while (st.hasMoreTokens()){
                        qs[i]=st.nextToken();
                        i++;
                    }
                    String op1=qs[0].substring(2,qs[0].length()-1);
                    String op2=qs[1].substring(1,qs[1].length()-1);
                    String op3=qs[2].substring(1,qs[2].length()-3);
                    ArrayList<String> strings=new ArrayList<>();
                    strings.add(op1);
                    strings.add(op2);
                    strings.add(op3);
                    strings.add(correctAns);
                    Collections.shuffle(strings);
                    Question question1=new Question();
                    question1.setQuestion(question);
                    question1.setOption_a(strings.get(0));
                    question1.setOption_b(strings.get(1));
                    question1.setOption_c(strings.get(2));
                    question1.setOption_d(strings.get(3));
                    question1.setAns(correctAns);
                    questions.add(question1);



                }
                question=getAQuestion(questions);
//                questionNumber++;
                txt_qs.setText(question.getQuestion());
                optA.setText(question.getOption_a());
                optB.setText(question.getOption_b());
                optC.setText(question.getOption_c());
                optD.setText(question.getOption_d());

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.optA){
                            userAns=optA.getText().toString();

                        }
                        else if(checkedId==R.id.optB){
                            userAns=optB.getText().toString();
                        }
                        else if(checkedId==R.id.optC){
                            userAns=optC.getText().toString();
                        }
                        else if(checkedId==R.id.optD){
                            userAns=optD.getText().toString();
                        }
                        else{
                            userAns=null;
                        }
                    }
                });
                Log.d("UserAns",userAns);

                radioGroup.clearCheck();;
                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        qn.setText(String.valueOf(questionNumber+1));
                        if(userAns==null){
                            Toast.makeText(getApplicationContext(),"Must Choose an Option",Toast.LENGTH_LONG).show();
                        }
                        else {
                            if(userAns.equals(question.getAns())){
                                if(level.equals("Easy")){
                                    score++;
                                }else if(level.equals("Medium")){
                                    score=score+2;
                                }
                                else {
                                    score=score+3;
                                }

                            }


                            radioGroup.clearCheck();
                            if (questionNumber==questions.size()){
                                Intent intent=new Intent(getApplicationContext(),resultActivity.class);
                                intent.putExtra("score",String.valueOf(score));
                                startActivity(intent);
                                finish();
                            }
                            else {

                                question = getAQuestion(questions);
                                questionNumber++;
                                txt_qs.setText(question.getQuestion());
                                optA.setText(question.getOption_a());
                                optB.setText(question.getOption_b());
                                optC.setText(question.getOption_c());
                                optD.setText(question.getOption_d());
                            }

                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Question getAQuestion(ArrayList<Question> questions){

            return questions.get(questionNumber);
        }
    }

}