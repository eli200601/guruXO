package com.app.elisoft.guru.Recycler;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Table.User;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

    private static String TAG = "RecyclerAdapter";

    private Context context;
    private ArrayList<User> usersList;

    public RecyclerAdapter(Context context, ArrayList<User> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() Started");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, null);
        ViewHolder holder = new ViewHolder(view);
        holder.onlineIndication.setVisibility(View.INVISIBLE);
        holder.lastLogin.setVisibility(View.INVISIBLE);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() Started");
        User user = usersList.get(position);
        String userName = user.getEmail();
        long lastPing = user.getLastLogin();
        String lastStatus;


        lastStatus = setLastOnlineStatus(lastPing);
        if (lastStatus.equals("ONLINE")) {
            holder.onlineIndication.setVisibility(View.VISIBLE);
        }
        else {
            holder.lastLogin.setText(lastStatus);
            holder.lastLogin.setVisibility(View.VISIBLE);
        }
        holder.userName.setText(userName);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onDataItemClick(View view, int position) {
                //When Clicking on item in list
                Log.d(TAG, "Item in Recycler clicked: " + String.valueOf(position));
            }
        });

    }

    public String setLastOnlineStatus(long lastLogin){
        long start = System.currentTimeMillis();
        long hour = 60*1000;
        long inteval = 2*1000;

        Log.d(TAG, "### Time is:");
        long diff = start - lastLogin;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
//        Log.d(TAG, String.valueOf(days));
        Log.d(TAG, "Sec:" + String.valueOf(diffSeconds) + " Min:" + String.valueOf(diffMinutes) + " Hour:" + String.valueOf(diffHours) + " Day:" + String.valueOf(diffDays));
        if (diffDays > 0) {
            return "24h+";
        } else {
            if ( diffHours> 0) {
                return String.valueOf(diffHours) + "h";
            } else {
                if (diffMinutes > 2) {
                    return String.valueOf(diffMinutes) + "m";
                }
                else {
                    return "ONLINE";
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setItems(ArrayList<User> usersList){
        this.usersList = usersList;
    }
}
