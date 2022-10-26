package com.example.ecogas.ViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecogas.R;

import java.util.ArrayList;

/**
 * This adapter class is to set and initiate the user details in recycle view to view in AdminViewStations screen
 *
 * Author: IT19153414 Akeel M.N.M
 */

public class AdminUsersRecycleViewAdapter extends RecyclerView.Adapter<AdminUsersRecycleViewAdapter.ViewHolder>{

    private ArrayList<String> user = new ArrayList<>();
    private ArrayList<String> userName = new ArrayList<>();
    private ArrayList<String> userType = new ArrayList<>();
    private Context mContext;

    public AdminUsersRecycleViewAdapter(ArrayList<String> user, ArrayList<String> userName, ArrayList<String> userType, Context mContext) {
        this.user = user;
        this.userName = userName;
        this.userType = userType;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdminUsersRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_view_layout,parent,false);
        AdminUsersRecycleViewAdapter.ViewHolder viewHolder = new AdminUsersRecycleViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUsersRecycleViewAdapter.ViewHolder holder, final int position) {

        holder.name.setText(user.get(position));
        holder.userNameView.setText(userName.get(position));
        holder.userTypeView.setText(userType.get(position));
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,userNameView,userTypeView;
        RelativeLayout adminUserViewLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userViewName);
            userNameView=itemView.findViewById(R.id.userViewUserName);
            userTypeView = itemView.findViewById(R.id.userType);
            adminUserViewLayout=itemView.findViewById(R.id.adminUserViewLayout);
        }

    }
}
