package com.project.focustime;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class connectServer extends AsyncTask<String, String, Connection> {

    @Override
    public Connection doInBackground(String... args) {

        try {
            return connect();
        } catch (Exception e) {
            Log.e("Error", e.toString());
            return null;
        }

    }

    public Connection connect() {

        Log.e("Android", "SQL Connection start");

        String ip = "167.71.59.193";
        String dbName = "admin_focus";
        String user = "admin_focus";
        String password = "Z67_d1bz";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            String connString = "jdbc:mysql://" + ip + "/" + dbName;

            conn = DriverManager.getConnection(connString, user, password);
            Log.e("Connection", "Open");
            return conn;

        } catch (Exception e) {
            Log.e("Error connection", e.toString());
            return null;
        }

    }
}

