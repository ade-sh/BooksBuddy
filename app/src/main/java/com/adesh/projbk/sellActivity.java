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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class sellActivity extends AppCompatActivity {
    android.net.Uri filePath;
    EditText etbkName, etbkdisc, etPrice;
    Bitmap bitmap;
    Button getImage, btnsell;
    String result = "";
    lvImagepreview rvAdapter;
    RecyclerView lvTest;
    LinearLayoutManager llm;
    List<Uri> selected;
    RequestParams params;
    ProgressBar pd;
    HashMap<String, String> datas = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        Boolean LoginStatus = sp.getBoolean("IsLogged", false);
        Log.i("LoginStaus", LoginStatus.toString());
        if (!LoginStatus) {
            Toast.makeText(sellActivity.this, "You need to login first", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(sellActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        etbkName = (EditText) findViewById(R.id.etSell_bkname);
        etbkdisc = (EditText) findViewById(R.id.etSell_bkDisc);
        etPrice = (EditText) findViewById(R.id.etSell_price);
        getImage = (Button) findViewById(R.id.btn_getImage);
        pd = (ProgressBar) findViewById(R.id.pdsell);
        params = new RequestParams();
        btnsell = (Button) findViewById(R.id.btnSell_upload);
        bitmap = Bitmap.createBitmap(340, 240, Bitmap.Config.RGB_565);
        lvTest = (RecyclerView) findViewById(R.id.lvItems);
        selected = new ArrayList<Uri>();
        llm = new LinearLayoutManager(sellActivity.this, LinearLayoutManager.VERTICAL, false);

        getUserId();
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Laevatein.from(sellActivity.this).choose(MimeType.of(MimeType.JPEG)).resume(selected).count(1, 3).forResult(1);
            }
        });
        btnsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!result.contains("") || !result.contains("null") || !result.contains("0")) {
                uploadData();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred,Try again", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "result=" + result, Toast.LENGTH_SHORT).show();
                }
                /*if (selected.isEmpty()) {
                    getImage.setError("Image needed");
                    Toast.makeText(getApplicationContext(), "At least 1 image needed" + selected.size(), Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            List<Uri> selected = Laevatein.obtainResult(data);
            llm = new LinearLayoutManager(sellActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rvAdapter = new lvImagepreview(sellActivity.this, selected);
            lvTest.setLayoutManager(llm);
            lvTest.setAdapter(rvAdapter);
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

    public void uploadData() {
        pd.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String bokDisc, bookPrice;
        String bookNam = etbkName.getText().toString();
        bokDisc = etbkdisc.getText().toString();
        bookPrice = etPrice.getText().toString();
        params.put("bookName", bookNam);
        params.put("bookDisc", bokDisc);
        params.put("price", bookPrice);
        params.put("U_id", result);
        client.post(getString(R.string.httpUrl) + "/userSell.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(sellActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                pd.setVisibility(View.GONE);
                if (responseString.contains("true")) {
                    Toast.makeText(getApplicationContext(), "Uploaded Sucessfully", Toast.LENGTH_SHORT).show();
                    strBkDetail();
                }
            }
        });

    }

    public void strBkDetail() {
        pd.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("u_id", result);
        params.put("type", "sell");
        client.post(getString(R.string.httpUrl) + "/getBkid.inc.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Cannot Connect", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Intent intent = new Intent(sellActivity.this, bk_details.class);
                intent.putExtra("bkPos", responseString.trim());
                intent.putExtra("Type", "sell");
                Log.e("resoString", responseString);
                pd.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });
    }
    public void getUserId() {

        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        final String username = sp.getString("UserName", null);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        client.post(getString(R.string.httpUrl) + "/getUserID.inc.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Cannot Connect try again" + responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString.contains("false") && !result.contains("null")) {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                } else {
                    result = responseString.trim();
                    SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Uid", responseString.trim());
                    editor.apply();
                    Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}