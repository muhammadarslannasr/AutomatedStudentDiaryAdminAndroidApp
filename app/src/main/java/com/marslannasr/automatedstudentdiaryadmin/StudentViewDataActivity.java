package com.marslannasr.automatedstudentdiaryadmin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
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

public class StudentViewDataActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private RecyclerView recyclerViewID;
    private StudentRecyclerViewAdapter studentRecyclerViewAdapter;
    List<Student> studentList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SearchView searchView;
    private StorageReference mStorage;
    FirebaseDatabase mDatabase;
    private ConnectionDetect connectionDetect;
    ProgressDialog progressDialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_data);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        connectionDetect = new ConnectionDetect(getApplicationContext());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getApplicationContext(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("StudentData");
        mDatabaseReference.keepSynced(true);
        studentList = new ArrayList<>();
        recyclerViewID = (RecyclerView) findViewById(R.id.student_recyclerViewID);
        recyclerViewID.setHasFixedSize(true);
        recyclerViewID.setLayoutManager(new LinearLayoutManager(StudentViewDataActivity.this));
        if(connectionDetect.isConnected()) {

            mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Student student = dataSnapshot.getValue(Student.class);
                    studentList.add(student);
                    studentRecyclerViewAdapter = new StudentRecyclerViewAdapter(StudentViewDataActivity.this, studentList);
                    recyclerViewID.setAdapter(studentRecyclerViewAdapter);
                    studentRecyclerViewAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.partsmenusearch,menu);
        final MenuItem myactionmenuItem = menu.findItem(R.id.mypartssearch);
        searchView = (SearchView) myactionmenuItem.getActionView();

        //onChangeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                myactionmenuItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Student> filteredmodelist = filter(studentList,newText);
                studentRecyclerViewAdapter.setfilter(filteredmodelist);
                return true;
            }
        });

        return true;
    }

    private List<Student> filter(List<Student> p1,String query){
        query = query.toLowerCase();
        final List<Student> filteredList = new ArrayList<>();
        for(Student model:p1){
            final String text = model.getStud_name().toLowerCase();
            if(text.contains(query)){
                filteredList.add(model);
            }
        }

        return filteredList;
    }
}
