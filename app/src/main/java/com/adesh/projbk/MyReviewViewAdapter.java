package com.adesh.projbk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MyReviewViewAdapter extends RecyclerView.Adapter<MyReviewViewAdapter.ViewHolder> {
    private final ArrayList<String> Head;
    private final ArrayList<Integer> Rating;
    private final ArrayList<String> user;
    private final ArrayList<String> body;

    public MyReviewViewAdapter(ArrayList<String> Head, ArrayList<Integer> Rating, ArrayList<String> user, ArrayList<String> body) {

        this.Head = Head;
        this.Rating = Rating;
        this.body = body;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (!Head.get(position).equals("none") && !body.get(position).equals("none")) {
            holder.Head.setText(Head.get(position));
            holder.rating.setRating(Rating.get(position));
            holder.User.setText("By " + user.get(position));
            holder.Body.setText(body.get(position));
        } else {
            holder.Head.setText("No Review available for this book");
            holder.Body.setVisibility(View.GONE);
            holder.rating.setVisibility(View.GONE);
            holder.User.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return body.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Head, User, Body;
        RatingBar rating;
        public ViewHolder(View view) {
            super(view);
            Head = (TextView) view.findViewById(R.id.tv_review_head);
            Body = (TextView) view.findViewById(R.id.tv_review_Body);
            User = (TextView) view.findViewById(R.id.tv_review_user);
            rating = (RatingBar) view.findViewById(R.id.rb_review);
        }

    }
}
