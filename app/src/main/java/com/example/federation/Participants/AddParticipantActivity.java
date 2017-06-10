package com.example.federation.Participants;

import android.app.DatePickerDialog;
import android.content.Intent;
import java.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.federation.Events.CreateEvent;
import com.example.federation.Events.EventInformationActivity;
import com.example.federation.R;
import com.example.federation.SQLite.DatabaseLRHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddParticipantActivity extends AppCompatActivity {
    Spinner sGender;

    CheckBox fe;
    boolean bfe=false;
    CheckBox ne;
    boolean bne=false;
    CheckBox agree;
    boolean ba=false;
EditText eWeight;
    String gender;
    DatabaseLRHandler db;
    EditText name,email,phone,dateOfBirth,postalAddress,nameOfOrganization;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant);
        db=new DatabaseLRHandler(this);
        name=(EditText)findViewById(R.id.participantName);
        email=(EditText)findViewById(R.id.participantEmail);
        phone=(EditText)findViewById(R.id.phoneNumber);
        submit=(Button)findViewById(R.id.registerButton);
        dateOfBirth=(EditText)findViewById(R.id.dateOfBirth);
        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int smYear = mcurrentDate.get(Calendar.YEAR);
                    int smMonth = mcurrentDate.get(Calendar.MONTH);
                    int smDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker = new DatePickerDialog(AddParticipantActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                            String  dob= selectedday + "/" + selectedmonth + "/" + selectedyear;
                            dateOfBirth.setText("" + dob);
                        }
                    }, smYear, smMonth, smDay);
                    mDatePicker.setTitle("Select date");
                    mDatePicker.show();
                }
            }
        });
        sGender=(Spinner)findViewById(R.id.spinner);
        eWeight=(EditText)findViewById(R.id.weight);

        fe=(CheckBox)findViewById(R.id.cFTEntrant);
        fe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    bfe=true;
                }

            }
        });

        ne=(CheckBox)findViewById(R.id.cNEntrant);
        ne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    bne=true;
                }
            }
        });

        agree=(CheckBox)findViewById(R.id.checkBoxTermsAndCondition);
        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    ba=true;
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sGender.setAdapter(adapter);
        sGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender= (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        postalAddress=(EditText)findViewById(R.id.address);
        nameOfOrganization=(EditText)findViewById(R.id.nameOfOrganization);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().trim().equals("")||email.getText().toString().trim().equals("")||phone.getText().toString().equals("")||eWeight.getText().toString().equals("")||dateOfBirth.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Fill all details properly",Toast.LENGTH_SHORT).show();
                }
                else if(!(isValidEmail(email.getText().toString())))
                {
                    Toast.makeText(getApplicationContext(),"Invalid Email Address",Toast.LENGTH_SHORT).show();
                }
                else if (ba&&name.getText().toString().length()>=4){
                    JSONArray jsonArray=new JSONArray();
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("Name",name.getText().toString());
                        jsonObject.put("Email",email.getText().toString());
                        jsonObject.put("MobileNumber",phone.getText().toString());
                        jsonObject.put("Date of Birth",dateOfBirth.getText().toString().trim());
                        jsonObject.put("Postal Address",postalAddress.getText().toString().trim());
                        jsonObject.put("Name of Organization",nameOfOrganization.getText().toString().trim());
                        jsonObject.put("FirstEntrant",bfe);
                        jsonObject.put("NewEntrant",bne);
                        jsonObject.put("Gender",gender);
                        jsonObject.put("Weight",eWeight.getText().toString().trim());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    db.addParticipant(name.getText().toString().trim(),email.getText().toString().trim(),phone.getText().toString().trim(),getIntent().getIntExtra("eventId",0),jsonArray.toString());
                    Intent in=new Intent(getApplicationContext(), EventInformationActivity.class);
                    in.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                    in.putExtra("eventName",getIntent().getStringExtra("eventName"));
                    in.putExtra("eventId",getIntent().getIntExtra("eventId",0));

                    startActivity(in);
                }
            }
        });
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
