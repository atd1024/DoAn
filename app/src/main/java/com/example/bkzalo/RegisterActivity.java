package com.example.bkzalo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bkzalo.WebService.WebService;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, displayname, password;
    Button btn_register;

//    FirebaseAuth auth;
//    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        displayname = findViewById(R.id.displayname);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        //auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_displayname = displayname.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) ||
                        TextUtils.isEmpty(txt_displayname) ||
                        TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Your password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                } else {
                    String result = register(txt_username, txt_password, txt_displayname);
                    switch(result){
                        case "Username is exist!":
                            Toast.makeText(RegisterActivity.this, "Username is exist!", Toast.LENGTH_SHORT).show();
                            break;
                        case "Register Error!":
                            Toast.makeText(RegisterActivity.this, "Register Error!", Toast.LENGTH_SHORT).show();
                            break;
                        case "Register Sucessful!":
                            Toast.makeText(RegisterActivity.this, "You can login now!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            break;
                        default:
                            Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String register(final String username, String password, String displayname) {
        AsyncTask addUserTask = new AddUserTask().execute(username, password, displayname);
        try {
            String result = addUserTask.get().toString();

            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "Register Error!";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Register Error!";
        }

    }

    class AddUserTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = WebService.getInstance().Register(params);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }



}