package com.adesh.projbk;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class bkdeatilFragment extends Fragment {


    Button btnBuy;
    RatingBar ratingBar;
    RecyclerView rvAllimg;
    private TextView mTextMessage, et_bkName, et_bkDisk;

    public bkdeatilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bkdeatil, container, false);
        btnBuy = (Button) v.findViewById(R.id.btn_buy);
        mTextMessage = (TextView) v.findViewById(R.id.mtv_view);
        et_bkName = (TextView) v.findViewById(R.id.et_bkName);
        et_bkDisk = (TextView) v.findViewById(R.id.tv_deatils);
        ratingBar = (RatingBar) v.findViewById(R.id.rb_bkRating);
        rvAllimg = (RecyclerView) v.findViewById(R.id.lvdetailItems);
        String sPos = getArguments().getString("bkPos");
        String upType = getArguments().getString("type");
        et_bkName.setText(sPos);
        et_bkDisk.setText("");
        if (Getjson.arrUploader.get(0).contains("user") || Getjson.arrUploader.get(0).contains("Request")) {
            ratingBar.setVisibility(View.GONE);
        }
        if (Getjson.arrUploader.get(0).contains("Request")) {
            btnBuy.setVisibility(View.GONE);
        }
        et_bkName.setText(Getjson.arrname.get(0));
        et_bkDisk.setText(Getjson.arrDisc.get(0));
        ratingBar.setRating(Integer.parseInt(Getjson.arrRatin.get(0)));
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
        return v;
    }


}
