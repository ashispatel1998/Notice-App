package com.example.fireauthdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    Button logout,btn_save,btn_show;
    FirebaseAuth mauth;
    EditText txt_name,txt_des;
    User user;

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        logout=(Button)findViewById(R.id.btn_logout);
        txt_name=(EditText)findViewById(R.id.txt_name);
        txt_des=(EditText)findViewById(R.id.txt_des);
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_show=(Button)findViewById(R.id.btn_show);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(getApplicationContext(),ShowData.class);
                startActivity(i);
            }
        });


        //Object of user class

        user=new User();

        //Refer to the database

        reference= FirebaseDatabase.getInstance().getReference().child("User");

        //On clicking the save button

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=txt_name.getText().toString().trim();
                String des=txt_des.getText().toString().trim();

                if(name.isEmpty()){
                    txt_name.setError("Required");
                    txt_name.requestFocus();
                    return;
                }
                if(des.isEmpty()){
                    txt_des.setError("Required");
                    txt_des.requestFocus();
                    return;
                }


                user.setName(name);
                user.setDes(des);

                reference.push().setValue(user);

                Toast.makeText(getApplicationContext(),"Post successful!",Toast.LENGTH_SHORT).show();

                txt_name.setText("");
                txt_des.setText("");
            }
        });


        //Logout from the profile activity.
        mauth=FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }

}
