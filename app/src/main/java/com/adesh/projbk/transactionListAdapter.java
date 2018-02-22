package com.adesh.projbk;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class transactionListAdapter extends RecyclerView.Adapter<transactionListAdapter.ViewHolder> {
    ArrayList<String> arrTransName;
    ArrayList<String> arrTransTime;
    ArrayList<String> arrTransStatus;
    ArrayList<String> arrTransType;
    private boolean isAlltrans;

    public transactionListAdapter(ArrayList<String> name, ArrayList<String> Time, ArrayList<String> Status, ArrayList<String> Type, boolean isAlltrans) {
        this.arrTransName = name;
        this.arrTransStatus = Status;
        this.arrTransTime = Time;
        this.arrTransType = Type;
        this.isAlltrans = isAlltrans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View imView = inflater.inflate(R.layout.translist, parent, false);
        ViewHolder viewHolder = new ViewHolder(imView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isAlltrans) {
            holder.tvType.setVisibility(View.VISIBLE);
            holder.tvType.setText(arrTransType.get(position));
        }
        holder.tvname.setText(arrTransName.get(position));
        holder.tvStatus.setText(arrTransStatus.get(position));
        holder.tvtime.setText(arrTransTime.get(position));

    }

    @Override
    public int getItemCount() {
        return arrTransName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvtime, tvStatus, tvType;

        public ViewHolder(View itemView) {
            super(itemView);
            tvname = (TextView) itemView.findViewById(R.id.tv_trans_name);
            tvtime = (TextView) itemView.findViewById(R.id.tv_trans_time);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_trans_status);
            tvType = (TextView) itemView.findViewById(R.id.tv_trans_type);
        }
    }
}
