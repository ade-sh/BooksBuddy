package com.adesh.projbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.laevatein.Laevatein;
import com.laevatein.MimeType;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Request extends AppCompatActivity implements View.OnClickListener {
    EditText et_name, et_disc;
    Button btn_img, btn_request;
    RecyclerView rvreqImg;
    lvImagepreview rvAdapter;
    LinearLayoutManager llm;
    Bitmap bitmap;
    RequestParams params;
    ProgressBar prg;
    String Uid;
    List<Uri> selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        Boolean LoginStatus = sp.getBoolean("IsLogged", false);
        Log.i("LoginStaus", LoginStatus.toString());
        if (!LoginStatus) {
            Toast.makeText(this, "You need to log in", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        Uid = sp.getString("Uid", "");
        getUserId();
        et_name = (EditText) findViewById(R.id.et_rqname);
        et_disc = (EditText) findViewById(R.id.et_rqDisc);
        btn_img = (Button) findViewById(R.id.btn_rqImg);
        btn_request = (Button) findViewById(R.id.btn_Request);
        rvreqImg = (RecyclerView) findViewById(R.id.rvReqestImg);
        btn_img.setOnClickListener(this);
        btn_request.setOnClickListener(this);
        params = new RequestParams();
        prg = (ProgressBar) findViewById(R.id.pbReq);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btn_img.getId()) {
            Laevatein.from(Request.this).choose(MimeType.of(MimeType.JPEG)).capture(true).resume(selected).count(1, 3).forResult(1);
        }
        if (v.getId() == btn_request.getId()) {
            if (isVaild()) {
                AsyncHttpClient client = new AsyncHttpClient();
                params.put("name", et_name.getText().toString());
                params.put("Disc", et_disc.getText().toString());
                params.put("u_id", Uid);
                prg.setVisibility(View.VISIBLE);
                client.post(Request.this, getString(R.string.httpUrl) + "/sendRequest.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(Request.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        prg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        prg.setVisibility(View.GONE);
                        startBkdetil();
                    }
                });
            }
        }
    }

    private void startBkdetil() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("u_id", Uid);
        params.put("type", "request");
        client.post("http://10.0.3.2/getBkid.inc.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Cannot Connect", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Intent intent = new Intent(Request.this, bk_details.class);
                intent.putExtra("bkPos", responseString.trim());
                intent.putExtra("Type", "Request");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selected = Laevatein.obtainResult(data);
            llm = new LinearLayoutManager(Request.this, LinearLayoutManager.HORIZONTAL, false);
            rvAdapter = new lvImagepreview(Request.this, selected);
            rvreqImg.setLayoutManager(llm);
            rvreqImg.setAdapter(rvAdapter);
            for (int i = 0; i < selected.size(); i++) {
                try {
                    int K = 0;
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selected.get(i));
                    String uploadImage = getStringImage(bitmap);
                    K = i + 1;
                    params.put("image" + K, uploadImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            rvAdapter.notifyDataSetChanged();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String encodeImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodeImage;
    }

    public boolean isVaild() {
        String name = et_name.getText().toString();
        String Disc = et_disc.getText().toString();
        if (name.isEmpty()) {
            et_name.setError("Name Required");
            et_name.requestFocus();
            return false;
        }
        if (Disc.isEmpty()) {
            et_disc.setError("Some Discription Required");
            et_disc.requestFocus();
            return false;
        }
        return true;
    }

    public void getUserId() {
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        final String username = sp.getString("UserName", null);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        client.post("http://10.0.3.2/getUserID.inc.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Cannot Connect", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString.contains("false") && !responseString.contains("null")) {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                } else {
                    Uid = responseString.trim();
                }
            }
        });
    }
}
