package com.adesh.projbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvCart;
    CartDatabaseHelper cartdatabase;
    TextView tvTotalPrice;
    Button Checkout;
    int total, uid;
    OkHttpClient httpClient;
    ArrayList<String> bookname, publisher, price, imgurl, bkid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        Boolean LoginStatus = sp.getBoolean("IsLogged", false);
        if (!LoginStatus) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        //uid=getUserId();
        rvCart = (RecyclerView) findViewById(R.id.rvCart);
        bookname = new ArrayList<>();
        publisher = new ArrayList<>();
        price = new ArrayList<>();
        imgurl = new ArrayList<>();
        bkid = new ArrayList<>();
        tvTotalPrice = (TextView) findViewById(R.id.Tv_cartTotal);
        Checkout = (Button) findViewById(R.id.btn_cart_chk);
        cartdatabase = new CartDatabaseHelper(CartActivity.this);
        Cursor res = cartdatabase.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "Nothing in your cart yet", Toast.LENGTH_SHORT).show();
            return;
        } else {
            long sizeofCur = cartdatabase.getCountSize();
            if (sizeofCur != -1) {
                for (int i = 0; i <= sizeofCur; i++) {
                    while (res.moveToNext()) {
                        bookname.add(res.getString(3));
                        publisher.add(res.getString(6));
                        price.add(res.getString(4));
                        imgurl.add(res.getString(5));
                        bkid.add(res.getString(2));
                    }
                }
            }
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rvCart.setLayoutManager(llm);
            cartAdapter adapter = new cartAdapter(CartActivity.this, bookname, publisher, price, imgurl, bkid);
            rvCart.setAdapter(adapter);
            rvCart.getAdapter().notifyDataSetChanged();
            for (int k = 0; k < price.size(); k++) {
                total = total + Integer.parseInt(price.get(k));
            }
            tvTotalPrice.setText("Total: " + total + "");
            Checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpClient httpClient = new OkHttpClient();
                    Date d = new Date();
                    long bill = d.getTime() + uid;
                    RequestBody parameter = new FormBody.Builder().add("bkid", bkid.get(1) + "").add("bill_id", bill + "").add("uid", uid + "").add("bkname", bookname.get(1)).add("pb_id", 3 + "").add("bkprice", price.get(1)).add("total_price", total + "").add("pnname", publisher.get(1)).build();
                    okhttp3.Request request = new okhttp3.Request.Builder().url(getString(R.string.httpUrl) + "/setOrders.inc.php").post(parameter).build();
                    PdfDocument sb;
                    try {
                        Response response = httpClient.newCall(request).execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public int getUserId() {
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        final String username = sp.getString("UserName", null);
        String res = 0 + "";
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
                String res;
                if (responseString.contains("false") && !responseString.contains("null")) {
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
        return Integer.parseInt(res);
    }
}
