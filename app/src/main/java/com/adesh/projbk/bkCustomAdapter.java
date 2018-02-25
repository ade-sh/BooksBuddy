package com.adesh.projbk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class bkCustomAdapter extends RecyclerView.Adapter<bkCustomAdapter.ViewHolder> {

    ArrayList<String> arrBook;
    ArrayList<String> arrId;
    ArrayList<String> arrUrl;
    ArrayList<String> arrRatin;
    ArrayList<String> arrUploader;
    private Context context1;

    public bkCustomAdapter(Context context, ArrayList book, ArrayList Urls, ArrayList<String> Img_id, ArrayList arrRatin, ArrayList arrUploader) {
        this.arrBook = book;
        this.arrUrl = Urls;
        this.context1 = context;
        this.arrId = Img_id;
        this.arrRatin = arrRatin;
        this.arrUploader = arrUploader;
    }

    private Context getContext() {
        return context1;
    }
    @Override
    public bkCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View bkView = inflater.inflate(R.layout.objnav, parent, false);
        ViewHolder viewHolder = new ViewHolder(bkView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(bkCustomAdapter.ViewHolder viewHolder, final int position) {
        if (arrUploader.get(viewHolder.getAdapterPosition()).contains("user")) {
            viewHolder.rate.setVisibility(View.GONE);
            viewHolder.Uploader.setVisibility(View.VISIBLE);
            viewHolder.Uploader.setText("By User");
        } else if (arrUploader.get(viewHolder.getAdapterPosition()).contains("Request")) {
            viewHolder.rate.setVisibility(View.GONE);
            viewHolder.Uploader.setVisibility(View.VISIBLE);
            viewHolder.Uploader.setText("Request");
            viewHolder.Uploader.setBackgroundColor(Color.CYAN);
        } else {
            viewHolder.Uploader.setVisibility(View.GONE);
        }
        viewHolder.Title.setText(arrBook.get(viewHolder.getAdapterPosition()));
        viewHolder.img.setTag(arrId.get(viewHolder.getAdapterPosition()));
        viewHolder.rate.setRating(Integer.parseInt(arrRatin.get(viewHolder.getAdapterPosition())));
        String ustr = arrUrl.get(viewHolder.getAdapterPosition());
        Picasso.with(context1).load(context1.getString(R.string.httpUrl) + ustr).resize(500, 235).placeholder(R.drawable.defaultloading).into(viewHolder.img);
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, bk_details.class);
                //for transition animation
                // Pair<View, String> pair1 = Pair.create(viewHolder.findViewById(R.id.bkImg), ViewCompat.getTransitionName(viewHolder.findViewById(R.id.bkImg)));
                //Pair<View, String> pair2 = Pair.create(viewHolder.findViewById(R.id.ratingBar), ViewCompat.getTransitionName(viewHolder.findViewById(R.id.ratingBar)));
                // ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1, pair2);
                intent.putExtra("bkPos", arrId.get(position));
                intent.putExtra("Type", arrUploader.get(position));
                context1.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrBook.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView Uploader;
        ImageView img;
        RatingBar rate;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.ll_objnav);
            Title = (TextView) itemView.findViewById(R.id.bkText);
            img = (ImageView) itemView.findViewById(R.id.bkImg);
            rate = (RatingBar) itemView.findViewById(R.id.ratingBar);
            Uploader = (TextView) itemView.findViewById(R.id.tvUploader);
    }
}

}