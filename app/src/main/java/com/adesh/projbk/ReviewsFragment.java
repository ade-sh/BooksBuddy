package com.adesh.projbk;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class ReviewsFragment extends Fragment {

    ArrayList<String> arrHead;
    ArrayList<String> arrBOdy;
    ArrayList<String> arrUser;
    ArrayList<Integer> arrReview;
    ProgressBar pb;

    public ReviewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvReviewList);
        arrUser = new ArrayList<>();
        pb = (ProgressBar) view.findViewById(R.id.pbReviewList);
        arrReview = new ArrayList<>();
        arrBOdy = new ArrayList<>();
        arrHead = new ArrayList<>();
        if (Getjson.arrRvHead != null) {
            pb.setVisibility(View.GONE);
            arrHead = Getjson.arrRvHead;
            arrUser = Getjson.arrRvUser;
            arrReview = Getjson.arrRvRating;
            arrBOdy = Getjson.arrRvBody;
        // Set the adapter
            Context context = view.getContext();
            LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(llm);
            MyReviewViewAdapter adapter = new MyReviewViewAdapter(arrHead, arrReview, arrUser, arrBOdy);
            recyclerView.setAdapter(adapter);
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            pb.setVisibility(View.VISIBLE);
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                }
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btn_fragsetReview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment frag = new setReviewFragment();
                ft.replace(R.id.lv_frag_place_review, frag).addToBackStack(null).commit();
                pb.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
