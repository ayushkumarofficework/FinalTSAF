package com.example.federation.Participants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.federation.Events.EventInformationActivity;
import com.example.federation.R;
import com.example.federation.SQLite.DatabaseLRHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Evaluation extends AppCompatActivity {

    TextView participantName,eventName;
    ListView evaluationCriteriaListView;
    EditText maxAltitude;
    EditText remarks;
    List<String> criteriaLinkedList=new ArrayList<>();
    Map<Integer,Float> mapOfRatings=new LinkedHashMap<>();
    Button submitButton;
    float [] ratings;
    DatabaseLRHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        db=new DatabaseLRHandler(this);
        maxAltitude=(EditText)findViewById(R.id.maxAltitude);
        remarks=(EditText)findViewById(R.id.remarks);
        participantName=(TextView)findViewById(R.id.participantName);
        eventName=(TextView)findViewById(R.id.eventName);
        participantName.setText(getIntent().getStringExtra("participantName"));
        eventName.setText(getIntent().getStringExtra("eventName"));
        evaluationCriteriaListView=(ListView)findViewById(R.id.evaluationCriteria);
        criteriaLinkedList.add("fitness");
        criteriaLinkedList.add("punctuality");

        ratings=new float[criteriaLinkedList.size()+1];
        for(int i=0;i<criteriaLinkedList.size();i++){
            mapOfRatings.put(i,(float)0);
            populateEvaluationCriteriaListView();
        }

        submitButton=(Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject=new JSONObject();
                SortedSet<Integer> keys=new  TreeSet<Integer>(mapOfRatings.keySet());
                for(Integer key:keys){
                    try {
                        jsonObject.put(String.valueOf(key),mapOfRatings.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                jsonArray.put(jsonObject);
                db.submitEvaluation(getIntent().getIntExtra("participantId",0),getIntent().getIntExtra("eventId",0),jsonArray.toString(),remarks.getText().toString().trim());
                Toast.makeText(getApplicationContext(), "Evaluation submitted!", Toast.LENGTH_SHORT).show();
                Intent iF=new Intent(getApplicationContext(), EventInformationActivity.class);
                iF.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                iF.putExtra("eventName",getIntent().getStringExtra("eventName"));
                iF.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                startActivity(iF);
                finish();
            }
        });

    }











    private void  populateEvaluationCriteriaListView(){
        ArrayAdapter<String> adapter=new EvaluationListAdapter();
        evaluationCriteriaListView.setAdapter(adapter);
    }
    class EvaluationListAdapter extends ArrayAdapter<String>{
        public EvaluationListAdapter(){
            super(getApplicationContext(),R.layout.activity_evaluation_list_item_layout,criteriaLinkedList);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= getLayoutInflater().inflate(R.layout.activity_evaluation_list_item_layout,parent,false);
            }
            TextView criteria=(TextView)convertView.findViewById(R.id.criteria);
            RatingBar rating=(RatingBar)convertView.findViewById(R.id.ratingBar);
            rating.setRating(ratings[position]);
            rating.setTag(position);
            String criteriaString=criteriaLinkedList.get(position);
            criteria.setText(criteriaString);
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                    if(!b)return;

                    int index = (Integer)(ratingBar.getTag());
                    ratings[index] = rating;
                    mapOfRatings.remove(position);
                    mapOfRatings.put(position,rating);
                }
            });
            return convertView;
        }
    }
}
