package com.app.elisoft.guru.Recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.elisoft.guru.R;


public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    TextView userName;
    TextView lastLogin;
    ImageView onlineIndication;

    RelativeLayout item;

    ItemClickListener itemClickListener;

    public ViewHolder(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        lastLogin = (TextView) itemView.findViewById(R.id.last_seen_text);
        onlineIndication = (ImageView) itemView.findViewById(R.id.user_online);
        item = (RelativeLayout) itemView.findViewById(R.id.single_item);

        item.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onDataItemClick(view, getLayoutPosition());
    }


}
