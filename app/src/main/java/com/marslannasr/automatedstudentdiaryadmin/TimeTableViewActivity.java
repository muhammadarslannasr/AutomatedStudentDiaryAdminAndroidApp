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

public class TimeTableViewActivity extends AppCompatActivity {

    private RecyclerView teacher_recyclerViewID;
    private TimeTableRecyclerViewAdapter timeTableRecyclerViewAdapter;
    private DatabaseReference mDatabaseReference;
    List<TimeTable> timeTableList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SearchView searchView;
    FirebaseDatabase mDatabase;
    private ConnectionDetect connectionDetect;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_view);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        connectionDetect = new ConnectionDetect(getApplicationContext());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getApplicationContext(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("TimeTable");
        mDatabaseReference.keepSynced(true);
        timeTableList = new ArrayList<>();
        teacher_recyclerViewID = (RecyclerView) findViewById(R.id.timetable_recyclerViewID);
        teacher_recyclerViewID.setHasFixedSize(true);
        teacher_recyclerViewID.setLayoutManager(new LinearLayoutManager(TimeTableViewActivity.this));
        if(connectionDetect.isConnected()) {
//            progressDialog.setMessage("Plz Wait!!");
//            progressDialog.show();
            mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    TimeTable timeTable = dataSnapshot.getValue(TimeTable.class);
                    timeTableList.add(timeTable);
                    timeTableRecyclerViewAdapter = new TimeTableRecyclerViewAdapter(TimeTableViewActivity.this, timeTableList);
                    teacher_recyclerViewID.setAdapter(timeTableRecyclerViewAdapter);
                    timeTableRecyclerViewAdapter.notifyDataSetChanged();
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
