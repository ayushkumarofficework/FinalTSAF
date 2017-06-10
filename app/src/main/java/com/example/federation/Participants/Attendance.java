package com.example.federation.Participants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.federation.Events.EventInformationActivity;
import com.example.federation.GetterSetterClasses.ParticipantAttendance;
import com.example.federation.R;
import com.example.federation.SQLite.DatabaseLRHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Attendance extends AppCompatActivity {

    ListView participantAttendanceList;

    TextView attendanceStatus;

    Button bSubmit;
    DatabaseLRHandler db=new DatabaseLRHandler(this);
    ArrayList<ParticipantAttendance> participantAttendanceLinkedList = new ArrayList<>();

    public static boolean morningStatus = false, eveningStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        participantAttendanceList=(ListView)findViewById(R.id.attendanceaListView);
        attendanceStatus=(TextView)findViewById(R.id.attendanceType);
        bSubmit=(Button)findViewById(R.id.submitAttendanceButton);
        participantAttendanceLinkedList=db.getParticipantListForAttendance(getIntent().getIntExtra("eventId",0));
        for(int i=0;i<participantAttendanceLinkedList.size();i++){
            populateParticipantAttendanceList();
        }
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String todayDate=String.valueOf(dateFormat.format(date));
                Log.d("tag",todayDate);
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("Tata",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if(!sharedPreferences.getString("lastAttendance","").equals(todayDate)){
                    Log.d("tag",sharedPreferences.getString("lastAttendance","null"));

                    {
                        db.submitAttendance(participantAttendanceLinkedList,"morning");
                        editor.putBoolean("morning",true);
                        editor.putBoolean("evening",false);
                        editor.putString("lastAttendance",todayDate);
                        editor.commit();

                        Toast.makeText(getApplicationContext(),"Morning attendance is done!",Toast.LENGTH_SHORT).show();
                        Intent in=new Intent(getApplicationContext(), EventInformationActivity.class);
                        in.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                        in.putExtra("eventName",getIntent().getStringExtra("eventName"));
                        in.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                        startActivity(in);
                        finish();
                    }
                }
                else{
                    if(sharedPreferences.getBoolean("morning",false)==true&&sharedPreferences.getBoolean("evening",false)==false){
                        db.submitAttendance(participantAttendanceLinkedList,"evening");
                        editor.putBoolean("morning",true);
                        editor.putBoolean("evening",true);
                        editor.putString("lastAttendance",todayDate);
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Evening attendance is done!",Toast.LENGTH_SHORT).show();
                        Intent in=new Intent(getApplicationContext(), EventInformationActivity.class);
                        in.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                        in.putExtra("eventName",getIntent().getStringExtra("eventName"));
                        in.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                        startActivity(in);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Today's attendance is already done!",Toast.LENGTH_SHORT).show();
                        Intent in=new Intent(getApplicationContext(), EventInformationActivity.class);
                        in.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                        in.putExtra("eventName",getIntent().getStringExtra("eventName"));
                        in.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                        startActivity(in);
                        finish();
                    }
                }
            }
        });
    }





    private void populateParticipantAttendanceList() {
        ArrayAdapter<ParticipantAttendance> adapter = new ParticipantAttendanceAdapter(Attendance.this,participantAttendanceLinkedList);
        participantAttendanceList.setAdapter(adapter);
    }




    public static class ParticipantAttendanceAdapter extends ArrayAdapter<ParticipantAttendance> {
        List<ParticipantAttendance> list;
        Context context;
        SparseBooleanArray sba=new SparseBooleanArray();


        public ParticipantAttendanceAdapter(Context context, ArrayList<ParticipantAttendance> list) {
            super(context, R.layout.activity_attendance_list_item_layout, list);
            this.context = context;
            this.list = list;
            //Log.d(TAG, "participant attendance constructor is called");
        }


        public static class ViewHolder {
            protected CheckBox presentCheckBox;
            protected TextView participantName;
            protected EditText reason;
        }


        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v=convertView;

            final ParticipantAttendance wed = list.get(position);

            if (v == null) {


                LayoutInflater inflator = LayoutInflater.from(context);

                v = inflator.inflate(R.layout.activity_attendance_list_item_layout, null);

                final ViewHolder viewHolder = new ViewHolder();

                viewHolder.participantName = (TextView) v.findViewById(R.id.participantNameInAttendance);
                viewHolder.presentCheckBox = (CheckBox) v.findViewById(R.id.presentCheckbox);
                viewHolder.reason = (EditText)v.findViewById(R.id.reason);

                v.setTag(viewHolder);
                viewHolder.participantName.setTag(wed);
                viewHolder.presentCheckBox.setTag(wed);
                viewHolder.reason.setTag(wed);
            } else {
                ViewHolder holder = (ViewHolder) v.getTag();
                holder.participantName.setTag(wed);
                holder.presentCheckBox.setTag(wed);
                holder.reason.setTag(wed);
            }
            final ViewHolder viewHolder = (ViewHolder) v.getTag();
            final ViewHolder finalViewHolder1 = viewHolder;
            viewHolder.reason.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    ParticipantAttendance element = (ParticipantAttendance) finalViewHolder1.reason.getTag();
                    element.setParticipantReasonForAbsent(charSequence.toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });



            viewHolder.participantName.setText(wed.getParticipantName());
            viewHolder.reason.setText(wed.getParticipantReasonForAbsent());
            viewHolder.presentCheckBox.setChecked(sba.get(position));
            viewHolder.presentCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(viewHolder.presentCheckBox.isChecked())
                    {
                        wed.setParticipantTodaysAttendanceStatus(true);
                        sba.put(position, true);
                    }

                    else
                    {
                        sba.put(position, false);
                    }
                }
            });
            return v;
        }
    }





}
