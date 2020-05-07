package com.example.fireauthdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp_Activity extends AppCompatActivity {
    EditText email,password,confirmpassword;
    Button btn_signup;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        confirmpassword=(EditText)findViewById(R.id.confirm);
        btn_signup=(Button)findViewById(R.id.btn_signup);

        mauth=FirebaseAuth.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }
    public void registerUser(){

        String eml=email.getText().toString().trim();
        String ps=password.getText().toString().trim();
        String cps=confirmpassword.getText().toString().trim();

        if(eml.isEmpty()){
            email.setError("Required");
            email.requestFocus();
            return;
        }
        if(ps.isEmpty()){
            password.setError("Required");
            password.requestFocus();
            return;
        }
        if(cps.isEmpty()){
            confirmpassword.setError("Required");
            confirmpassword.requestFocus();
            return;
        }
        if(ps.length()<6 && cps.length()<6){
            password.setError("more than 6 char!");
            password.requestFocus();
            return;
        }
        if(!(ps.equals(cps))){
            password.setError("password must be same");
            password.requestFocus();
            confirmpassword.setError("password must be same");
            confirmpassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(eml).matches()){
            email.setError("Invalid email !");
            email.requestFocus();
            return;
        }
        ////adding the user to firebase auth
        mauth.createUserWithEmailAndPassword(eml,ps).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {


                    mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                finish();
                                Toast.makeText(getApplicationContext(),"Please check your mail !",Toast.LENGTH_SHORT).show();
                                email.setText("");
                                password.setText("");
                                confirmpassword.setText("");
                                mauth.signOut();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));


                            }else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}
