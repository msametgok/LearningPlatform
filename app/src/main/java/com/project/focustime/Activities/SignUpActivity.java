package com.project.focustime.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.project.focustime.DBOperations;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.project.focustime.Models.User;
import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;
import com.project.focustime.connectServer;
import com.project.focustime.Utils.Md5;

public class SignUpActivity extends AppCompatActivity {

    EditText emailAddressForSignup, passwordForSignup, nameForSignup ;
    TextView signUpTitle;
    Button signUpButton;
    ArrayList<User> myuser;
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+com";

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        initProgressBar();
    }
    private void init() {
        emailAddressForSignup = findViewById(R.id.emailAddressForSignUp);
        nameForSignup = findViewById(R.id.nameForSignUp);
        passwordForSignup = findViewById(R.id.passwordForSignUp);
        signUpTitle = findViewById(R.id.textForSignUp);
        signUpButton = findViewById(R.id.createAccount);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpFT();
            }
        });
    }
    private void initProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }
    public void signUpFT(){

        String name = nameForSignup.getText().toString();
        String mail = emailAddressForSignup.getText().toString();
        String password = passwordForSignup.getText().toString();
        byte[] md5Input = password.getBytes();

        BigInteger md5Data = null;

        try{
            md5Data = new BigInteger(1,Md5.encryptMD5(md5Input));
        }catch (Exception e){
            e.printStackTrace();
        }

        String passwordMD5 = md5Data.toString(16);

        boolean checkUser;

        if(mail.trim().matches(emailPattern)){
            checkUser = db.checkUser(conn,mail);
            if(checkUser==true){
                Toast.makeText(getApplicationContext(),"This Email Address is Used",Toast.LENGTH_SHORT).show();
            }
            else{
                db.signUp(conn,name,passwordMD5,mail);
                // Buraya daha sonra bir toast mesajı atılabilir. Activation gönderildi diye.

                myuser = db.signIn(conn,mail,passwordMD5);

                try {
                    if(!myuser.equals(null)){
                        saveUserDataOnSharedPreference(myuser);
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }catch (Exception e){

                    Log.e("HATA",e.toString());
                    Toast.makeText(getApplicationContext(),"Wrong User or Password",Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(),"Invalid Email Adress",Toast.LENGTH_SHORT).show();
        }

    }

    private void saveUserDataOnSharedPreference(ArrayList<User> myuser) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Helpers.SHARED_PREF, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId",myuser.get(0).getUserId());
        editor.putString("userName", myuser.get(0).getName());
        editor.putString("userEmail", myuser.get(0).getEmail());
        editor.putString("userPassword", myuser.get(0).getPassword());
        editor.putInt("userValidity",myuser.get(0).getValidity());
        editor.putString("userType",myuser.get(0).getType());
        editor.putString("userStatus",myuser.get(0).getStatus());
        editor.putString("userPhoto",myuser.get(0).getUser_photo());
        editor.putString("userBio",myuser.get(0).getDescription());
        editor.putString("userWebsite",myuser.get(0).getWebsite());
        editor.putString("userGithub",myuser.get(0).getGithub());
        editor.putString("userLinkedIn",myuser.get(0).getLinkedin());
        editor.commit();
    }


}
