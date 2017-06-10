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

public class Feedback extends AppCompatActivity {

    TextView eventName,participantName,participantEmail;
    ListView feedbackCriteriaListView;
    Button submitButton;
    EditText remarks;
    DatabaseLRHandler db;
    List<String> feedbackCriteriaLinkedList=new ArrayList<>();
    Map<Integer,Float> mapOfFeedBack=new LinkedHashMap<>();
    float [] ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        db=new DatabaseLRHandler(this);
        eventName=(TextView)findViewById(R.id.eventName);
        participantEmail=(TextView)findViewById(R.id.participantEmail);
        participantName=(TextView)findViewById(R.id.participantName);
        remarks=(EditText)findViewById(R.id.remarks);
        eventName.setText(getIntent().getStringExtra("eventName"));
        participantEmail.setText(getIntent().getStringExtra("participantEmail"));
        participantName.setText(getIntent().getStringExtra("participantName"));
        feedbackCriteriaListView=(ListView)findViewById(R.id.feedbackCriteriaListView);
        submitButton=(Button)findViewById(R.id.submitButton);
        feedbackCriteriaLinkedList.add("Did the programme create an awakening for outdoor Activities?");
        feedbackCriteriaLinkedList.add("Did the programme provide the stretch for discovering the potential in you?");
        feedbackCriteriaLinkedList.add("Did the Water Sports activity Increase your confidence level in water ?");
        feedbackCriteriaLinkedList.add("Did the Ice Breaker game help in bringing you closer to one another to form a good team Ice Brea er game?");
        feedbackCriteriaLinkedList.add("Did the Adventures activities create awareness of achieving goals through Team Performance ?");
        feedbackCriteriaLinkedList.add("Did the adventure activities focus on Leadership Development ?");
        feedbackCriteriaLinkedList.add("Keeping in view the overall programme, how do you grade your group Instructor?");
        for(int i=0;i<feedbackCriteriaLinkedList.size();i++){
            mapOfFeedBack.put(i,(float)0);
            populateFeedBackList();
        }

        ratings=new float[feedbackCriteriaLinkedList.size()+1];
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject=new JSONObject();
                SortedSet<Integer> keys=new TreeSet<Integer>(mapOfFeedBack.keySet());
                for(Integer key:keys){
                    try {
                        jsonObject.put(String.valueOf(key),String.valueOf(mapOfFeedBack.get(key)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                jsonArray.put(jsonObject);
                db.submitFeedback(getIntent().getIntExtra("participantId",0),getIntent().getIntExtra("eventId",0),jsonArray.toString(),remarks.getText().toString().trim());
                Toast.makeText(getApplicationContext(), "Feedback submitted!", Toast.LENGTH_SHORT).show();
                Intent iF=new Intent(getApplicationContext(), EventInformationActivity.class);
                iF.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                iF.putExtra("eventName",getIntent().getStringExtra("eventName"));
                iF.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                startActivity(iF);
                finish();
            }

        });

    }



    private void populateFeedBackList(){
        ArrayAdapter<String> adapter=new PopulateFeedbackListAdapter();
        feedbackCriteriaListView.setAdapter(adapter);
    }

    class PopulateFeedbackListAdapter extends ArrayAdapter<String>{
        public PopulateFeedbackListAdapter(){
            super(getApplicationContext(),R.layout.activity_feedback_list_item_layout,feedbackCriteriaLinkedList);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if(row==null){
                row= getLayoutInflater().inflate(R.layout.activity_feedback_list_item_layout,parent,false);
            }
            TextView feedbackCriteria=(TextView)row.findViewById(R.id.feedbackCriteria);
            feedbackCriteria.setText(feedbackCriteriaLinkedList.get(position));
            RatingBar rating=(RatingBar)row.findViewById(R.id.ratingBar);
            rating.setRating(ratings[position]);
            rating.setTag(position);

            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {

                    if(!b)return;
                    int index = (Integer)(ratingBar.getTag());
                    ratings[index] = rating;
                    mapOfFeedBack.remove(position);
                    mapOfFeedBack.put(position,rating);
                }
            });
            return row;
        }
    }
}
