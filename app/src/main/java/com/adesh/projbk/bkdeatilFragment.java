package com.adesh.projbk;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;


public class bkdeatilFragment extends Fragment {


    Button btnBuy;
    RatingBar ratingBar;
    RecyclerView rvAllimg;
    Button delete;
    String username, Password, uid;
    ProgressBar pb;
    private TextView mTime, et_bkName, et_bkDisk, price;

    public bkdeatilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bkdeatil, container, false);
        btnBuy = (Button) v.findViewById(R.id.btn_buy);
        mTime = (TextView) v.findViewById(R.id.mtv_view);
        et_bkName = (TextView) v.findViewById(R.id.et_bkName);
        et_bkDisk = (TextView) v.findViewById(R.id.tv_deatils);
        price = (TextView) v.findViewById(R.id.tv_price);
        ratingBar = (RatingBar) v.findViewById(R.id.rb_bkRating);
        rvAllimg = (RecyclerView) v.findViewById(R.id.lvdetailItems);
        pb = (ProgressBar) v.findViewById(R.id.pb_bkfragdetail);
        String sPos = getArguments().getString("bkPos");
        String upType = getArguments().getString("type");
        delete = (Button) v.findViewById(R.id.btn_delete);
        SharedPreferences sp = getActivity().getSharedPreferences("UserLogin", MODE_PRIVATE);
        username = sp.getString("UserName", null);
        Password = sp.getString("Password", null);
        uid = sp.getString("Uid", null);

        if (Getjson.arruid.get(0).equals(uid) && Getjson.arrUploader.get(0).equals("user")) {
            delete.setVisibility(View.VISIBLE);
            btnBuy.setVisibility(View.GONE);

        }
        et_bkName.setText(sPos);
        et_bkDisk.setText("");
        if (Getjson.arrUploader.get(0).contains("user") || Getjson.arrUploader.get(0).contains("Request")) {
            ratingBar.setVisibility(View.GONE);
        }
        if (Getjson.arrUploader.get(0).contains("Request")) {
            btnBuy.setVisibility(View.GONE);
            price.setVisibility(View.GONE);
        }
        if (Getjson.arrname.size() > 0) {
        et_bkName.setText(Getjson.arrname.get(0));
        et_bkDisk.setText(Getjson.arrDisc.get(0));
            price.setText("₹" + Getjson.arrPrice.get(0));
            mTime.setText(Getjson.arrTime.get(0));
            ratingBar.setRating(Integer.parseInt(Getjson.arrRatin.get(0)));
        }
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        ArrayList lvUrls = new ArrayList();

        lvUrls.add(Getjson.arrurls.get(0));
        if (Getjson.arrurls2.get(0) != null && Getjson.arrurls2.get(0).length() != 4 && Getjson.arrurls2.get(0).isEmpty() && Getjson.arrurls2.get(0).length() != 0) {
            lvUrls.add(Getjson.arrurls2.get(0));
        }
        if (Getjson.arrurls3.get(0) != null && Getjson.arrurls3.get(0).length() != 4 && Getjson.arrurls3.get(0).isEmpty() && Getjson.arrurls3.get(0).length() != 0) {
            lvUrls.add(Getjson.arrurls3.get(0));
        }
        rvImageView rvAdapter = new rvImageView(getActivity().getApplicationContext(), lvUrls);
        rvAllimg.setLayoutManager(llm);
        rvAllimg.setAdapter(rvAdapter);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("email", username);
                params.put("password", Password);
                params.put("type", Getjson.arrUploader.get(0).trim());
                params.put("bkid", Getjson.arrid.get(0).trim());
                client.post(getString(R.string.httpUrl) + "/delete.inc.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        pb.setVisibility(View.GONE);
                        Intent in = new Intent(getContext(), MainActivity.class);
                        Log.d("response in delete", responseString);
                        startActivity(in);
                    }
                });
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment frag = new BuyFragment();
                ft.replace(R.id.lv_buyfragPlace, frag);
                ft.commit();
            }
        });

        return v;
    }
}
