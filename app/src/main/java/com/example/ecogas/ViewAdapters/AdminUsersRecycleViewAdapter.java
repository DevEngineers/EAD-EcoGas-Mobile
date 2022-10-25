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

public class AdminUsersRecycleViewAdapter extends RecyclerView.Adapter<AdminUsersRecycleViewAdapter.ViewHolder>{

    private ArrayList<String> user = new ArrayList<>();
    private ArrayList<String> userName = new ArrayList<>();


    private Context mContext;

    public AdminUsersRecycleViewAdapter(ArrayList<String> user, ArrayList<String> userName, Context mContext) {
        this.user = user;
        this.userName = userName;
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
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,userNameView;
        RelativeLayout adminUserViewLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userViewName);
            userNameView=itemView.findViewById(R.id.userViewUserName);
            adminUserViewLayout=itemView.findViewById(R.id.adminUserViewLayout);
        }

    }
}
