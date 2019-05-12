package com.example.tyagi.insta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    private EditText UserName,Mail,Password;
    private Button signUp;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        SetUp();
        firebaseAuth=FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String user_email=Mail.getText().toString().trim();
                    String user_pass=Password.getText().toString().trim();
                    progressDialog.setMessage("Logging In");
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this,"Registration Successfull",Toast.LENGTH_SHORT).show();
                                sendMailVerification();
                                Intent intent1=new Intent(Registration.this,SecondActivity.class);
                                startActivity(intent1);
                            }

                            else{
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Registration.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void SetUp(){
        UserName=findViewById(R.id.etName);
        Mail=findViewById(R.id.etMail);
        Password=findViewById(R.id.etPassword);
        signUp=findViewById(R.id.btnRegister);
        userLogin=findViewById(R.id.tvUserLogin);
        progressDialog=new ProgressDialog(this);
    }
    public boolean validate(){
        String u=UserName.getText().toString();
        String m=Mail.getText().toString();
        String p=Password.getText().toString();

        Boolean res=false;
        if(u.equals("") || m.equals("") || p.equals(""))
            Toast.makeText(this,"Enter All Details",Toast.LENGTH_SHORT).show();
        else
            res=true;

        return  res;
    }

    private void sendMailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else {
                        Toast.makeText(Registration.this,"Network Problem!!! Verification mail hasn't been sent",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }


}
