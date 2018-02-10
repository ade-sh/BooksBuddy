package com.adesh.projbk;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class bk_details extends AppCompatActivity {

    public Getjson getjsonobj;
    ImageView bk_img;
    Button btnBuy;
    RatingBar ratingBar;
    RecyclerView rvAllimg;
    private TextView mTextMessage, et_bkName, et_bkDisk;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_main);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bk_details);
        btnBuy = (Button) findViewById(R.id.btn_buy);
        mTextMessage = (TextView) findViewById(R.id.mtv_view);
        et_bkName = (TextView) findViewById(R.id.et_bkName);
        et_bkDisk = (TextView) findViewById(R.id.tv_deatils);
        bk_img = (ImageView) findViewById(R.id.iv_bkImg);
        ratingBar = (RatingBar) findViewById(R.id.rb_bkRating);
        rvAllimg = (RecyclerView) findViewById(R.id.lvdetailItems);
        Intent intent = getIntent();
        String sPos = intent.getStringExtra("bkPos");
        String upType = intent.getStringExtra("Type");
        et_bkName.setText(sPos);
        et_bkDisk.setText("");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fillData startFill = new fillData(sPos, upType);
        startFill.execute();
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
                Log.i("uptype", upType);
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
            String ustr = Getjson.arrurls.get(0).substring(15);
            Log.i("ustr", ustr);
            if (Getjson.arrUploader.get(0).contains("user") || Getjson.arrUploader.get(0).contains("Request")) {
                ratingBar.setVisibility(View.GONE);
            }

            et_bkName.setText(Getjson.arrname.get(0));
            et_bkDisk.setText(Getjson.arrDisc.get(0));
            ratingBar.setRating(Integer.parseInt(Getjson.arrRatin.get(0)));
            LinearLayoutManager llm = new LinearLayoutManager(bk_details.this, LinearLayoutManager.HORIZONTAL, false);
            Picasso.with(getApplicationContext()).load(("http://10.0.3.2" + ustr)).placeholder(R.mipmap.im_defbk).into(bk_img);
            ArrayList lvUrls = new ArrayList();

            lvUrls.add(Getjson.arrurls.get(0));
            if (Getjson.arrurls2.get(0) != null && Getjson.arrurls2.get(0).length() != 4 && Getjson.arrurls2.get(0).isEmpty() && Getjson.arrurls2.get(0).length() != 0) {
                lvUrls.add(Getjson.arrurls2.get(0));
            }
            if (Getjson.arrurls3.get(0) != null && Getjson.arrurls3.get(0).length() != 4 && Getjson.arrurls3.get(0).isEmpty() && Getjson.arrurls3.get(0).length() != 0) {
                lvUrls.add(Getjson.arrurls3.get(0));
            }
            rvImageView rvAdapter = new rvImageView(bk_details.this, lvUrls);
            rvAllimg.setLayoutManager(llm);
            rvAllimg.setAdapter(rvAdapter);

        }
    }
}


