package com.project.focustime.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.focustime.DBOperations;
import com.project.focustime.Utils.Helpers;
import com.project.focustime.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.textfield.TextInputEditText;
import com.project.focustime.Utils.Md5;
import com.project.focustime.connectServer;

import java.math.BigInteger;
import java.sql.Connection;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText currentPassword, newPassword, confirmPassword;
    Button updateButton;
    private ProgressBar progressBar;
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        init();
        initProgressBar();
    }

    private void init() {
        currentPassword = findViewById(R.id.currentPasswordEditText);
        newPassword = findViewById(R.id.newPasswordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);
        updateButton = findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoggedIn()){
                    updatePassword();
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "Log In First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePassword() {
        SharedPreferences preferences = this.getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userID = preferences.getInt("userId", 0);
        String password = preferences.getString("userPassword","");

        String currentpass =  currentPassword.getText().toString();
        String newpass = newPassword.getText().toString();
        String confirmpass = confirmPassword.getText().toString();

        byte[] md5Input = currentpass.getBytes();
        byte[] md5Input2 = newpass.getBytes();

        BigInteger md5Data = null;
        BigInteger md5Data2 = null;

        try{
            md5Data = new BigInteger(1, Md5.encryptMD5(md5Input));

            md5Data2 = new BigInteger(1,Md5.encryptMD5(md5Input2));


        }catch (Exception e){
            e.printStackTrace();
        }

        String passwordMD5 = md5Data.toString(16);
        String newPassMD5 = md5Data2.toString(16);

        try{
            if(passwordMD5.equals(password)){
                if(newpass.equals(confirmpass)){


                    db.changePass(conn,userID,newPassMD5);
                    currentPassword.getText().clear();
                    newPassword.getText().clear();
                    confirmPassword.getText().clear();
                    SharedPreferences.Editor prefsEditor = preferences.edit();
                    prefsEditor.putString("userPassword", newPassMD5);
                    prefsEditor.commit();
                    Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();

                    System.out.println("Yeni Şifre");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Wrong Current Password",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }

    }

    private void initProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }


    private boolean isLoggedIn() {
        SharedPreferences preferences = this.getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userValidity = preferences.getInt("userValidity", 0);
        if (userValidity == 1) {
            return true;
        }else{
            return false;
        }
    }

    public void handleBackButton(View view) {
        ChangePasswordActivity.super.onBackPressed();
    }
}
