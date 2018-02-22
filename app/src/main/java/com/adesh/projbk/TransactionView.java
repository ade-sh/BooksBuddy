package com.adesh.projbk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TransactionView extends AppCompatActivity {
    RecyclerView rvTransList;
    transactionListAdapter adapter;
    ArrayList<String> name, status, time, type;
    String uid, transType;
    ProgressBar pb;
    boolean isAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        rvTransList = (RecyclerView) findViewById(R.id.rv_viewTransaction);
        pb = (ProgressBar) findViewById(R.id.pb_TransView);
        name = new ArrayList<>();
        time = new ArrayList<>();
        type = new ArrayList<>();
        status = new ArrayList<>();
        adapter = new transactionListAdapter(name, time, status, time, false);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        transType = intent.getStringExtra("transType");
        Toast.makeText(TransactionView.this, "Uid" + uid + transType, Toast.LENGTH_LONG).show();
        fillData();
    }

    private void fillData() {
        pb.setVisibility(View.VISIBLE);
        if (transType.contains("All")) {
            isAll = true;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.add("trans", transType);
        client.post(getString(R.string.httpUrl) + "/getTansDetail.inc.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Getjson getjson = new Getjson(responseString);
                getjson.getTransDetail();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pb.setVisibility(View.GONE);
                if (Getjson.arrname.size() != 0) {
                    for (int i = 0; i < Getjson.arrname.size(); i++) {
                        name.add(Getjson.arrname.get(i));
                        time.add(Getjson.arrTime.get(i));
                        type.add(Getjson.arrType.get(i));
                        status.add(Getjson.arrStatus.get(i));
                    }
                    adapter = new transactionListAdapter(name, time, status, type, isAll);
                    RecyclerView.LayoutManager llm = new LinearLayoutManager(TransactionView.this, LinearLayoutManager.VERTICAL, isAll);
                    rvTransList.setLayoutManager(llm);
                    adapter.notifyDataSetChanged();
                    rvTransList.setAdapter(adapter);
                } else {
                    Toast.makeText(TransactionView.this, "No data found", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
