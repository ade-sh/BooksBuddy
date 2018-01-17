package com.adesh.projbk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class bkCustomAdapter extends RecyclerView.Adapter<bkCustomAdapter.ViewHolder> {

    int Ratin[];
    View.OnClickListener myClickListener;
    private Context context1;
    private String[] book;
    private String[] Img_id;
    private String Urls[];

    public bkCustomAdapter(Context context, View.OnClickListener myClickListener, String[] book, String[] Urls, String[] Img_id, int Rating[]) {
        this.book = book;
        this.Urls = Urls;
        this.context1 = context;
        this.Img_id = Img_id;
        this.Ratin = Rating;
        this.myClickListener = myClickListener;
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
    public void onBindViewHolder(bkCustomAdapter.ViewHolder viewHolder, int position) {
        viewHolder.Title.setText(book[position]);
        viewHolder.img.setTag(Img_id[position]);
        viewHolder.rate.setRating(Ratin[position]);
        String ustr = Urls[position].substring(15);
        Picasso.with(context1).load(("http://10.0.3.2" + ustr)).placeholder(R.mipmap.im_defbk).resize(450, 235).into(viewHolder.img);
    }

    @Override
    public int getItemCount() {
        return book.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        ImageView img;
        RatingBar rate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(myClickListener);
            Title = (TextView) itemView.findViewById(R.id.bkText);
            img = (ImageView) itemView.findViewById(R.id.bkImg);
            rate = (RatingBar) itemView.findViewById(R.id.ratingBar);
    }
}


}