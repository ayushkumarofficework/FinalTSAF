package com.example.federation.Events;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.federation.GetterSetterClasses.Participant;
import com.example.federation.Participants.AddParticipantActivity;
import com.example.federation.Participants.Attendance;
import com.example.federation.Participants.Evaluation;
import com.example.federation.Participants.Feedback;
import com.example.federation.Participants.Questionnaire;
import com.example.federation.R;
import com.example.federation.SQLite.DatabaseLRHandler;
import com.example.federation.Session.SessionManager;
import com.example.federation.Volley.MyVolleyClass;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventInformationActivity extends AppCompatActivity {
    ArrayList<Participant> participantLinkedList=new ArrayList<>();
    DatabaseLRHandler db;
    ListView participantListView;
    TextView heading;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information);
        setTitle(getIntent().getStringExtra("eventName")+" ("+getIntent().getStringExtra("eventCode")+")");
        db=new DatabaseLRHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        session = new SessionManager(getApplicationContext());
        String name=session.checkLogin();
        if(name!=null)
        { getSupportActionBar().setTitle(name.toString());
            myToolbar.setTitleTextColor(Color.WHITE);}
        participantLinkedList=db.getParticipantList(getIntent().getIntExtra("eventId",0));
        participantListView=(ListView)findViewById(R.id.participantListView);
        if(participantLinkedList.size()!=0)
        {
            for(int i=0;i<participantLinkedList.size();i++){
                participantLinkedList.get(i).getParticipantName();
            }
        }


        heading=(TextView)findViewById(R.id.heading);
        if(participantLinkedList.size()!=0){
            //heading.setText("Participants");
                       for(int i=0;i<participantLinkedList.size();i++){
                populateParticipantList();
            }
        }
        else{
           // heading.setText("Add Participants");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.part_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_participants:
                Intent in=new Intent(getApplicationContext(),AddParticipantActivity.class);
                in.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                in.putExtra("eventName",getIntent().getStringExtra("eventName"));
                in.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                startActivity(in);
                return true;
            case R.id.action_takeAttendance:
                Intent i=new Intent(getApplicationContext(),Attendance.class);
                i.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                i.putExtra("eventName",getIntent().getStringExtra("eventName"));
                i.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                startActivity(i);
                return true;
            case R.id.action_image:
                Intent intent=new Intent(this,UploadImage.class);
                intent.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                intent.putExtra("eventName",getIntent().getStringExtra("eventName"));
                intent.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                startActivity(intent);
                return  true;
            case R.id.action_logout:
                session.logoutUser();
                return true;
            case R.id.action_sync:
                sendSyncData(getIntent().getStringExtra("eventCode"),getIntent().getIntExtra("eventId",0));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }



    private void sendSyncData(String eventCode,int eventId){
        //todo write sync code
        Toast.makeText(getApplicationContext(),"Sending data in Background",Toast.LENGTH_SHORT).show();
        DatabaseLRHandler db=new DatabaseLRHandler(this);
        JSONArray participantData=new JSONArray();
        final String participantDataString,eventDataString,feedbackDataString,evaluationDataString,preQuestionnairDataString,postQuestionnairDataString,
                morningAttendanceDataString,eveningAttendanceDataString;
        try {
            participantData=db.getParticioantsData(eventId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        participantDataString=participantData.toString();
         JSONArray eventData=new JSONArray();
        eventData=db.getEventData(eventId);
        eventDataString=eventData.toString();
        JSONArray feedbackData=new JSONArray();
        feedbackData=db.getFeedbackData(eventId);
        feedbackDataString=feedbackData.toString();
        JSONArray evaluationData=new JSONArray();
        evaluationData=db.getEvaluationData(eventId);
        evaluationDataString=evaluationData.toString();
        //pre post morning evening
        JSONArray preQuestionnairData=new JSONArray();
        preQuestionnairData=db.getPreQuestionnairData(eventId);
        preQuestionnairDataString=preQuestionnairData.toString();
        JSONArray postQuestionnairData=new JSONArray();
        postQuestionnairData=db.getPostQuestionnairData(eventId);
        postQuestionnairDataString=postQuestionnairData.toString();
        JSONArray morningAttendance=new JSONArray();
        morningAttendance=db.getMorningAttendance(eventId);
        morningAttendanceDataString=morningAttendance.toString();
        JSONArray eveningAttendance=new JSONArray();
        eveningAttendance=db.getEveningAttendance(eventId);
        eveningAttendanceDataString=eveningAttendance.toString();
        Log.d("eventdata",eventData.toString());
        Log.d("feedbackdata",feedbackData.toString());
        Log.d("evaluationdata",evaluationData.toString());
        Log.d("preQuestionnair",preQuestionnairData.toString());
        Log.d("postQdata",postQuestionnairData.toString());
        Log.d("morningAttendance",morningAttendance.toString());
        Log.d("eeveningAttendance",eveningAttendance.toString());
        SharedPreferences pref=this.getSharedPreferences("Tata",MODE_PRIVATE);
        final String email=pref.getString("email","");
        final String name=pref.getString("name","");
        final String eventcode=getIntent().getStringExtra("eventCode");
        String url="https://tsafindia.org/tsafadminpanel/tsaf/Appcontrollers/finalSync";
        //String url="http://10.51.1.19/finalSync.php";
        if(!email.equals("")){

            StringRequest request=new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {
                    Log.d("response",response);
                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Something went wrong try again!",Toast.LENGTH_SHORT).show();
                    Log.d("error",error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("instructorEmail",email);
                    params.put("eventData",eventDataString);
                    params.put("participantsData",participantDataString);
                    params.put("feedbackData",feedbackDataString);
                    params.put("evaluationData",evaluationDataString);
                    params.put("preQuestionnairData",preQuestionnairDataString);
                    params.put("postQuestionnairData",postQuestionnairDataString);
                    params.put("morningAttendance",morningAttendanceDataString);
                    params.put("eveningAttendance",eveningAttendanceDataString);
                    params.put("instructorName",name);
                    params.put("eventCode",eventcode);
                    Log.d("params",params.toString());
                    return params;
                }
            };
            MyVolleyClass.getInstance(EventInformationActivity.this).addToRequestQueue(request);
        }
    }




    private void populateParticipantList(){
        ArrayAdapter<Participant> adapter=new ParticipantAdapter();
        participantListView.setAdapter(adapter);
    }
    class ParticipantAdapter extends ArrayAdapter<Participant>{

        public ParticipantAdapter() {
            super(getApplicationContext(),R.layout.participant_item_layout,participantLinkedList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.participant_item_layout,parent,false);
            }

            final Participant currentParticipant = participantLinkedList.get(position);
            TextView participantName= (TextView) convertView.findViewById(R.id.participantName);
            participantName.setText(currentParticipant.getParticipantName());
            ImageView imageMore = (ImageView) convertView.findViewById(R.id.imageMore);

            try {

                imageMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.imageMore:
                                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                                popup.getMenuInflater().inflate(R.menu.menu_list_of_participants, popup.getMenu());
                                popup.show();
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.Evaluate:
                                                if(currentParticipant.getParticipantEvaluation()==0){
                                                    Intent i=new Intent(getApplicationContext(),Evaluation.class);
                                                    i.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                                                    i.putExtra("eventName",getIntent().getStringExtra("eventName"));
                                                    i.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                                                    i.putExtra("participantId",currentParticipant.getParticipantId());
                                                    i.putExtra("ParticipantName",currentParticipant.getParticipantName());
                                                    startActivity(i);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"Participant already Evaluated!",Toast.LENGTH_SHORT).show();
                                                }

                                                break;
                                            case R.id.Feedback:
                                                if(currentParticipant.getParticipantFeedback()==0){
                                                    Intent iF=new Intent(getApplicationContext(),Feedback.class);
                                                    iF.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                                                    iF.putExtra("eventName",getIntent().getStringExtra("eventName"));
                                                    iF.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                                                    iF.putExtra("participantId",currentParticipant.getParticipantId());
                                                    iF.putExtra("ParticipantName",currentParticipant.getParticipantName());
                                                    startActivity(iF);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"Feedback Already taken!",Toast.LENGTH_SHORT).show();
                                                }

                                                break;
                                            case R.id.Questionnaire:
                                                if(currentParticipant.getParticipantPreQuestionnair()==0||currentParticipant.getParticipantPostQuestionnair()==0){
                                                    Intent iQ=new Intent(getApplicationContext(),Questionnaire.class);
                                                    iQ.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                                                    iQ.putExtra("eventName",getIntent().getStringExtra("eventName"));
                                                    iQ.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                                                    iQ.putExtra("participantId",currentParticipant.getParticipantId());
                                                    iQ.putExtra("ParticipantName",currentParticipant.getParticipantName());
                                                    iQ.putExtra("questionnairStatus",currentParticipant.getParticipantPreQuestionnair());
                                                    startActivity(iQ);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"Questionnair already Submitted!",Toast.LENGTH_SHORT).show();
                                                }

                                                break;

                                            default:
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                break;
                            default:
                                break;
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            participantName.setText(currentParticipant.getParticipantName());










            return convertView;
        }
    }
}
