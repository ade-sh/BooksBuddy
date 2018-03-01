package com.adesh.projbk;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class setReviewFragment extends Fragment {
    EditText etHead, etBody;
    Button setRev, cancelRev;
    RatingBar ratingBar;
    String uid, DUserName;

    public setReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_review, container, false);
        etHead = (EditText) v.findViewById(R.id.et_frag_setReview_Head);
        etBody = (EditText) v.findViewById(R.id.et_frag_setReview_body);
        setRev = (Button) v.findViewById(R.id.btn_frag_setReview_send);
        cancelRev = (Button) v.findViewById(R.id.btn_frag_setReview_cancel);
        ratingBar = (RatingBar) v.findViewById(R.id.rb_frag_setReview);
        SharedPreferences sp = getActivity().getSharedPreferences("UserLogin", MODE_PRIVATE);
        uid = sp.getString("Uid", null);
        SharedPreferences settingPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        DUserName = settingPreference.getString("display_name", "anon");
        setRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpClient httpClient = new OkHttpClient();
                RequestBody parameter = new FormBody.Builder().add("head", etHead.getText().toString()).add("body", etBody.getText().toString()).add("rating", ratingBar.getRating() + "").add("uid", uid).add("bkid", Getjson.arrid.get(0).trim()).add("user", DUserName).build();
                okhttp3.Request request = new Request.Builder().url(getString(R.string.httpUrl) + "/setReview.inc.php").post(parameter).build();
                try {
                    Response response = httpClient.newCall(request).execute();
                    String Result = response.body().string();
                    if (Result.contains("false")) {
                        Toast.makeText(getActivity(), "You already have a review for this book", Toast.LENGTH_LONG).show();
                    } else if (Result.contains("true")) {
                        Toast.makeText(getActivity(), "Added your Review", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onCloseFragment();
            }
        });
        cancelRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseFragment();
            }
        });
        //add back button close
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onCloseFragment();
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
