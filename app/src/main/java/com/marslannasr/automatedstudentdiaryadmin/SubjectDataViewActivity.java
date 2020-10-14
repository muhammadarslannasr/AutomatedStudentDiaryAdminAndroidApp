package com.marslannasr.automatedstudentdiaryadmin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SubjectDataViewActivity extends AppCompatActivity {

    private RecyclerView teacher_recyclerViewID;
    private SubjectRecyclerViewAdapter subjectRecyclerViewAdapter;
    private DatabaseReference mDatabaseReference;
    List<Subject> subjectList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SearchView searchView;
    FirebaseDatabase mDatabase;
    private ConnectionDetect connectionDetect;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_data_view);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        connectionDetect = new ConnectionDetect(getApplicationContext());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getApplicationContext(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Subject");
        mDatabaseReference.keepSynced(true);
        subjectList = new ArrayList<>();
        teacher_recyclerViewID = (RecyclerView) findViewById(R.id.subject_recyclerViewID);
        teacher_recyclerViewID.setHasFixedSize(true);
        teacher_recyclerViewID.setLayoutManager(new LinearLayoutManager(SubjectDataViewActivity.this));

        if(connectionDetect.isConnected()) {
//            progressDialog.setMessage("Plz Wait!!");
//            progressDialog.show();
            mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Subject subject = dataSnapshot.getValue(Subject.class);
                    subjectList.add(subject);
                    subjectRecyclerViewAdapter = new SubjectRecyclerViewAdapter(SubjectDataViewActivity.this, subjectList);
                    teacher_recyclerViewID.setAdapter(subjectRecyclerViewAdapter);
                    subjectRecyclerViewAdapter.notifyDataSetChanged();
                    //progressDialog.dismiss();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }else {
            Toast.makeText(getApplicationContext(), "Plz Turn on Your Internt Connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
