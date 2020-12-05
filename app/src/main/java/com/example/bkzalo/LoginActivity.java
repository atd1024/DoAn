package com.example.bkzalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.bkzalo.Model.User;
import com.example.bkzalo.WebService.WebService;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

// firebase lib
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;


public class LoginActivity extends AppCompatActivity {

    MaterialEditText email, password;
    Button btn_login;

    // current user
    // User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {

           // @SuppressLint("WrongConstant")
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);

                String txt_email=email.getText().toString().trim();
                String txt_password=password.getText().toString().trim();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    int currentUserID = CheckLogin(txt_email,txt_password);
                    if ( currentUserID != 0) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("currentUserID", currentUserID);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


        public int CheckLogin (String username, String password){
            AsyncTask getUserTask = new GetUserTask().execute(username, password);
            try {
                User user =(User)getUserTask.get();
                if (user != null) {
                    return user.getID();
                } else {
                    return 0;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
                return 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            }
        }

        class GetUserTask extends AsyncTask<String, Integer, User> {
            @Override
            protected User doInBackground(String... params) {
                User user = null;
                try {
                    String jsonStr = WebService.getInstance().Login(params);
                    user = WebService.getInstance().parserUser(jsonStr);
                    return user;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            
            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
            }
        }
}

