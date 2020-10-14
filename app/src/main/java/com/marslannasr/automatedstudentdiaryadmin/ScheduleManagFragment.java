package com.marslannasr.automatedstudentdiaryadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ScheduleManagFragment extends Fragment {

    public ScheduleManagFragment() {
        // Required empty public constructor
    }

    private Button view_scheduleID;
    private FloatingActionButton fab;
    private ImageButton imageButton;
    private EditText postTitleEt;
    private Button submitPost;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ConnectionDetect connectionDetect;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorage;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReferrence;
    private ProgressDialog mProgressDialog;
    private final static int GALLERY_CODE = 1;
    private Uri mImageUri = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_manag, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabaseReferrence = FirebaseDatabase.getInstance().getReference().child("ScheduleData");
        fab = view.findViewById(R.id.fab);
        view_scheduleID = view.findViewById(R.id.view_scheduleID);

        view_scheduleID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),ScheduleDataViewActivity.class));
            }
        });

        connectionDetect = new ConnectionDetect(getActivity());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getActivity(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addSchedule();
            }
        });




        return view;
    }

    private void addSchedule() {

        dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.schedule_add_post_row, null);
        imageButton = view.findViewById(R.id.imageButton);
        postTitleEt = view.findViewById(R.id.postTitleEt);
        //descriptionEt = view.findViewById(R.id.descriptionEt);
        submitPost = view.findViewById(R.id.submitPost);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        submitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(connectionDetect.isConnected()) {
                    if(!postTitleEt.getText().toString().isEmpty()) {
                        createSchedule();
                    }else {
                        Toast.makeText(getActivity(), "Plz Enter Data.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Turn Internet Connection on for Creating Accout!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void createSchedule() {

        if(mImageUri != null){

            mProgressDialog.setMessage("Adding Data....");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();


            StorageReference storageReference = mStorage.child("schedule_images")
                    .child(mImageUri.getLastPathSegment());

            storageReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabaseReferrence.push();
                    String ID = newPost.getKey();
                    Map<String,String> data = new HashMap<>();
                    data.put("postTitleEt",postTitleEt.getText().toString().trim());
                    //data.put("descriptionEt",descriptionEt.getText().toString().trim());
                    data.put("imageButton",downloadurl.toString());
                    data.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    data.put("schedule_ID",ID);
                    newPost.setValue(data);
                    Toast.makeText(getActivity(), "Post Added!", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    dialog.dismiss();
//                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
//                    finish();
                }
            });
        }else {
            Toast.makeText(getActivity(), "Plz Put Image!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            if (data!=null) {
                mImageUri = data.getData();
                imageButton.setImageURI(mImageUri);
            }

        }
    }


}
