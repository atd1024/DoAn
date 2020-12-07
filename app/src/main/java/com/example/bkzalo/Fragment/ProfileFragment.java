package com.example.bkzalo.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bkzalo.Adapter.UserAdapter;
import com.example.bkzalo.ChatActivity;
import com.example.bkzalo.MainActivity;
import com.example.bkzalo.Model.User;
import com.example.bkzalo.R;
import com.example.bkzalo.WebService.WebService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {



    CircleImageView image_profile;
    TextView displayname;

    User current_user;

    StorageReference storageReference;
    private static final int IMAGE_RESQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.profile_image);
        displayname = view.findViewById(R.id.displayname);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        current_user = MainActivity.current_user;

        displayname.setText(current_user.getDisplayname());

        // load avatar
        if(current_user.getImageURL().equals("default")){
            image_profile.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(getContext()).load(current_user.getImageURL()).into(image_profile);
        }

        // quét DB, có update thì load avatar
            // update avatar xong thì update lại thằng current user bên MainActivity
        // **********************************
        UpdateImage updateImage = new UpdateImage();
        updateImage.start();

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        return view;
    }

    private void openImage(){       // mở ảnh từ thiết bị
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_RESQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        // update imageURL vào current user
                        int current_user_id = current_user.getID();
                        new SetProfileImage().execute(current_user_id+"", mUri);
                        // update lại current user
                        MainActivity.current_user = getUserByID(current_user_id);
                        current_user = MainActivity.current_user;
                        //************************************
                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_RESQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
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

    class SetProfileImage extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            WebService.getInstance().SetProfileImage(params);
            return null;
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

    class UpdateImage extends Thread {
                @Override
                public void run() {
                    while (true) {
                        AsyncTask getStateTask = new GetStateTask().execute();
                        try {
                            String result = getStateTask.get().toString();
                            if (result.equals("true")) {
                                // set lại user table state
                                new SetStateTask().execute();
                                // update lại phần UI
                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                threadHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(getContext()).load(current_user.getImageURL()).into(image_profile);
                                    }
                                });
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                }
        }
    }
}