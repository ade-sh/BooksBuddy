package com.adesh.projbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AccountActivity extends AppCompatActivity {
    TextView tvuserName, tvEmail, tvphone;
    Button btnLogout;
    Getjson getjsonobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        String SPusrname = sp.getString("UserName", "user");
        tvuserName = (TextView) findViewById(R.id.tv_accUserName);
        tvEmail = (TextView) findViewById(R.id.tv_AccEmail);
        tvphone = (TextView) findViewById(R.id.tv_AccPhone);
        tvuserName.setText(SPusrname);
        btnLogout = (Button) findViewById(R.id.btn_Logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("IsLogged", false);
                editor.putString("Username", "");
                editor.apply();
                Intent InLo = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(InLo);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        startFill();
    }

    public void startFill() {
        class startFilling extends AsyncTask<Void, Void, Void> {
            String username;
            String Password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
                username = sp.getString("UserName", null);
                Password = sp.getString("Password", null);
            }

            @Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection conn;
                URL url;
                try {
                    //setup HttpURLConnection class to send aand receive data from php and mysql
                    url = new URL((R.string.httpUrl) + "/getUserInfo.inc.php");
                    conn = (HttpURLConnection) url.openConnection();
                    Thread.sleep(2000);
                    conn.setConnectTimeout(4000);
                    conn.setRequestMethod("POST");

                    //setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    //Appends parameters to URL
                    Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", username).appendQueryParameter("password", Password);
                    String query = builder.build().getEncodedQuery();

                    //Open Connection for sending data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    bwriter.write(query);
                    bwriter.flush();
                    bwriter.close();
                    os.close();
                    conn.connect();

                    int response_code = conn.getResponseCode();
                    //Check if sucessfull connection made
                    if (response_code == HttpsURLConnection.HTTP_OK) {
                        //Read Data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        Log.i("ustr res", result.toString());
                        getjsonobj = new Getjson(result.toString());
                        getjsonobj.getUser();

                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    e3.getMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                Log.i("Username,phone,Email", Getjson.JUserName + "" + Getjson.JPhone);
                tvuserName.setText(Getjson.JUserName);
                tvphone.setText(Getjson.JPhone);
                tvEmail.setText(Getjson.JEmail);
            }
        }
        startFilling st = new startFilling();
        st.execute();
    }
}
