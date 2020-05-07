package com.example.fireauthdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText input_email, input_password;
    Button btn_login;
    TextView create_ac;
    private long backPressedTime = 0;


    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        create_ac = (TextView) findViewById(R.id.link_signup);


        mauth = FirebaseAuth.getInstance();

        create_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp_Activity.class);
                startActivity(i);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mauth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Profile.class));
        }
    }

    private void userLogin() {
        String eml = input_email.getText().toString().trim();
        String ps = input_password.getText().toString().trim();
        if (eml.isEmpty()) {
            input_email.setError("Required");
            input_email.requestFocus();
            return;
        }
        if (ps.isEmpty()) {
            input_password.setError("Required");
            input_password.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(eml).matches()) {
            input_email.setError("Invalid email !");
            input_email.requestFocus();
            return;
        }
        if (ps.length() < 6) {
            input_password.setError("more than 6 char!");
            input_password.requestFocus();
            return;
        }
        mauth.signInWithEmailAndPassword(eml, ps).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                   if(mauth.getCurrentUser().isEmailVerified()){
                        Intent i = new Intent(getApplicationContext(), Profile.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                   else{
                       Toast.makeText(getApplicationContext(), "Please verify your mail!", Toast.LENGTH_SHORT).show();
                   }
                }
                else {
                    Toast.makeText(getApplicationContext(), "You are not a valid user!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
   //On back pressed it will close the activity .
    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Press back again to Exit",
                    Toast.LENGTH_SHORT).show();
        } else {
            // clean up
            super.onBackPressed();       // bye
        }
    }
    }

