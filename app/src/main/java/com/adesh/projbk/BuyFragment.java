package com.adesh.projbk;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BuyFragment extends Fragment {
    TextView tvname, tvPrice, tvpublisher, tvmsg;
    Button btnBuy, btnClose;
    String uid;
    public BuyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy, container, false);
        tvname = (TextView) v.findViewById(R.id.tv_frag_buy_name);
        tvPrice = (TextView) v.findViewById(R.id.tv_frag_buy_price);
        tvpublisher = (TextView) v.findViewById(R.id.tv_frag_buy_seller);
        tvmsg = (TextView) v.findViewById(R.id.tv_frag_buy_msg);
        if (!Getjson.arrname.isEmpty()) {
        tvname.setText(Getjson.arrname.get(0));
        tvPrice.setText(Getjson.arrPrice.get(0));
            tvmsg.setText("Your details will be sent to the " + Getjson.JUserName + "  #" + Getjson.arrid.get(0));
            tvpublisher.setText(Getjson.JUserName);
        }
        btnBuy = (Button) v.findViewById(R.id.btn_frag_buy);

        btnClose = (Button) v.findViewById(R.id.btn_frag_buy_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseFragment();
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDatabaseHelper dbHelper = new CartDatabaseHelper(getActivity());
                boolean added = dbHelper.insertData(Getjson.arrname.get(0), Integer.parseInt(Getjson.arrid.get(0)), Integer.parseInt(Getjson.arruid.get(0)), Integer.parseInt(Getjson.arrPrice.get(0)), Getjson.arrurls.get(0), Getjson.JUserName);
                onCloseFragment();
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);

            }
        });
        //to close on back press
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                }
                return true;
            }
        });
        return v;
    }

    private void onCloseFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
