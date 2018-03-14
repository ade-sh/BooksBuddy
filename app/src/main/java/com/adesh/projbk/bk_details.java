package com.adesh.projbk;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
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

import cz.msebera.android.httpclient.Header;

public class bk_details extends AppCompatActivity {
    public Getjson getjsonobj;
    int load = 0;
    Fragment bkfragment;
    ImageView bk_img;
    ProgressBar pb;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            pb.setVisibility(View.GONE);
            Fragment frag1 = bkfragment;
            Fragment frag2 = new ReviewsFragment();
            Fragment frag3 = new bkUserFragment();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            if (!frag1.isAdded() && !frag3.isAdded()) {
                ft.add(R.id.fragPlace, frag3).commit();
            }
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft.show(frag1);
                    ft.hide(frag2).hide(frag3).commit();
                    return true;
                case R.id.navigation_reviews:
                    if (Getjson.arrUploader.get(0) != null && Getjson.arrUploader.get(0).trim().contains("publisher")) {
                        if (Getjson.arrRvHead != null && !Getjson.arrRvHead.isEmpty()) {
                            ft.show(frag2);
                            ft.hide(frag1).hide(frag3).commit();
                        } else {
                            Toast.makeText(bk_details.this, "Please wait... fetching Reviews", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                case R.id.navigation_about:
                    pb.setVisibility(View.GONE);
                    ft.show(frag3);
                    ft.hide(frag1).hide(frag2).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settingPreference = PreferenceManager.getDefaultSharedPreferences(bk_details.this);
        boolean isDark = settingPreference.getBoolean("switch_preference_theme", false);
        if (isDark) {
            setTheme(R.style.Base_ThemeOverlay_AppCompat_Dark_);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bk_details);
        bk_img = (ImageView)findViewById(R.id.iv_bkImg);
        pb = (ProgressBar) findViewById(R.id.pb_bkdetail);
        Intent intent = getIntent();
        String sPos = intent.getStringExtra("bkPos");
        String upType = intent.getStringExtra("Type");
        //send data to fragment
        Bundle data = new Bundle();
        data.putString("bkPos",sPos);
        data.putString("Type",upType);
        bkfragment=new bkdeatilFragment();
        bkfragment.setArguments(data);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fillData startFill = new fillData(sPos, upType);
        startFill.execute();
    }

    public void getUserInfo() {
        class startFilling extends AsyncTask<Void, Void, Void> {
            String uid = Getjson.arruid.get(0).trim();

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
                    Uri.Builder builder = new Uri.Builder().appendQueryParameter("U_id", Getjson.arruid.get(0)).appendQueryParameter("uploader", Getjson.arrUploader.get(0).trim());
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
                        Getjson getjsonobjusr = new Getjson(result.toString());
                        getjsonobjusr.clrUser();
                        getjsonobjusr.getUser();

                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    e3.getMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        startFilling st = new startFilling();
        st.execute();
    }

    private void getReviews() {
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        String uid = sp.getString("Uid", null);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("bkid", Getjson.arrid.get(0).trim());
        params.add("uid", uid);
        client.post(getString(R.string.httpUrl) + "/getReviews.inc.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Toast.makeText(bk_details.this, "some error occured Cannot Fetch Reviews", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Getjson getjsonobjRev = new Getjson(response);
                getjsonobjRev.getReviews();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment frag2 = new ReviewsFragment();
                ft.add(R.id.fragPlace, frag2);
                ft.commit();
            }
        });
    }

    public void onStop() {
        super.onStop();
        getjsonobj.clrRev();
    }

    public class fillData extends AsyncTask<Object, Object, String> {
        String Img_id;
        String upType;

        fillData(String Img_id, String upType) {
            this.Img_id = Img_id.trim();
            this.upType = upType.trim();
        }

        @Override
        protected void onPreExecute() {
            Log.i("Img_Id", Img_id);
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Object... params) {
            HttpURLConnection conn;
            URL url;
            try {
                //setup HttpURLConnection class to send aand receive data from php and mysql
                url = new URL(getString(R.string.httpUrl) + "/getBkdetail.inc.php");
                conn = (HttpURLConnection) url.openConnection();
                Thread.sleep(2000);
                conn.setConnectTimeout(4000);
                conn.setRequestMethod("POST");

                //setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //Appends parameters to URL
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("bid", Img_id).appendQueryParameter("Uploader", upType);
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
                    //pass data to postExecute method
                    Log.i("result bkdetail", result.toString());
                    getjsonobj = new Getjson(result.toString());
                    getjsonobj.getAllImages();
                    return (result.toString());
                } else {
                    return ("unsucessful");
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                return ("Exception" + e3.getMessage());
            } catch (InterruptedException e) {
                return ("interrupted");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String ustr = Getjson.arrurls.get(0);
            Picasso.with(bk_details.this).load((getString(R.string.httpUrl) + ustr)).placeholder(R.drawable.defaultloading).into(bk_img);
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.add(R.id.fragPlace, bkfragment);
            Fragment frag3 = new bkUserFragment();
            if (bkfragment.isAdded()) {
                ft.add(R.id.fragPlace, frag3).commit();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getUserInfo();
                    if (Getjson.arrUploader.get(0).trim().contains("publisher")) {
                        getReviews();
                    }
                }
            });
        }
    }
}