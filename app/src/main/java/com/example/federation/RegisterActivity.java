package com.example.federation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.federation.SQLite.DatabaseLRHandler;

import java.util.regex.Pattern;

public class RegisterActivity extends Activity {

    EditText name;
    EditText email;
    EditText pin;
    EditText cpin;
    Button register;
    Button goLogin;

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    DatabaseLRHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db= new DatabaseLRHandler(this);

        name=(EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        pin=(EditText)findViewById(R.id.rpin);
        cpin=(EditText)findViewById(R.id.confirm_rpin);
        register=(Button)findViewById(R.id.btnRegister);
        goLogin=(Button)findViewById(R.id.btnLinkToLoginScreen);

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nam=name.getText().toString();
                String p=pin.getText().toString();
                String cp=cpin.getText().toString();
                String ema=email.getText().toString();

                if(nam.equals("")||p.equals("")||ema.equals("")||cp.equals(""))
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            RegisterActivity.this);
                    builder.setTitle("Tata");
                    builder.setMessage("Fields are vacant.");
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
                if(!p.equals(cp))
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            RegisterActivity.this);
                    builder.setTitle("Tata");
                    builder.setMessage("Pins didn't match");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    //Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.show();

                }
                else if(!isValidEmail(email.getText().toString()))
                {


                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            RegisterActivity.this);
                    builder.setTitle("Tata");
                    builder.setMessage("Please enter valid Email Address");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    //Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.show();

                }
                else
                {
                    db.addInstructorRegister(nam,Integer.valueOf(p),ema);


                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            RegisterActivity.this);
                    builder.setTitle("Tata");
                    builder.setMessage("You have been succesfully registered.Go to Login page");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(i);
                                    //Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.show();

                }
            }
        });

    }
    public final static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
