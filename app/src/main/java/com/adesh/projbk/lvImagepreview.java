package com.adesh.projbk;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class lvImagepreview extends RecyclerView.Adapter<lvImagepreview.ViewHolder> {
    Bitmap bitmap;
    Context context1;
    List<Uri> selected;

    public lvImagepreview(Context context, List<Uri> selected) {
        this.context1 = context;
        this.selected = selected;

    }

    private Context getContext() {
        return context1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View imView = inflater.inflate(R.layout.llimpreview, parent, false);
        ViewHolder viewHolder = new ViewHolder(imView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context1).load(selected.get(position)).placeholder(R.drawable.default_user).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return selected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.llivPre);
        }
    }
}