package com.example.bkzalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bkzalo.Adapter.MessageAdapter;
import com.example.bkzalo.Adapter.UserAdapter;
import com.example.bkzalo.Fragment.ProfileFragment;
import com.example.bkzalo.Fragment.UserFragment;
import com.example.bkzalo.Model.Chat;
import com.example.bkzalo.Model.User;
import com.example.bkzalo.WebService.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    // id user mà người dùng muốn chat
    int userid;

    // user mà người dùng muốn chat
    User user;

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

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
        userid = intent.getIntExtra("userID", 0);
        user = getUserByID(userid);

        // get current user
        current_user = MainActivity.current_user; // tạo current user

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = text_send.getText().toString().trim();
                if ((!message.equals("")) && !message.matches("[\\n\\r]+")) {
                    sendMessage(current_user.getID(), userid, message);
                }
                text_send.setText("");
            }
        });

        username.setText(user.getDisplayname());

        // load avatar
        if(user.getImageURL().equals("default")){
            profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(ChatActivity.this).load(user.getImageURL()).into(profile_image);
        }

        readChatOnStart();

        ReadMessage readMessage = new ReadMessage(current_user.getID() + "", userid + "");   // thread start
        readMessage.start();

//        UpdateImage updateImage = new UpdateImage();
//        updateImage.start();
    }


    private void sendMessage(int sender, int receiver, String message) {

        // insert message vào DB và set isNewChat là true
        AsyncTask sendMessageTask = new SendMessageTask().execute(sender + "", receiver + "", message);

        // thêm user vào danh sách hiển thị các user chat cùng (Message Fragmemt)
    }

    // thread đọc tất cả tin nhắn từ DB
    class ReadMessage extends Thread {
        String current_user_id;
        String userid;

        ReadMessage(String current_user_id, String userid) {
            this.current_user_id = current_user_id;
            this.userid = userid;
        }

        @Override
        public void run() {
            while (true) {
                AsyncTask getStateTask = new GetStateTask().execute();
                try {
                    String result = getStateTask.get().toString();
                    // nếu có chat mới được insert vào bảng chats
                    if (result.equals("true")) {
                        // get lại danh sách chat mới cập nhật
                        AsyncTask getAllChatTask = new GetAllChatTask().execute(current_user_id, userid);
                        try {
                            mChat = (ArrayList<Chat>)getAllChatTask.get();
                            // set lại state cho isChatUser là false
                            new SetStateTask().execute();

                            // update lại phần UI
                            Handler threadHandler = new Handler(Looper.getMainLooper());
                            threadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    messageAdapter = new MessageAdapter(ChatActivity.this, mChat, user.getImageURL());
                                    recyclerView.setAdapter(messageAdapter);
                                }
                            });
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // thread bắt thay đổi avatar
    class UpdateImage extends Thread {
        @Override
        public void run() {
            while (true) {
                AsyncTask getStateTask = new GetUserStateTask().execute();
                try {
                    String result = getStateTask.get().toString();
                    if (result.equals("true")) {
                        // set lại user table state
                        new SetUserStateTask().execute();
                        // update lại phần UI
                        Handler threadHandler = new Handler(Looper.getMainLooper());
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                user = getUserByID(userid);
                                Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                            }
                        });
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    private User getUserByID(int idUser) {
        AsyncTask getUserByID = new GetUserByIDTask().execute(idUser + "");
        try {
            User user = (User) getUserByID.get();
            if (user != null) {
                return user;
            } else {
                return null;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void readChatOnStart() {
        AsyncTask getAllChatTask = new GetAllChatTask().execute(current_user.getID()+"", userid+"");
        try {
            mChat = (ArrayList<Chat>)getAllChatTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // hiển thị
        messageAdapter = new MessageAdapter(ChatActivity.this, mChat, user.getImageURL());
        recyclerView.setAdapter(messageAdapter);
    }

    class GetUserByIDTask extends AsyncTask<String, Integer, User> {
            @Override
            protected User doInBackground(String... params) {
                User user = null;
                try {
                    String jsonStr = WebService.getInstance().GetUserByID(params);
                    user = WebService.getInstance().parserUser(jsonStr);
                    return user;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    class GetAllChatTask extends AsyncTask<String, Integer, ArrayList<Chat>> {
            @Override
            protected ArrayList<Chat> doInBackground(String... params) {
                ArrayList<Chat> listChat = new ArrayList<Chat>();
                String jsonStr = WebService.getInstance().GetAllChat(params);
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Chat chat = WebService.getInstance().parseChat(jsonObject);

                        listChat.add(chat);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return listChat;
            }
        }

    class SendMessageTask extends AsyncTask<String, Integer, Void> {
            @Override
            protected Void doInBackground(String... params) {
                WebService.getInstance().SendMessage(params);
                return null;
            }
        }

    // state CHAT
    class GetStateTask extends AsyncTask<Void, Integer, String> {
            @Override
            protected String doInBackground(Void... voids) {
                String result = WebService.getInstance().GetChatTableState();
                return result;
            }
        }

    class SetStateTask extends AsyncTask<Void, Integer, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                String[] values = {"false"};
                WebService.getInstance().SetChatTableState(values);
                return null;
            }
        }

    // state USER
    class GetUserStateTask extends AsyncTask<Void, Integer, String> {
            @Override
            protected String doInBackground(Void... voids) {
                String result = WebService.getInstance().GetUserTableState();
                return result;
            }
        }

    class SetUserStateTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String[] values = {"false"};
            WebService.getInstance().SetUserTableState(values);
            return null;
        }
    }
    }
