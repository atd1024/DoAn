package com.example.bkzalo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
//import com.example.bkzalo.Fragment.ChatsFragment;
//import com.example.bkzalo.Fragment.NewsFragment;
//import com.example.bkzalo.Fragment.ProfileFragment;
import com.example.bkzalo.Fragment.NewsFragment;
import com.example.bkzalo.Fragment.UserFragment;
import com.example.bkzalo.Model.User;
import com.example.bkzalo.WebService.WebService;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    public static User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        int currentUserID = getIntent().getIntExtra("currentUserID",0);
        current_user = getUserByID(currentUserID);

        profile_image.setImageResource(R.mipmap.ic_launcher);

        // ***** khi có thay đổi của bảng User (đổi avatar) *****

//        User user = getUserByID();
        username.setText(current_user.getDisplayname()); // display the user's name
//        if (user.getImageURL().equals("default")){
//            profile_image.setImageResource(R.mipmap.ic_launcher);
//        } else {
//            Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
//        }

        // ******************************************************

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new NewsFragment(), "News");
        //viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UserFragment(), "Users");
        //viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                //FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        //fields
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        //constructor
        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void setStatus(String status){
        //reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        //reference.updateChildren(hashMap);
    }

    private User getUserByID(int idUser){
        AsyncTask getUserByID = new GetUserByIDTask().execute(idUser+"");
        try {
            User user =(User)getUserByID.get();
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

    @Override
    protected void onResume() {     // when this activity is running
        super.onResume();
        setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus("offline");       // when this activity is stopped
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

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
        }
    }
}

