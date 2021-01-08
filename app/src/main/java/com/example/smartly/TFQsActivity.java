package com.example.smartly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.StringTokenizer;

public class TFQsActivity extends AppCompatActivity {
    TextView txt_qs,qn;
    Button next;
    int questionNumber,noOfQs,score;
    RadioGroup radioGroup;
    boolean userAns;
    boolean isNotChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_f_qs);
        qn=findViewById(R.id.tf_numbering);
        noOfQs= Integer.parseInt( getIntent().getStringExtra("qs"))  ;
        score=0;
        radioGroup=findViewById(R.id.TFradGrp);
        questionNumber=0;
        next=findViewById(R.id.tf_next);
        txt_qs=findViewById(R.id.txt_tfq);
        new GetQuestions().execute();


    }


    private class GetQuestions extends AsyncTask<Void, Void, Void> {
        ArrayList<TFQuestion> questions;
        GetQuestions(){
            questions=new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "https://opentdb.com/api.php?amount=10&category=11&difficulty=easy&type=boolean";


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("Res",response);
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
                for (int k=0;k<noOfQs;++k) {


                    StringTokenizer st = new StringTokenizer(reader.getString(k), ",");
                    String[] type = new String[st.countTokens()];
                    int i = 0;
                    while (st.hasMoreTokens()) {
                        type[i] = st.nextToken();
                        i = i + 1;
                    }
                    String question = type[3];


                    question = question.substring(12, question.length() - 3);
                    Log.d("Qs", question);

                    String correctAns = type[5];
                    TFQuestion tfQuestion = new TFQuestion();
                    tfQuestion.setQuestion(question);
                    try {
                        correctAns = correctAns.substring(22, correctAns.length() - 3);
                    }catch (IndexOutOfBoundsException e){
                        correctAns="False";
                    }


                    Log.d("CANS", correctAns);
                    if (correctAns.equals("True")) {
                        tfQuestion.setAns(true);
                    } else {
                        tfQuestion.setAns(false);
                    }
                    questions.add(tfQuestion);
                }
                TFQuestion question=getAQuestion(questions);
                txt_qs.setText(question.getQuestion());
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.tr){
                            userAns=true;

                        }
                        else if(checkedId==R.id.fl){
                            userAns=false;
                        }

                        else{
                            isNotChecked=true;
                        }
                    }
                });
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        radioGroup.clearCheck();
                        questionNumber++;
                        qn.setText(String.valueOf(questionNumber+1));
                        if (!isNotChecked) {
                            Toast.makeText(getApplicationContext(), "Must Choose an Option", Toast.LENGTH_LONG).show();
                        }
                        else {


                            if (userAns == question.ans) {
                                score++;
                            }
                            if (questionNumber == noOfQs - 1) {
                                Intent intent=new Intent(getApplicationContext(),resultActivity.class);
                                intent.putExtra("score",String.valueOf(score));
                                startActivity(intent);
                            }

                            TFQuestion q = getAQuestion(questions);
                            txt_qs.setText(q.getQuestion());
                        }
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    TFQuestion getAQuestion(ArrayList<TFQuestion> questions){

        return questions.get(questionNumber);
    }
}