package com.adesh.projbk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {
    Context mcontext;
    ArrayList<String> bookname, publisher, price, imgurl, bkid;
    int pos;

    cartAdapter(Context context, ArrayList<String> Bookname, ArrayList<String> publisher, ArrayList<String> price, ArrayList<String> imgurl, ArrayList<String> bkid) {
        this.mcontext = context;
        this.bookname = Bookname;
        this.publisher = publisher;
        this.price = price;
        this.imgurl = imgurl;
        this.bkid = bkid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View imView = inflater.inflate(R.layout.cart_rv_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(imView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        pos = holder.getAdapterPosition();
        holder.tvName.setText(bookname.get(position));
        holder.tvPrice.setText(price.get(position));
        holder.tvPublisher.setText(publisher.get(position));
        Picasso.with(mcontext).load(mcontext.getString(R.string.httpUrl) + imgurl.get(position)).into(holder.iv);
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDatabaseHelper cd = new CartDatabaseHelper(mcontext);
                cd.deleteData(bkid.get(pos));
                Toast.makeText(mcontext, bkid.get(pos), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvPublisher;
        Button minus;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_car_bkname);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_cart_bkPrice);
            tvPublisher = (TextView) itemView.findViewById(R.id.tv_cart_bkPubl);
            minus = (Button) itemView.findViewById(R.id.btn_rv_cartrem);
            iv = (ImageView) itemView.findViewById(R.id.iv_cart_list);
        }
    }
}
