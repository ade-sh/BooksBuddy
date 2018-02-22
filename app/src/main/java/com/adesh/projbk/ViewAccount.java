package com.adesh.projbk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;

public class ViewAccount extends AppCompatActivity {
    ImageView iv;
    String SPusrname, uid, uploader;
    String arr[];
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);
        iv = (ImageView) findViewById(R.id.ivAccViewImg);
        lv = (ListView) findViewById(R.id.lvaccViewdetail);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        uploader = intent.getStringExtra("uploader");
        startDataFill();
    }

    private void startDataFill() {
        final String arr[] = {SPusrname, "", "", "Password", "Location", "All Products"};
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("U_id", uid);
        params.put("uploader", uploader);
        client.post(ViewAccount.this, getString(R.string.httpUrl) + "/getUserInfo.inc.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ViewAccount.this, "cannot fetch data now", Toast.LENGTH_SHORT).show();
                Toast.makeText(ViewAccount.this, "Try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String arr[] = {"Username: " + Getjson.JUserName, "Phone no: " + Getjson.JPhone, "Emailid: " + Getjson.JEmail, "All Products"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.llaccdetail, arr);
                lv.setAdapter(adapter);
                Picasso.with(ViewAccount.this).load("https://determinately-torqu.000webhostapp.com" + Getjson.JProfilePic.trim()).resize(300, 300).into(iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        iv.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        iv.setImageResource(R.drawable.default_user);
                    }
                });
            }
        });
    }
}
