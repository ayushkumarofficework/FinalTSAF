package com.example.federation.Events;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.federation.GetterSetterClasses.Event;
import com.example.federation.R;
import com.example.federation.SQLite.DatabaseLRHandler;
import com.example.federation.Session.SessionManager;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    SessionManager session;
    ListView eventsList;
    TextView heading;
    ArrayList<Event> eventLinkedList=new ArrayList<>();
    SharedPreferences sharedData;
    DatabaseLRHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        db=new DatabaseLRHandler(this);
        session = new SessionManager(getApplicationContext());
        String name=session.checkLogin();
        if(name!=null)
        { getSupportActionBar().setTitle(name.toString());
        myToolbar.setTitleTextColor(Color.WHITE);}
        else
        {
            finish();
        }
        eventsList=(ListView)findViewById(R.id.eventsListView);
        heading=(TextView)findViewById(R.id.heading);
        sharedData=getSharedPreferences("Tata", Context.MODE_PRIVATE);
        String email=sharedData.getString("email","");
        eventLinkedList=db.getEventListUnderInstructor(email);
        for(int i=0;i<eventLinkedList.size();i++){
            Log.d("tag",String.valueOf(eventLinkedList.get(i).getEventId()));
        }
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in =new Intent(getApplicationContext(),EventInformationActivity.class);
                in.putExtra("eventName",eventLinkedList.get(i).getEventName());
                in.putExtra("eventId",eventLinkedList.get(i).getEventId());
                in.putExtra("eventCode",eventLinkedList.get(i).getEventCode());
                startActivity(in);
            }
        });
        if(eventLinkedList.size()!=0){
            Log.d("tata","linkedlist is fine");
            heading.setText("Events");
            for(int i=0;i<eventLinkedList.size();i++){
                populateEventList();

            }
        }
        else{
            Log.d("tata","linkedlist is null");
            heading.setText("Add a new Event");
        }
    }

    private void populateEventList(){
        ArrayAdapter<Event> adapter=new EventListAdapter();
        eventsList.setAdapter(adapter);
    }
    class EventListAdapter extends ArrayAdapter<Event>{

        public EventListAdapter() {
            super(getApplicationContext(),R.layout.event_list_item,eventLinkedList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.event_list_item,parent,false);
            }
            final Event currentEvent=eventLinkedList.get(position);
            TextView eventName=(TextView)convertView.findViewById(R.id.eventName);
            TextView eventVenue=(TextView)convertView.findViewById(R.id.eventVenue);
            TextView eventCode=(TextView)convertView.findViewById(R.id.eventCode);

            eventCode.setText(currentEvent.getEventCode());
            eventName.setText(currentEvent.getEventName());
            eventVenue.setText(currentEvent.getEventVenue());
            return  convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_events:
                Intent i=new Intent(this,CreateEvent.class);
                startActivity(i);
                return true;
            case R.id.action_logout:
                session.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
