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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TeacherManagFragment extends Fragment {

    private Button view_teachID,update_teachbtnID,delete_teachbtnID;
    private FloatingActionButton fab;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ConnectionDetect connectionDetect;
    private EditText teacherName_ID,teacherQulifiacationTextId,teacherUniTextID,teachermobilenumberTextId;
    private Spinner teacherspinner2genderId;
    private Button teacher_submitbuttonId;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorage;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReferrence;
    String genderListSpinner;
    private ProgressDialog mProgressDialog;
    public TeacherManagFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_manag, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        view_teachID = (Button) view.findViewById(R.id.view_teachID);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseReferrence = FirebaseDatabase.getInstance().getReference().child("TeacherData");
        update_teachbtnID = (Button) view.findViewById(R.id.update_teachbtnID);
        delete_teachbtnID = (Button) view.findViewById(R.id.delete_teachbtnID);
        view_teachID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),TeacherViewDataActivity.class));
            }
        });

        connectionDetect = new ConnectionDetect(getActivity());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getActivity(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }

        update_teachbtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),TeacherViewDataActivity.class));
            }
        });

        delete_teachbtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),TeacherViewDataActivity.class));

            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addTeacher();

            }
        });


        return view;
    }

    private void addTeacher() {


        dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.teacher_adddata, null);
        teacherName_ID = (EditText) view.findViewById(R.id.teacherName_ID);
        teacherQulifiacationTextId = (EditText) view.findViewById(R.id.teacherQulifiacationTextId);
        teacherUniTextID  = (EditText) view.findViewById(R.id.teacherUniTextID);
        teachermobilenumberTextId = (EditText) view.findViewById(R.id.teachermobilenumberTextId);
        teacherspinner2genderId = (Spinner) view.findViewById(R.id.teacherspinner2genderId);
        teacher_submitbuttonId = (Button) view.findViewById(R.id.teacher_submitbuttonId);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();



        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");
        genderList.add("Rather not say");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,genderList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherspinner2genderId.setAdapter(arrayAdapter);
        teacherspinner2genderId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                genderListSpinner = adapterView.getItemAtPosition(i).toString();

                //Toast.makeText(SignUp2Activity.this,genderListSpinner , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        teacher_submitbuttonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(connectionDetect.isConnected()) {
                    if(!teacherName_ID.getText().toString().isEmpty() && !teacherQulifiacationTextId.getText().toString().isEmpty() && !teacherUniTextID.getText().toString().isEmpty() && !teachermobilenumberTextId.getText().toString().isEmpty()) {
                        createTeacher();
                    }else {
                        Toast.makeText(getActivity(), "Plz Enter Data.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Turn Internet Connection on for Creating Accout!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void createTeacher() {

        mProgressDialog.setMessage("Adding Data....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        DatabaseReference newPost = mDatabaseReferrence.push();
        String ID = newPost.getKey();
        Map<String,String> data = new HashMap<>();
        data.put("teacher_name",teacherName_ID.getText().toString().trim());
        data.put("teacher_qualification",teacherQulifiacationTextId.getText().toString().trim());
        data.put("teacher_university",teacherUniTextID.getText().toString().trim());
        data.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
        data.put("userid",mUser.getUid());
        data.put("gender",genderListSpinner);
        data.put("phone_number",teachermobilenumberTextId.getText().toString().trim());
        data.put("teacher_nodeID",ID);
        newPost.setValue(data);
        Toast.makeText(getActivity(), "Teacher Data Added!", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
        dialog.dismiss();

    }


}
