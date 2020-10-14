package com.marslannasr.automatedstudentdiaryadmin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

public class TeacherViewDataActivity extends AppCompatActivity {

    private RecyclerView teacher_recyclerViewID;
    private TeacherRecyclerViewAdapter teacherRecyclerViewAdapter;
    private DatabaseReference mDatabaseReference;
    List<Teacher> teacherList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SearchView searchView;
    FirebaseDatabase mDatabase;
    private ConnectionDetect connectionDetect;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_data);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        connectionDetect = new ConnectionDetect(getApplicationContext());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getApplicationContext(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("TeacherData");
        mDatabaseReference.keepSynced(true);
        teacherList = new ArrayList<>();
        teacher_recyclerViewID = (RecyclerView) findViewById(R.id.teacher_recyclerViewID);
        teacher_recyclerViewID.setHasFixedSize(true);
        teacher_recyclerViewID.setLayoutManager(new LinearLayoutManager(TeacherViewDataActivity.this));
        if(connectionDetect.isConnected()) {
//            progressDialog.setMessage("Plz Wait!!");
//            progressDialog.show();
            mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Teacher teacher = dataSnapshot.getValue(Teacher.class);
                    teacherList.add(teacher);
                    teacherRecyclerViewAdapter = new TeacherRecyclerViewAdapter(TeacherViewDataActivity.this, teacherList);
                    teacher_recyclerViewID.setAdapter(teacherRecyclerViewAdapter);
                    teacherRecyclerViewAdapter.notifyDataSetChanged();
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
                final List<Teacher> filteredmodelist = filter(teacherList,newText);
                teacherRecyclerViewAdapter.setfilter(filteredmodelist);
                return true;
            }
        });

        return true;
    }

    private List<Teacher> filter(List<Teacher> p1,String query){
        query = query.toLowerCase();
        final List<Teacher> filteredList = new ArrayList<>();
        for(Teacher model:p1){
            final String text = model.getTeacher_name().toLowerCase();
            if(text.contains(query)){
                filteredList.add(model);
            }
        }

        return filteredList;
    }

    }

