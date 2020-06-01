package com.project.focustime.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.focustime.Models.User;
import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.project.focustime.Utils.Md5;
import com.project.focustime.connectServer;
import com.project.focustime.DBOperations;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;


public class SignInActivity extends AppCompatActivity {

    EditText emailAddressForLogin, passwordForLogin;
    ImageView applicationLogo;
    TextView loginTitle;

    private ProgressBar progressBar;
    ArrayList<User> myuser;
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();

    private static final String TAG = "SignInActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        init();
        initProgressBar();
    }

    private void init() {
        emailAddressForLogin = findViewById(R.id.emailAddressForLogin);
        passwordForLogin = findViewById(R.id.passwordForLogin);
        applicationLogo = findViewById(R.id.applicationLogo);
        loginTitle = findViewById(R.id.textForLogin);
    }

    private void initProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }


    public void signUp(View view) {
        Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(intent);
        SignInActivity.this.finish();
    }

    public void logIn(View view) {

        String email = emailAddressForLogin.getText().toString();
        String password = passwordForLogin.getText().toString();

        byte[] md5Input = password.getBytes();

        BigInteger md5Data = null;

        try{
            md5Data = new BigInteger(1, Md5.encryptMD5(md5Input));
        }catch (Exception e){
            e.printStackTrace();
        }

        String passwordMD5 = md5Data.toString(16);
        myuser = db.signIn(conn,email,passwordMD5);

        try {
            if(!myuser.equals(null)){
                saveUserDataOnSharedPreference(myuser);
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }

        }catch (Exception e){

            Log.e("HATA",e.toString());
            Toast.makeText(getApplicationContext(),"Wrong User or Password",Toast.LENGTH_SHORT).show();
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
