package com.adesh.projbk;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class bkUserFragment extends Fragment {
    TextView name, Email, Phone;
    String email;

    public bkUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bk_user, container, false);
        name = (TextView) v.findViewById(R.id.tvBkfrag_UserName);
        Phone = (TextView) v.findViewById(R.id.tvBkfrag_Phone);
        Email = (TextView) v.findViewById(R.id.tvBkfrag_Email);
        name.setText("Name: " + Getjson.JUserName);
        Email.setText("Email: " + Getjson.JEmail);
        Phone.setText("Phone: " + Getjson.JPhone);
        return v;
    }

}
