package com.adesh.projbk;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Adapter For detail image viewer
public class rvImageView extends RecyclerView.Adapter<rvImageView.ViewHolder> {
    Bitmap bitmap;
    Context context1;
    ArrayList<String> selected;

    public rvImageView(Context context, ArrayList selected) {
        this.context1 = context;
        this.selected = selected;

    }

    private Context getContext() {
        return context1;
    }

    @Override
    public rvImageView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View imView = inflater.inflate(R.layout.llimpreview, parent, false);
        rvImageView.ViewHolder viewHolder = new rvImageView.ViewHolder(imView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(rvImageView.ViewHolder holder, int position) {
        //check if image is null
        if (!selected.get(position).isEmpty() || selected != null || !selected.get(position).contains("null")) {

            if ((selected.get(position)).trim().length() != 4) {
                {
                    if ((selected.get(position)).trim().length() != 0) {
                        Log.i("selected", "" + selected.get(position).length());
                        String ustr = selected.get(position);
                        Log.i("imView", ustr);
                        Picasso.with(context1).load(context1.getString(R.string.httpUrl) + ustr).placeholder(R.drawable.defaultloading).into(holder.img);
                    }
        }
            }
        }
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
