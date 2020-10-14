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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDataViewActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private RecyclerView recyclerViewID;
    private ScheduleRecyclerVireAdapter scheduleRecyclerVireAdapters;
    List<Schedule> scheduleList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SearchView searchView;
    private StorageReference mStorage;
    FirebaseDatabase mDatabase;
    private ConnectionDetect connectionDetect;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_data_view);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        connectionDetect = new ConnectionDetect(getApplicationContext());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getApplicationContext(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("ScheduleData");
        mDatabaseReference.keepSynced(true);
        scheduleList = new ArrayList<>();
        recyclerViewID = (RecyclerView) findViewById(R.id.schedule_recyclerViewID);
        recyclerViewID.setHasFixedSize(true);
        recyclerViewID.setLayoutManager(new LinearLayoutManager(ScheduleDataViewActivity.this));
        if(connectionDetect.isConnected()) {

            mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    scheduleList.add(schedule);
                    scheduleRecyclerVireAdapters = new ScheduleRecyclerVireAdapter(ScheduleDataViewActivity.this, scheduleList);
                    recyclerViewID.setAdapter(scheduleRecyclerVireAdapters);
                    scheduleRecyclerVireAdapters.notifyDataSetChanged();
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

