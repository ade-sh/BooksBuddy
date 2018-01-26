package com.adesh.projbk;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogout, btnsavImg, btncngImg;
    Getjson getjsonobj;
    ImageView ivAccImg, ivcmgImg;
    ListView lvaccdetail;
    android.net.Uri filePath;
    Bitmap bitmap;
    ImageButton ivCanceldialog;
    Dialog imgChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        String SPusrname = sp.getString("UserName", "user");
        String arr[] = {SPusrname, "", ""};
        ivAccImg = (ImageView) findViewById(R.id.ivAccImg);
        lvaccdetail = (ListView) findViewById(R.id.lvaccdetail);
        btnsavImg = (Button) findViewById(R.id.btnsavImg);
        btncngImg = (Button) findViewById(R.id.btncngImg);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.llaccdetail, arr);
        lvaccdetail.setAdapter(adapter);
        ivCanceldialog = (ImageButton) findViewById(R.id.ibCancelDialog);
        btnLogout = (Button) findViewById(R.id.btn_Logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("IsLogged", false);
                editor.putString("Username", "");
                editor.apply();
                finish();
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
        ivAccImg.setOnClickListener(this);

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
                    url = new URL(getString(R.string.httpUrl) + "/getUserInfo.inc.php");
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
                String arr[] = {"Username: " + Getjson.JUserName, "Phone no: " + Getjson.JPhone, "Emailid: " + Getjson.JEmail};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.llaccdetail, arr);
                lvaccdetail.setAdapter(adapter);
            }
        }
        startFilling st = new startFilling();
        st.execute();
    }

    @Override
    public void onClick(View v) {
        imgChanger = new Dialog(AccountActivity.this, android.R.style.Theme_Black_NoTitleBar);
        if (v.getId() == R.id.ivAccImg) {
            imgChanger.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
            imgChanger.setContentView(R.layout.cngaccimg);
            imgChanger.setCancelable(true);
            imgChanger.show();

        }
        if (v.getId() == R.id.btncngImg) {
            imgChanger.dismiss();
            Intent intent = new Intent();
            intent.setType("image/");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (ContextCompat.checkSelfPermission(AccountActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //give explanation
                ActivityCompat.requestPermissions(AccountActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                ActivityCompat.requestPermissions(AccountActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
        }
        if (v.getId() == R.id.ibCancelDialog) {
            imgChanger.dismiss();
            Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgChanger.dismiss();
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Picasso.with(getApplicationContext()).load(filePath).resize(300, 240).into(ivAccImg);
                btnsavImg.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

