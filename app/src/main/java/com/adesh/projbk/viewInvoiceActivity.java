package com.adesh.projbk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class viewInvoiceActivity extends AppCompatActivity {
    WebView wvshowpdf;
    Button save;
    String pdfPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        save = (Button) findViewById(R.id.btn_saveInvoice);
        wvshowpdf = (WebView) findViewById(R.id.wv_invoice);
        wvshowpdf.getSettings().setLoadWithOverviewMode(true);
        wvshowpdf.getSettings().setUseWideViewPort(true);
        wvshowpdf.getSettings().setJavaScriptEnabled(true);
        wvshowpdf.setWebViewClient(new WebViewClient());
        Intent intent = getIntent();
        String bill = intent.getStringExtra("billid");
        RequestBody parameter = new FormBody.Builder().add("billid", bill).build();
        Log.i("bill2", bill);
        okhttp3.Request request = new okhttp3.Request.Builder().url(getString(R.string.httpUrl) + "/getInvoice.inc.php").post(parameter).build();
        Response response = null;
        try {
            OkHttpClient httpClient = new OkHttpClient();
            response = httpClient.newCall(request).execute();
            pdfPath = response.body().string().trim();
            wvshowpdf.loadUrl("http://docs.google.com/gview?embedded=true&url=" + getString(R.string.httpUrl) + "/" + pdfPath);
            Log.i("pdfURl", pdfPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                try {
                    OkHttpClient httpClient = new OkHttpClient();
                    File file = new File(getCacheDir(), pdfPath);
                    okhttp3.Request request = new Request.Builder().url(getString(R.string.httpUrl) + "/" + pdfPath).build();
                    Response response = httpClient.newCall(request).execute();

                    InputStream is = response.body().byteStream();

                    BufferedInputStream input = new BufferedInputStream(is);
                    OutputStream output = new FileOutputStream(file);

                    byte[] data = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;

                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
