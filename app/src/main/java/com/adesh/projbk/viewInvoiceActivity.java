package com.adesh.projbk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class viewInvoiceActivity extends AppCompatActivity {
    WebView wvshowpdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        wvshowpdf = (WebView) findViewById(R.id.wv_invoice);
        wvshowpdf.getSettings().setLoadWithOverviewMode(true);
        wvshowpdf.getSettings().setUseWideViewPort(true);
        Intent intent = getIntent();
        String bill = intent.getStringExtra("billid");
        RequestBody parameter = new FormBody.Builder().add("billid", bill).build();
        Log.i("bill2", bill);
        okhttp3.Request request = new okhttp3.Request.Builder().url(getString(R.string.httpUrl) + "/getInvoice.inc.php").post(parameter).build();
        Response response = null;
        try {
            OkHttpClient httpClient = new OkHttpClient();
            response = httpClient.newCall(request).execute();
            String pdfPath = response.body().string().trim();
            wvshowpdf.loadUrl("http://docs.google.com/gview?embedded=true&url=" + getString(R.string.httpUrl) + "/" + pdfPath);
            Log.i("pdfURl", pdfPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
