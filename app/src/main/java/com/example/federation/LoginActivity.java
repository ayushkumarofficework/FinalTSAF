package com.example.federation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.federation.Events.EventActivity;
import com.example.federation.SQLite.DatabaseLRHandler;
import com.example.federation.Session.SessionManager;

public class LoginActivity extends Activity {
    EditText email;
    EditText pin;
    Button login;
    Button goRegister;
    DatabaseLRHandler db;
    ProgressDialog mProgress;
    SessionManager sessionManager;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db=new DatabaseLRHandler(this);
        sessionManager=new SessionManager(getApplicationContext());

        mProgress=new ProgressDialog(this);

        login=(Button)findViewById(R.id.btnLogin);

        email=(EditText)findViewById(R.id.lemail);

        pin=(EditText)findViewById(R.id.lpin);
        goRegister=(Button)findViewById(R.id.btnLinkToRegisterScreen);
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress.setMessage("Logging In :) ");
                mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgress.setIndeterminate(true);
                mProgress.show();
                String p=pin.getText().toString();
                String e=email.getText().toString();
                if(p==" "||e==" ")
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginActivity.this);
                    builder.setTitle("Tata");
                    builder.setMessage("Fill the data properly.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                    return;
                }
                String name=db.getUserAvailability(Integer.valueOf(p),e);
                if(name!=null)
                {
                    sessionManager.createLoginSession(p,e,name);
                    Intent i=new Intent(getApplicationContext(), EventActivity.class);
                    startActivity(i);
                    mProgress.dismiss();
                }
                else
                {

                    mProgress.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginActivity.this);
                    builder.setTitle("Tata");
                    builder.setMessage("No such user exists.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();


                }

            }
        });
    }
}
