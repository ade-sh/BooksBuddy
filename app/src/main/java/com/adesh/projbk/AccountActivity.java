package com.adesh.projbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button btnLogout, btnsavImg, btncngImg;
    Getjson getjsonobj;
    ImageView ivAccImg, ivcmgImg;
    ListView lvaccdetail;
    android.net.Uri filePath;
    String res;
    Bitmap bitmap;
    String username;
    String Password;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        String SPusrname = sp.getString("UserName", "user");
        String arr[] = {SPusrname, "", "", "Password", "Location", "My Orders", "Request", "Sold", "All Transactions"};
        getUserId();
        ivAccImg = (ImageView) findViewById(R.id.ivAccImg);
        lvaccdetail = (ListView) findViewById(R.id.lvaccdetail);
        btnsavImg = (Button) findViewById(R.id.btnsavImg);
        btncngImg = (Button) findViewById(R.id.btncngImg);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.llaccdetail, arr);
        lvaccdetail.setAdapter(adapter);
        lvaccdetail.setOnItemClickListener(this);
        btnLogout = (Button) findViewById(R.id.btn_Logout);
        btnsavImg.setOnClickListener(this);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("IsLogged", false);
                editor.putString("Username", "");
                editor.apply();
                CartDatabaseHelper db = new CartDatabaseHelper(AccountActivity.this);
                db.Deleteall();
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

    //Set User Data
    public void startFill() {
        class startFilling extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (username == null) {
                SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
                username = sp.getString("UserName", null);
                Password = sp.getString("Password", null);
                    uid = sp.getString("Uid", null);
                    Toast.makeText(AccountActivity.this, uid, Toast.LENGTH_SHORT).show();
                }
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
                    Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", username);
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
                SharedPreferences sp = getSharedPreferences("Userdetail", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ProfilePic", Getjson.JProfilePic);
                editor.putString("Profilename", Getjson.JUserName);
                editor.putString("Email", Getjson.JEmail);
                editor.apply();
                String arr[] = {"Username: " + Getjson.JUserName, "Phone no: " + Getjson.JPhone, "Emailid: " + Getjson.JEmail, "Password", "Location", "My Orders", "Request", "Sold", "All Transactions"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.llaccdetail, arr);
                lvaccdetail.setAdapter(adapter);
                Picasso.with(AccountActivity.this).load(getString(R.string.httpUrl) + Getjson.JProfilePic.trim()).resize(300, 300).into(ivAccImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) ivAccImg.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        ivAccImg.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        ivAccImg.setImageResource(R.drawable.default_user);
                    }
                });
            }
        }
        startFilling st = new startFilling();
        st.execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivAccImg) {
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
        if (v.getId() == R.id.btnsavImg) {
            UploadnewProf();
        }
    }

    //send Image to server
    private void UploadnewProf() {
        String uploadImage = getStringImage(bitmap);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("u_id", res);
        params.put("profile", uploadImage);
        client.post(AccountActivity.this, getString(R.string.httpUrl) + "/updateImg.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(AccountActivity.this, "Failed to upload Image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(AccountActivity.this, "Changed Profile", Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("profPic", Getjson.JProfilePic);
                editor.apply();
            }
        });
    }


    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String encodeImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodeImage;
    }

    //getting Image from Storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Picasso.with(getApplicationContext()).load(filePath).resize(300, 300).into(ivAccImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) ivAccImg.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        ivAccImg.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        ivAccImg.setImageResource(R.drawable.ic_home_black_24dp);
                    }
                });
                btnsavImg.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //get userId
    public void getUserId() {
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        final String username = sp.getString("UserName", null);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        client.post(getString(R.string.httpUrl) + "/getUserID.inc.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Cannot Connect", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString.contains("false") && !res.contains("null")) {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                } else {
                    res = responseString.trim();
                    SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Uid", res.trim());
                    editor.apply();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (res == null) {
            Toast.makeText(AccountActivity.this, "Please wait,Loading items", Toast.LENGTH_SHORT).show();
            return;
        }
        if (position == 0) {
            Toast.makeText(getApplicationContext(), "Change name not available now", Toast.LENGTH_SHORT).show();
        } else if (position == 1) {
            Toast.makeText(getApplicationContext(), "Change Phone no not available now", Toast.LENGTH_SHORT).show();
        } else if (position == 2) {
            Toast.makeText(getApplicationContext(), "Email Change  not available now", Toast.LENGTH_SHORT).show();
        } else if (position == 3) {
            Toast.makeText(getApplicationContext(), "Password Change  not available now", Toast.LENGTH_SHORT).show();
        } else if (position == 4) {
            Toast.makeText(getApplicationContext(), "Location  not available now", Toast.LENGTH_SHORT).show();
        } else if (position == 5) {
            Intent intent = new Intent(AccountActivity.this, TransactionView.class);
            intent.putExtra("uid", res);
            intent.putExtra("transType", "order");
            startActivity(intent);
        } else if (position == 6) {
            Intent intent = new Intent(AccountActivity.this, TransactionView.class);
            intent.putExtra("uid", res);
            intent.putExtra("transType", "request");
            startActivity(intent);
        } else if (position == 8) {
            Intent intent = new Intent(AccountActivity.this, TransactionView.class);
            intent.putExtra("uid", res);
            intent.putExtra("transType", "All");
            startActivity(intent);
        } else if (position == 7) {
            Intent intent = new Intent(AccountActivity.this, TransactionView.class);
            intent.putExtra("uid", res);
            intent.putExtra("transType", "sold");
            startActivity(intent);
        }
    }
}

