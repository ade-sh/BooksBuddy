package com.adesh.projbk;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class bkUserFragment extends Fragment {
    TextView name, Email, Phone;


    public bkUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bk_user, container, false);
        name = (TextView) v.findViewById(R.id.tvBkfrag_UserName);
        Phone = (TextView) v.findViewById(R.id.tvBkfrag_Phone);
        Email = (TextView) v.findViewById(R.id.tvBkfrag_Email);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewAccount.class);
                intent.putExtra("uid", Getjson.arruid.get(0));
                intent.putExtra("uploader", Getjson.arrUploader.get(0).trim());
                startActivity(intent);
            }
        });
        name.setText(Getjson.JUserName);
        Email.setText(Getjson.JEmail);
        Phone.setText(Getjson.JPhone);
        return v;
    }

}
