package com.adesh.projbk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class bkCustomAdapter extends ArrayAdapter<String> {

    int Ratin[];
    private Context context1;
    private String[] book;
    private String[] Img_id;
    private String Urls[];

    public bkCustomAdapter(Context context, String[] book, String[] Urls, String[] Img_id, int Rating[]) {
        super(context, R.layout.activity_main, book);
        this.book = book;
        this.Urls = Urls;
        this.context1 = context;
        this.Img_id = Img_id;
        this.Ratin = Rating;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup Parent) {
        ViewHolder viewHolder;
        // if(convertview==null){
        viewHolder = new ViewHolder();
        convertview = LayoutInflater.from(getContext()).inflate(R.layout.objnav, Parent, false);
        viewHolder.Title = (TextView) convertview.findViewById(R.id.bkText);
        viewHolder.img = (ImageView) convertview.findViewById(R.id.bkImg);
        viewHolder.rate = (RatingBar) convertview.findViewById(R.id.ratingBar);
        convertview.setTag(viewHolder);
        viewHolder.Title.setText(book[position]);
        viewHolder.img.setTag(Img_id[position]);
        viewHolder.rate.setRating(Ratin[position]);
        String ustr = Urls[position].substring(15);
        //setting image using picasso
        Picasso.with(context1).load(("http://10.0.3.2" + ustr)).placeholder(R.mipmap.im_defbk).resize(450, 235).into(viewHolder.img);

        // }
        /*    viewHolder=(ViewHolder)convertview.getTag();
            Log.i("convert view not null","Initialised");
        }*/
        return convertview;
    }

    public static class ViewHolder {
        TextView Title;
        ImageView img;
        RatingBar rate;
    }
}
