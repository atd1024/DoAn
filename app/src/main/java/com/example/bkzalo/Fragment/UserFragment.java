package com.example.bkzalo.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.bkzalo.Adapter.UserAdapter;
import com.example.bkzalo.MainActivity;
import com.example.bkzalo.Model.User;
import com.example.bkzalo.R;
import com.example.bkzalo.WebService.WebService;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user,container,false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUserOnStart();

        ReadUsers readUsers = new ReadUsers();
        readUsers.start();


        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    class ReadUsers extends Thread {
        @Override
        public void run() {
            while(true){
                AsyncTask getStateTask = new GetStateTask().execute();
                try {
                    String result = getStateTask.get().toString();
                    // nếu có user mới được insert vào bảng user
                    if(result.equals("true")){
                            // get lại danh sách user mới cập nhật
                        User current_user = MainActivity.current_user;
                        AsyncTask getAllUserTask = new GetAllUserTask().execute();
                        try {
                            mUsers = (ArrayList<User>)getAllUserTask.get();
                            for(int i=0; i<mUsers.size(); i++){
                                if (mUsers.get(i).getID() == current_user.getID()){
                                    mUsers.remove(i);
                                }
                            }
                            // set lại state cho isNewUser là false
                            new SetStateTask().execute();

                            // update lại phần UI
                            Handler threadHandler = new Handler(Looper.getMainLooper());
                            threadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    userAdapter = new UserAdapter(getContext(), mUsers, false);
                                    recyclerView.setAdapter(userAdapter);
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

    private void readUserOnStart() {
        // current user
        User current_user = MainActivity.current_user;
        AsyncTask getAllUserTask = new GetAllUserTask().execute();
        try {
            mUsers = (ArrayList<User>)getAllUserTask.get();
            for(int i=0; i<mUsers.size(); i++){
                if (mUsers.get(i).getID() == current_user.getID()){
                    mUsers.remove(i);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       // hiển thị users
        userAdapter = new UserAdapter(getContext(), mUsers, false);
        recyclerView.setAdapter(userAdapter);
    }

    private void searchUsers(String username) {
        //  search user
        // sử dụng onTextChange
        // gõ vào và lập tức hiển thị danh sách user tương ứng
    }

    class GetAllUserTask extends AsyncTask<Void, Integer, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            ArrayList<User> listUser = new ArrayList<User>();
            String jsonStr = WebService.getInstance().GetAllUser();
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    User user = WebService.getInstance().parserUser(jsonObject);
                    listUser.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listUser;
        }
    }

    class GetStateTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String result = WebService.getInstance().GetUserTableState();
            return result;
        }
    }

    class SetStateTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String[] values = {"false"};
            WebService.getInstance().SetUserTableState(values);
            return null;
        }
    }


}