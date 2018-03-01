package com.adesh.projbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvCart;
    CartDatabaseHelper cartdatabase;
    TextView tvTotalPrice;
    Button Checkout;
    int total;
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
        }

    }
}
