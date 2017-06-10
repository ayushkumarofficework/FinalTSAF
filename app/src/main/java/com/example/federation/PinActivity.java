package com.example.federation;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.federation.Events.EventActivity;
import com.example.federation.Session.SessionManager;

import org.w3c.dom.Text;

public class PinActivity extends AppCompatActivity {

    EditText ePin;
    Button bConfirm;
    Button bLogout;
    SessionManager session;
    TextView user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ePin=(EditText)findViewById(R.id.lpin);
        bConfirm=(Button)findViewById(R.id.btnConfirm);
        session = new SessionManager(getApplicationContext());
        user=(TextView)findViewById(R.id.tUserName) ;
        bLogout=(Button)findViewById(R.id.btnLogout);
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });
        String name=session.checkLogin();
        if(name!=null)
        {
            user.setText(name);
        }
        else
        {
            finish();
        }
        final String pin=session.getPin();
        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"as",Toast.LENGTH_LONG).show();
                if(ePin.getText().toString().equals(pin.toString()))
                {
                    Intent i=new Intent(getApplicationContext(), EventActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
