package com.marslannasr.automatedstudentdiaryadmin;


import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectManagFragment extends Fragment {


    private Button view_subjectID;
    private FloatingActionButton fab;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ConnectionDetect connectionDetect;
    private Spinner selectTeacherSpinnerId,selectSubjectSpinnerId;
    private Button submitbuttonId;
    DatabaseReference mDatabaseReferrence;
    DatabaseReference subjectDatabaseReference;
    private ProgressDialog mProgressDialog;
    String teacherListSpinner;
    String subjectListSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_manag, container, false);
        view_subjectID = view.findViewById(R.id.view_subjectID);
        fab = view.findViewById(R.id.fab);
        mProgressDialog = new ProgressDialog(getActivity());
        subjectDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Subject");

        connectionDetect = new ConnectionDetect(getActivity());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getActivity(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }


        view_subjectID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),SubjectDataViewActivity.class));
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addSubject();
            }
        });

        return view;
    }

    private void addSubject() {

        dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.subject_adddata, null);
        selectTeacherSpinnerId = view.findViewById(R.id.selectTeacherSpinnerId);
        selectSubjectSpinnerId = view.findViewById(R.id.selectSubjectSpinnerId);
        submitbuttonId = view.findViewById(R.id.submitbuttonId);


        mDatabaseReferrence = FirebaseDatabase.getInstance().getReference().child("TeacherData");
        mDatabaseReferrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> teacherList = new ArrayList<String>();
                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("teacher_name").getValue(String.class);
                    teacherList.add(areaName);
                }

                if(getActivity() != null){
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, teacherList);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectTeacherSpinnerId.setAdapter(areasAdapter);

                    selectTeacherSpinnerId.setAdapter(areasAdapter);
                    selectTeacherSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                            teacherListSpinner = adapterView.getItemAtPosition(i).toString();

                            //Toast.makeText(SignUp2Activity.this,genderListSpinner , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        List<String> subjectList = new ArrayList<>();
        subjectList.add("Chemistry");
        subjectList.add("Physics");
        subjectList.add("Computer Science");
        subjectList.add("Islamiat");
        subjectList.add("Pakistan Studies");
        subjectList.add("Biology");
        subjectList.add("Math");
        subjectList.add("English");
        subjectList.add("Urdu");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,subjectList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSubjectSpinnerId.setAdapter(arrayAdapter);
        selectSubjectSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                subjectListSpinner = adapterView.getItemAtPosition(i).toString();

                //Toast.makeText(SignUp2Activity.this,genderListSpinner , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();


        submitbuttonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(connectionDetect.isConnected()) {

                    createSubject();

                }else {
                    Toast.makeText(getActivity(), "Please Turn Internet Connection on for Creating Accout!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void createSubject() {

        mProgressDialog.setMessage("Adding Data....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        DatabaseReference newPost = subjectDatabaseReference.push();
        String ID = newPost.getKey();
        Map<String,String> data = new HashMap<>();
        data.put("teacher_name",teacherListSpinner);
        data.put("subject_name",subjectListSpinner);
        data.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
        data.put("subject_nodeID",ID);
        newPost.setValue(data);
        Toast.makeText(getActivity(), "Subject Added!", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
        dialog.dismiss();
    }

}
