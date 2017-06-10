package com.example.federation.Events;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.federation.R;
import com.example.federation.SQLite.DatabaseLRHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aakash on 15-Apr-17.
 */

public class CreateEvent extends AppCompatActivity {
    EditText eventCode, eventName, eventVenue;
    Button submit;
    TextView   eventStartingDate, eventEndingDate;
    DatabaseLRHandler db;
    String startingDate;
    String endingDate;
    int smYear,smMonth,smDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        eventCode = (EditText) findViewById(R.id.event_code);
        eventName = (EditText) findViewById(R.id.event_name);
        eventVenue = (EditText) findViewById(R.id.event_venue);
        eventStartingDate = (TextView) findViewById(R.id.eventStartingDate);

        eventStartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                smYear = mcurrentDate.get(Calendar.YEAR);
                smMonth = (mcurrentDate.get(Calendar.MONTH));
                smDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        selectedmonth=selectedmonth+1;

                        startingDate =  selectedyear+"-"+selectedmonth+"-"+selectedday;
                        eventStartingDate.setText("" + startingDate);
                    }
                }, smYear, smMonth, smDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();


            }
        });

        eventEndingDate = (TextView) findViewById(R.id.eventEndingDate);
        eventEndingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentDate = Calendar.getInstance();
                int emYear = mcurrentDate.get(Calendar.YEAR);
                int emMonth = mcurrentDate.get(Calendar.MONTH);
                //Log.d("M", String.valueOf(emMonth));
                int emDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog mDatePicker = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        selectedmonth=selectedmonth+1;
                        endingDate = selectedyear+"-"+selectedmonth+"-"+selectedday;
                        eventEndingDate.setText("" + endingDate);
                    }
                }, emYear, emMonth, emDay);
                //mDatePicker.getDatePicker().findViewById(Resources.getSystem().getI)

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                try {
                    d = sdf.parse(startingDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mDatePicker.getDatePicker().setMinDate(d.getTime());
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });

        submit = (Button) findViewById(R.id.button);
        db = new DatabaseLRHandler(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventName.getText().toString().trim().equals("") || eventCode.getText().toString().trim().equals("") ||
                        eventVenue.getText().toString().trim().equals("") || eventStartingDate.getText().toString().trim().equals("")
                        || eventEndingDate.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Fill all the deteils properly", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.addEvent(eventCode.getText().toString().trim(), eventName.getText().toString().trim(), eventVenue.getText().toString().trim(),
                            eventStartingDate.getText().toString().trim(), eventEndingDate.getText().toString().trim());
                    Intent in = new Intent(getApplicationContext(), EventActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        });
    }

}
