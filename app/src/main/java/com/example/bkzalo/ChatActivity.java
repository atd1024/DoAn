package com.example.bkzalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bkzalo.Adapter.MessageAdapter;
import com.example.bkzalo.Model.Chat;
import com.example.bkzalo.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    int userid;

    //current user
    User current_user;


    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        // lấy dữ liệu của user được chọn bên UserList từ intent
        intent = getIntent();
        userid = intent.getIntExtra("userId",0);

        // get current user
        current_user = new User(); // tạo current user

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = text_send.getText().toString();
                if(!message.equals("")){
                    sendMessage(current_user.getID(),userid,message);
                }
                text_send.setText("");
            }
        });

        // lấy ra đối tượng user đang chat với mình dựa vào userid từ intent
        User user = new User(); // = getUserByID(userid);

        username.setText(user.getUsername());

        // set avatar
        profile_image.setImageResource(R.mipmap.ic_launcher);

        ReadMessage readMessage = new ReadMessage(current_user.getID(), userid, "default");   // thread start
        readMessage.start();
    }


    private void sendMessage(int sender, int receiver, String message){

        // insert message vào DB và set isNewChat là true


        // thêm user vào danh sách hiển thị các user chat cùng

//        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
//                .child(current_user.getUid())
//                .child(userid);
//
//        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(!dataSnapshot.exists()){
//                    chatRef.child("id").setValue(userid);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    // thread đọc tất cả tin nhắn từ DB
    class ReadMessage extends Thread {
        int myid;
        int userid;
        String imageurl;

        ReadMessage(int myid, int userid, String imageurl){
            this.myid = myid;
            this.userid = userid;
            this.imageurl = imageurl;
        }

        @Override
        public void run() {     // làm 1 vòng while(true)
            mChat = new ArrayList<>();

            // nếu isNewChat là true, thì bắt đầu các bước ở dưới
            mChat.clear();

            // duyệt hết danh sách getAllChat từ DB, kiểm tra điều kiện ở dưới
//        if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
//                chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
//            mChat.add(chat);
//        }
            // hiển thị lên
            messageAdapter = new MessageAdapter(ChatActivity.this, mChat, imageurl);
            recyclerView.setAdapter(messageAdapter);
        }
    }

//    // đọc tất cả tin nhắn từ DB
//    private void readMessage(final int myid, final int userid, final String imageurl){
//        mChat = new ArrayList<>();
//
//        // nếu isNewChat là true, thì bắt đầu các bước ở dưới
//        mChat.clear();
//
//        // duyệt hết danh sách getAllChat từ DB, kiểm tra điều kiện ở dưới
////        if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
////                chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
////            mChat.add(chat);
////        }
//        // hiển thị lên
//        messageAdapter = new MessageAdapter(ChatActivity.this, mChat, imageurl);
//        recyclerView.setAdapter(messageAdapter);
//    }

//    private void setStatus(String status){
//        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user.getUid());
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("status", status);
//
//        reference.updateChildren(hashMap);
//    }

    @Override
    protected void onResume() {     // when this activity is running
        super.onResume();
        //setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //setStatus("offline");       // when this activity is paused
    }

}