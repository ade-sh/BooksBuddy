package com.adesh.projbk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class sellActivity extends AppCompatActivity {
    android.net.Uri filePath;
    EditText etbkName, etbkdisc, etPrice;
    ImageView ivPreview;
    Bitmap bitmap;
    Button getImage, btnsell;

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
        btnsell = (Button) findViewById(R.id.btnSell_upload);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (ContextCompat.checkSelfPermission(sellActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //give explanation
                    ActivityCompat.requestPermissions(sellActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    ActivityCompat.requestPermissions(sellActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
            }
        });
        btnsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Picasso.with(getApplicationContext()).load(filePath).resize(300, 240).into(ivPreview);
                //ivPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        class UploadData extends AsyncTask<Bitmap, Void, String> {
            ProgressDialog loading;
            uploadRequestHandler urc = new uploadRequestHandler();
            String bokDisc, bookPrice;
            String bookNam = etbkName.getText().toString();

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(sellActivity.this, "Please wait....", null, true, true);
                String bookNam = etbkName.getText().toString();
                Log.i("book name String", bookNam);
                bokDisc = etbkdisc.getText().toString();
                bookPrice = etPrice.getText().toString();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put("image", uploadImage);
                data.put("bookName", bookNam);
                data.put("bookDisc", bokDisc);
                data.put("price", bookPrice);

                String result = urc.sendPostRequest(getString(R.string.httpUrl) + "/userSell.php", data);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("Result in sellActivity", s);
            }
        }
        UploadData ud = new UploadData();
        ud.execute(bitmap);
        finish();
    }
}
