package com.example.federation.Participants;

import android.app.ProgressDialog;
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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Questionnaire extends AppCompatActivity {

    public static boolean status=false;
    JSONArray jsonArrayOfQuestionair=new JSONArray();
    Map<Integer,Float> mapOfQuestionair=new LinkedHashMap<>();
    ListView criteriaListView;
    List<String> criteriaList=new LinkedList<>();
    Button submitButton;
    TextView type;
    EditText remarks;
    ProgressDialog mProgress;
    DatabaseLRHandler db;

    float[]ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        db=new DatabaseLRHandler(this);
        criteriaList.add("I am often able to identify the strengths and weaknesses of my team members");
        criteriaList.add("I am able to convey instructions to my colleagues effectively ");
        criteriaList.add("I always change my thinking or opinions if there is a better idea");
        criteriaList.add("I am well aware of how other people are feeling and what they are thinking about ");
        criteriaList.add("When working on a project, I always take the initiative to understand the details right ");
        criteriaList.add("I know how to use my team's resources to complete a task");
        criteriaList.add("I feel it is better to offer help to team members only when it is genuinely required.  ");
        criteriaList.add("Encourage people to push boundaries in order to complete a task ");
        criteriaList.add("I often tend to overestimate my capabilities  ");
        criteriaList.add("I am always honest and open about my capabilities with respect to a particular task  ");
        criteriaList.add("I often feel that I know myself better than how other people know themselves ");
        criteriaList.add("I feel very reluctant to take risks and going after what I want");
        criteriaList.add("I can clearly admit what I can offer to the team task ");
        criteriaList.add("I never have thoughts like, Things would be easier if I had a clear sense for what I really want ");
        criteriaList.add("I tend to get confused when making decisions under pressure ");
        criteriaList.add("I feel it is better to give up if initial attempts have been unsuccessful ");
        criteriaList.add("If the team is dissatisfied with how things are working right now, change is more likely to be successful ");
        criteriaList.add("I like to be an active-go getter in uncertain situations ");
        criteriaList.add("It is always better to make a comprehensive plan before starting work");
        criteriaList.add("I can adjust well with people from different cultures");
        criteriaList.add("I opt for challenging work over routine tasks");
        criteriaList.add("It is better to overlook the bigger picture if it can save time");
        criteriaList.add("I am very comfortable working in an unfamiliar environment ");
        criteriaList.add("It is not a good idea to crack jokes in a crisis ");
        criteriaList.add("I feel I can survive without electricity for more than 4 hours on a regular basis  ");
        criteriaList.add("I feel it is not necessary for me to be available on my mobile 24 7 ");
        criteriaList.add("I often feel the need to use less water while bathing ");
        criteriaList.add("I always prefer to walk over any other form of pubic or private transport to get to a nearby place");
        criteriaList.add("I enjoy watching birds and observing the natural scene in my leisure time");


        ratings=new float[criteriaList.size()+1];



        type=(TextView)findViewById(R.id.questionairType);
        criteriaListView=(ListView)findViewById(R.id.criteriaListView);
        submitButton=(Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject=new JSONObject();
                SortedSet<Integer> keys = new TreeSet<Integer>(mapOfQuestionair.keySet());
                for (Integer key : keys) {
                    float value = mapOfQuestionair.get(key);
                    try {
                        jsonObject.put(String.valueOf(key),String.valueOf(value));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // do something
                }
                jsonArray.put(jsonObject);
                //insert data in sqlite
                db.submitQuestionnair(getIntent().getIntExtra("eventId",0),getIntent().getIntExtra("participantId",0),jsonArray.toString(),getIntent().getIntExtra("questionnairStatus",0));
                Toast.makeText(getApplicationContext(), "Questionnair submitted!", Toast.LENGTH_SHORT).show();
                Intent iF=new Intent(getApplicationContext(), EventInformationActivity.class);
                iF.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                iF.putExtra("eventName",getIntent().getStringExtra("eventName"));
                iF.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                startActivity(iF);
                finish();


            }
        });
        remarks=(EditText)findViewById(R.id.remarks);
        if(getIntent().getIntExtra("questionairStatus",0)==1){
            status=true;
            setTitle(getIntent().getStringExtra("participantName")+" Questionnair!");
            type.setText("Post Questionnair");
        }
        else{
            setTitle(getIntent().getStringExtra("participantName")+" Questionnair!");
            type.setText("Pre Questionnair");
        }
        for(int i=0;i<criteriaList.size();i++){
            mapOfQuestionair.put(i,(float)0);
            populateCriteriaList();
        }
    }



    private void populateCriteriaList(){
        ArrayAdapter<String> adapter=new CriteriaListAdapter();
        criteriaListView.setAdapter(adapter);
    }
    public class CriteriaListAdapter extends ArrayAdapter<String>{

        public CriteriaListAdapter() {
            super(getApplicationContext(),R.layout.activity_questionnaire_list_item_layout,criteriaList);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.activity_questionnaire_list_item_layout,parent,false);
            }
            TextView criteria=(TextView)convertView.findViewById(R.id.criteria);
            final String currentCriteria=criteriaList.get(position);
            criteria.setText(currentCriteria);
            RatingBar rating=(RatingBar)convertView.findViewById(R.id.criteriaRating);
            rating.setRating(ratings[position]);
            rating.setTag(position);
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                    if(!b)return;
                    int index = (Integer)(ratingBar.getTag());
                    ratings[index] = rating;
                    mapOfQuestionair.remove(position);
                    mapOfQuestionair.put(position,rating);
                }
            });
            return convertView;
        }
    }
}
