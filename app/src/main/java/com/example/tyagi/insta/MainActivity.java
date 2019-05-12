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

public class MainActivity extends AppCompatActivity {


    private EditText name,pass;
    private Button logIn;
    private TextView info,SignUp,forgotPass;
    private  int count=5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String forgotMail="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.et1);
        pass=findViewById(R.id.et2);
        logIn=findViewById(R.id.btnLogin);
        info=findViewById(R.id.tvinfo);
        SignUp=findViewById(R.id.tvReg);
        forgotPass=findViewById(R.id.tvforgotPass);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();



        if(user!=null){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotMail=name.getText().toString().trim();
                if(forgotMail.equals("")){
                    Toast.makeText(MainActivity.this,"Please Enter Your Email and the last remembered Password",Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(forgotMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(MainActivity.this,"A password reset link is sent to your E-Mail",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotMail=name.getText().toString().trim();
                if(name.getText().toString().equals("") || pass.getText().toString().equals(""))
                    Toast.makeText(MainActivity.this,"Enter All Details",Toast.LENGTH_SHORT).show();
                else
                    validate(name.getText().toString(),pass.getText().toString());
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,Registration.class);
                startActivity(intent1);
            }
        });


    }

    private void validate(String userName,String Password)
    {
        if(userName.equals("") || Password.equals("")){

        }
        progressDialog.setMessage("Logging In");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailVerification();
                }
                else{
                    progressDialog.dismiss();
                    count--;
                    Toast.makeText(MainActivity.this,"Login Failed.",Toast.LENGTH_SHORT).show();
                    info.setText("Number of attempts: "+count);
                    if(count==0)
                        logIn.setEnabled(false);
                }
            }
        });
    }


    private void checkEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        Boolean emailFlag=firebaseUser.isEmailVerified();
        if(emailFlag){
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }
        else{
            Toast.makeText(this,"You need to verify your E-mail first",Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }

    }
}
