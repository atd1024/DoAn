package com.example.bkzalo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.bkzalo.ChatActivity;
import com.example.bkzalo.ChatActivity;
import com.example.bkzalo.Model.User;
import com.example.bkzalo.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    //fields
    private Context mContext;
    private List<User> mUsers;
    private boolean isChat;

    //constructor
    public UserAdapter(Context mContext, List<User> mUsers, boolean isChat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isChat = isChat;   // check if list user to display is in ChatsFragment or not
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.displayname.setText(user.getDisplayname());

        holder.profile_image.setImageResource(R.mipmap.ic_launcher);

         //load ảnh avatar
//        if(user.getImageURL().equals("default")){
//            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
//        } else {
//            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
//        }

        // click vào user thì vào chat activity, truyền theo id user tương ứng
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userID", user.getID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    //inner class
    public class ViewHolder extends RecyclerView.ViewHolder{

        //fields
        public TextView displayname;
        public ImageView profile_image;
        public ImageView img_online;
        public ImageView img_offline;
        // implement super constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            displayname = itemView.findViewById(R.id.displayname);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_online = itemView.findViewById(R.id.img_online);
            img_offline = itemView.findViewById(R.id.img_offline);
        }
    }
}