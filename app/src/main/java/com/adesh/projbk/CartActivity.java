package com.adesh.projbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CartActivity extends AppCompatActivity {
    RecyclerView rvCart;
    CartDatabaseHelper cartdatabase;
    TextView tvTotalPrice;
    Button Checkout;
    int total;
    String result;
    LinearLayout llpb;
    cartAdapter adapter;

    ArrayList<String> bookname, publisher, price, imgurl, bkid, uid;

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
        rvCart = (RecyclerView) findViewById(R.id.rvCart);
        bookname = new ArrayList<>();
        publisher = new ArrayList<>();
        price = new ArrayList<>();
        imgurl = new ArrayList<>();
        bkid = new ArrayList<>();
        uid = new ArrayList<>();
        llpb = (LinearLayout) findViewById(R.id.ll_cartProgressbar);
        tvTotalPrice = (TextView) findViewById(R.id.Tv_cartTotal);
        Checkout = (Button) findViewById(R.id.btn_cart_chk);
        fillDataFromdb();
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rvCart.setLayoutManager(llm);
        adapter = new cartAdapter(CartActivity.this, bookname, publisher, price, imgurl, bkid);
            rvCart.setAdapter(adapter);
            rvCart.getAdapter().notifyDataSetChanged();
            Checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!bookname.isEmpty()) {
                        OkHttpClient httpClient = new OkHttpClient();
                        Long now = System.currentTimeMillis();
                        Date d = new Date(now);
                        final int bill = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                        llpb.setVisibility(View.VISIBLE);
                        for (int a = 0; a < bkid.size(); a++) {
                            Log.i("bill1", bill + "");
                            Log.i("bknameinLoopcart", bookname.get(a));
                            RequestBody parameter = new FormBody.Builder().add("bkid", bkid.get(a) + "").add("bill_id", bill + "")
                                    .add("uid", uid.get(a) + "").add("bkname", bookname.get(a)).add("pbid", 3 + "").add("bkprice", price.get(a))
                                    .add("total_price", total + "").add("pbname", publisher.get(a)).build();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(getString(R.string.httpUrl) + "/setOrders.inc.php").post(parameter).build();
                            try {
                                Thread.sleep(100);
                                Response response = httpClient.newCall(request).execute();
                                result = response.body().string();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        llpb.setVisibility(View.GONE);
                        if (result.trim().contains("true")) {
                            cartdatabase = new CartDatabaseHelper(CartActivity.this);
                            cartdatabase.Deleteall();
                            bookname.clear();
                            publisher.clear();
                            price.clear();
                            imgurl.clear();
                            bkid.clear();
                            uid.clear();
                            adapter.notifyDataSetChanged();
                            tvTotalPrice.setText(" ");
                            Intent intent = new Intent(CartActivity.this, viewInvoiceActivity.class);
                            intent.putExtra("billid", bill + "");
                            intent.putExtra("create", "true");
                            startActivity(intent);
                        } else {
                            Toast.makeText(CartActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

    }

    public void updateTotal() {
        total = 0;
        for (int k = 0; k < price.size(); k++) {
            total = total + Integer.parseInt(price.get(k));
        }
        tvTotalPrice.setText("Total: " + total + "");
    }

    public void fillDataFromdb() {
        bookname = new ArrayList<>();
        publisher = new ArrayList<>();
        price = new ArrayList<>();
        imgurl = new ArrayList<>();
        bkid = new ArrayList<>();
        uid = new ArrayList<>();
        bookname.clear();
        publisher.clear();
        price.clear();
        imgurl.clear();
        bkid.clear();
        uid.clear();
        cartdatabase = new CartDatabaseHelper(CartActivity.this);
        Cursor res = cartdatabase.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "Nothing in your cart yet", Toast.LENGTH_SHORT).show();
            tvTotalPrice.setText(" ");
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
                        bkid.add(res.getString(1));
                        uid.add(res.getString(2));
                    }
                }
                updateTotal();
            }
        }
    }
}
