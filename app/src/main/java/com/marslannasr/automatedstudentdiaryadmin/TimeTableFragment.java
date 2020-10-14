package com.marslannasr.automatedstudentdiaryadmin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class TimeTableFragment extends Fragment {

    public TimeTableFragment() {
        // Required empty public constructor
    }


    private Button view_TimeTableID;
    private FloatingActionButton fab;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    DatePickerDialog dateDue;
    TimePickerDialog timeDue;
    private ConnectionDetect connectionDetect;
    private EditText datePicker_ID,timePickerId;
    private Spinner selectTeacherSpinnerId,selectSubjectSpinnerId,selectRoomSpinnerId;
    private Button submitbuttonId;
    private SimpleDateFormat dateFormatter, timeFormatter;
    Date dueDate, dueTime;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReferrence;
    DatabaseReference timeTableDatabaseReference;
    private ProgressDialog mProgressDialog;
    String teacherListSpinner;
    String subjectListSpinner;
    String roomListSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        view_TimeTableID = view.findViewById(R.id.view_TimeTableID);
        fab = view.findViewById(R.id.fab);
        mDatabase = FirebaseDatabase.getInstance();
        timeTableDatabaseReference = FirebaseDatabase.getInstance().getReference().child("TimeTable");
        connectionDetect = new ConnectionDetect(getActivity());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getActivity(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }

        view_TimeTableID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),TimeTableViewActivity.class));
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addTimeTable();
            }
        });

        return view;
    }

    private void addTimeTable() {

        dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.timetable_data, null);
        datePicker_ID = view.findViewById(R.id.datePicker_ID);
        timePickerId = view.findViewById(R.id.timePickerId);
        selectTeacherSpinnerId = view.findViewById(R.id.selectTeacherSpinnerId);
        selectSubjectSpinnerId = view.findViewById(R.id.selectSubjectSpinnerId);
        selectRoomSpinnerId = view.findViewById(R.id.selectRoomSpinnerId);
        submitbuttonId = view.findViewById(R.id.submitbuttonId);
        Calendar newCalendar = Calendar.getInstance();
        //mAuth=FirebaseAuth.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);

        datePicker_ID.setText(dateFormatter.format(newCalendar.getTime()));
        timePickerId.setText(timeFormatter.format(newCalendar.getTime()));
        dueDate = dueTime = newCalendar.getTime();


        datePicker_ID.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateDue.show();
                    }
                }
        );

        timePickerId.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeDue.show();
                    }
                }
        );

        timeDue = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newDateTime = Calendar.getInstance();
                newDateTime.set(newDateTime.get(Calendar.YEAR), newDateTime.get(Calendar.MONTH), newDateTime.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
                timePickerId.setText(timeFormatter.format(newDateTime.getTime()));
                dueTime = newDateTime.getTime();
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);


        dateDue = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                datePicker_ID.setText(dateFormatter.format(newDate.getTime()));
                dueDate = newDate.getTime();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


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



        List<String> roomtList = new ArrayList<>();
        roomtList.add("A201");
        roomtList.add("A202");
        roomtList.add("A203");
        roomtList.add("A204");
        roomtList.add("A205");
        roomtList.add("A106");
        roomtList.add("Lab5");
        roomtList.add("Lab6");
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,roomtList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectRoomSpinnerId.setAdapter(roomAdapter);
        selectRoomSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                roomListSpinner = adapterView.getItemAtPosition(i).toString();

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

                        createTimeTable();

                }else {
                    Toast.makeText(getActivity(), "Please Turn Internet Connection on for Creating Accout!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void createTimeTable() {

        mProgressDialog.setMessage("Adding Data....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        Calendar reminderDate = Calendar.getInstance();
        Calendar reminderTime = Calendar.getInstance();

        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(dueDate));
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(dueDate));
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(dueDate));
        int hour = Integer.parseInt(new SimpleDateFormat("HH").format(dueTime));
        int minutes = Integer.parseInt(new SimpleDateFormat("mm").format(dueTime));
        reminderDate.set(year, month - 1, day,hour,minutes);



        Date due = reminderDate.getTime();

        String date=due.toString();



        DatabaseReference newPost = timeTableDatabaseReference.push();
        String ID = newPost.getKey();
        Map<String,String> data = new HashMap<>();
        data.put("time_date",date);
        data.put("teacher_name",teacherListSpinner);
        data.put("subject",subjectListSpinner);
        data.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
        data.put("room_number",roomListSpinner);
        data.put("timetable_nodeID",ID);
        newPost.setValue(data);
        Toast.makeText(getActivity(), "Time Table Added!", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
        dialog.dismiss();
    }


}
