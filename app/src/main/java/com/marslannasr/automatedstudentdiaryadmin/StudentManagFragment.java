package com.marslannasr.automatedstudentdiaryadmin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentManagFragment extends Fragment {


    public StudentManagFragment() {
        // Required empty public constructor
    }
    private DatabaseReference mDatabaseReference;
    private RecyclerView recyclerViewID;
    private StudentRecyclerViewAdapter studentRecyclerViewAdapter;
    List<Student> studentList;
    private SearchView searchView;
    ProgressDialog progressDialog;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Uri mImageUri = null;
    private final static int GALLERY_CODE = 1;
    private ConnectionDetect connectionDetect;
    FloatingActionButton floatingActionButton;
    private EditText firstusernameTextId,fathernameTextID,mobileNumberTextId,homeAddressEditTextId,rollnumberTextId;
    private Spinner spinnerdateId,spinnerbirthdayId,spinneryearId,spinner2genderId,districtPakistanId,monthSpinnerId,attendenceSpinnerId,resultSpinnerId,feesSpinnerId;
    private Button submitbuttonId;
    private CircleImageView profile_signupId;
    String birthdaySpinner;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorage;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReferrence;
    private ProgressDialog mProgressDialog;
    String genderListSpinner;
    String districtListSpinner;
    String dateListSpinner;
    String yearListSpinner;
    String monthListSpinner;
    String attendenceListSpinner;
    String resultListSpinner;
    String feesListSpinner;
    private Button view_studID,update_studbtnID,search_studbtnID,deactivate_studbtnID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_manag, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabaseReferrence = FirebaseDatabase.getInstance().getReference().child("StudentData");
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstudent();
            }
        });

        connectionDetect = new ConnectionDetect(getActivity());
        if(connectionDetect.isConnected()){

        }else {
            Toast.makeText(getActivity(), "Turn on your internet connection before Creating Account", Toast.LENGTH_SHORT).show();
        }

        view_studID = view.findViewById(R.id.view_studID);
        view_studID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),StudentViewDataActivity.class));
            }
        });


        update_studbtnID = view.findViewById(R.id.update_studbtnID);
        update_studbtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),StudentViewDataActivity.class));
            }
        });

        update_studbtnID = view.findViewById(R.id.update_studbtnID);
        update_studbtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),StudentViewDataActivity.class));
            }
        });

        search_studbtnID = view.findViewById(R.id.search_studbtnID);
        search_studbtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),StudentViewDataActivity.class));
            }
        });

        deactivate_studbtnID = view.findViewById(R.id.deactivate_studbtnID);
        deactivate_studbtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),StudentViewDataActivity.class));
            }
        });
        return view;
    }

    private void addstudent() {

        dialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.activity_sign_up2, null);
        firstusernameTextId = (EditText) view.findViewById(R.id.firstusernameTextId);
        fathernameTextID = (EditText) view.findViewById(R.id.fathernameTextID);
        rollnumberTextId = (EditText) view.findViewById(R.id.rollnumberTextId);
        mobileNumberTextId = (EditText) view.findViewById(R.id.mobileNumberTextId);
        homeAddressEditTextId = (EditText)  view.findViewById(R.id.homeAddressEditTextId);
        spinnerdateId = (Spinner) view.findViewById(R.id.spinnerdateId);
        spinnerbirthdayId = (Spinner) view.findViewById(R.id.spinnerbirthdayId);
        spinneryearId = (Spinner) view.findViewById(R.id.spinneryearId);
        spinner2genderId = (Spinner) view.findViewById(R.id.spinner2genderId);
        districtPakistanId  = (Spinner) view.findViewById(R.id.districtPakistanId);
        monthSpinnerId = (Spinner) view.findViewById(R.id.monthSpinnerId);
        attendenceSpinnerId = (Spinner) view.findViewById(R.id.attendenceSpinnerId);
        resultSpinnerId = (Spinner) view.findViewById(R.id.resultSpinnerId);
        feesSpinnerId = (Spinner) view.findViewById(R.id.feesSpinnerId);
        profile_signupId = (CircleImageView) view.findViewById(R.id.profile_signupId);
        submitbuttonId = (Button) view.findViewById(R.id.submitbuttonId);


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();


        profile_signupId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });


        List<String> yearList = new ArrayList<>();
        yearList.add("1960");
        yearList.add("1961");
        yearList.add("1962");
        yearList.add("1963");
        yearList.add("1964");
        yearList.add("1965");
        yearList.add("1966");
        yearList.add("1967");
        yearList.add("1968");
        yearList.add("1969");
        yearList.add("1970");
        yearList.add("1971");
        yearList.add("1972");
        yearList.add("1973");
        yearList.add("1974");
        yearList.add("1975");
        yearList.add("1976");
        yearList.add("1977");
        yearList.add("1978");
        yearList.add("1979");
        yearList.add("1980");
        yearList.add("1981");
        yearList.add("1982");
        yearList.add("1983");
        yearList.add("1984");
        yearList.add("1985");
        yearList.add("1986");
        yearList.add("1987");
        yearList.add("1988");
        yearList.add("1989");
        yearList.add("1990");
        yearList.add("1991");
        yearList.add("1992");
        yearList.add("1993");
        yearList.add("1994");
        yearList.add("1995");
        yearList.add("1996");
        yearList.add("1997");
        yearList.add("1998");
        yearList.add("1999");
        yearList.add("2000");
        yearList.add("2001");
        yearList.add("2002");
        yearList.add("2003");
        yearList.add("2004");
        yearList.add("2005");
        yearList.add("2006");
        yearList.add("2007");
        yearList.add("2008");
        yearList.add("2009");
        yearList.add("2010");
        yearList.add("2011");
        yearList.add("2012");
        yearList.add("2013");
        yearList.add("2014");
        yearList.add("2015");
        yearList.add("2016");
        yearList.add("2017");
        yearList.add("2018");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneryearId.setAdapter(yearAdapter);
        spinneryearId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                yearListSpinner = adapterView.getItemAtPosition(i).toString();

                //Toast.makeText(SignUp2Activity.this, birthdaySpinner, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final List<String> birthday = new ArrayList<>();
        final List<String> dateList = new ArrayList<>();

        birthday.add("January");
        birthday.add("Febuaray");
        birthday.add("March");
        birthday.add("April");
        birthday.add("May");
        birthday.add("June");
        birthday.add("July");
        birthday.add("August");
        birthday.add("September");
        birthday.add("October");
        birthday.add("November");
        birthday.add("December");

        final ArrayAdapter<String> friendAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                birthday);
        spinnerbirthdayId.setAdapter(friendAdapter);

        ArrayAdapter<String> subFriendAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                dateList);
        spinnerdateId.setAdapter(subFriendAdapter);


        spinnerbirthdayId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long id) {
                //((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
                birthdaySpinner  = birthday.get(position);
                //Toast.makeText(SignUp2Activity.this, birthdaySpinner, Toast.LENGTH_SHORT).show();
                resetFriend(birthdaySpinner);

            }

            private void resetFriend(String friendName) {
                dateList.removeAll(dateList);
                if (friendName.equals("January")) {
                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                    dateList.add("31");
                } else if (friendName.equals("Febuaray")) {
                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                } else if(friendName.equals("March")) {
                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                    dateList.add("31");
                }else if(friendName.equals("April")) {
                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                }else if(friendName.equals("May")){

                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                    dateList.add("31");
                }else if(friendName.equals("June")){

                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");

                }else if(friendName.equals("July")){

                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                    dateList.add("31");
                }else if(friendName.equals("August")){

                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                    dateList.add("31");
                }else if(friendName.equals("September")){

                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                }else if(friendName.equals("October")){

                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                    dateList.add("31");
                }else if(friendName.equals("November")){

                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                }
                else if(friendName.equals("December")){

                    dateList.add("1");
                    dateList.add("2");
                    dateList.add("3");
                    dateList.add("4");
                    dateList.add("5");
                    dateList.add("6");
                    dateList.add("7");
                    dateList.add("8");
                    dateList.add("9");
                    dateList.add("10");
                    dateList.add("11");
                    dateList.add("12");
                    dateList.add("13");
                    dateList.add("14");
                    dateList.add("15");
                    dateList.add("16");
                    dateList.add("17");
                    dateList.add("18");
                    dateList.add("19");
                    dateList.add("20");
                    dateList.add("21");
                    dateList.add("22");
                    dateList.add("23");
                    dateList.add("24");
                    dateList.add("25");
                    dateList.add("26");
                    dateList.add("27");
                    dateList.add("28");
                    dateList.add("29");
                    dateList.add("30");
                    dateList.add("31");
                }
                ArrayAdapter<String> subFriendAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_spinner_item, dateList);
                spinnerdateId.setAdapter(subFriendAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerdateId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                dateListSpinner = dateList.get(i);

                //Toast.makeText(SignUp2Activity.this, dateListSpinner, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");
        genderList.add("Rather not say");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,genderList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2genderId.setAdapter(arrayAdapter);
        spinner2genderId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

      //Month Spinner
        List<String> monthList = new ArrayList<>();
        monthList.add("January");
        monthList.add("Febuaray");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,monthList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinnerId.setAdapter(monthAdapter);
        monthSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                monthListSpinner = adapterView.getItemAtPosition(i).toString();

                //Toast.makeText(SignUp2Activity.this,genderListSpinner , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Attendence Spinner
        List<String> attendenceList = new ArrayList<>();
        attendenceList.add("30%");
        attendenceList.add("40%");
        attendenceList.add("50%");
        attendenceList.add("60%");
        attendenceList.add("65%");
        attendenceList.add("70%");
        attendenceList.add("75%");
        attendenceList.add("80%");
        attendenceList.add("85%");
        attendenceList.add("90%");
        attendenceList.add("95%");
        attendenceList.add("100%");
        ArrayAdapter<String> attendenceAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,attendenceList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attendenceSpinnerId.setAdapter(attendenceAdapter);
        attendenceSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                attendenceListSpinner = adapterView.getItemAtPosition(i).toString();

                //Toast.makeText(SignUp2Activity.this,genderListSpinner , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Result Spinner
        List<String> resultList = new ArrayList<>();
        resultList.add("30%");
        resultList.add("40%");
        resultList.add("50%");
        resultList.add("60%");
        resultList.add("65%");
        resultList.add("70%");
        resultList.add("75%");
        resultList.add("80%");
        resultList.add("85%");
        resultList.add("90%");
        resultList.add("95%");
        resultList.add("100%");
        ArrayAdapter<String> resultAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,resultList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resultSpinnerId.setAdapter(resultAdapter);
        resultSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                resultListSpinner = adapterView.getItemAtPosition(i).toString();

                //Toast.makeText(SignUp2Activity.this,genderListSpinner , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //Fess Submission Spinner
        List<String> feesList = new ArrayList<>();
        feesList.add("Not Yet Now tell Later");
        feesList.add("Submitted");
        feesList.add("Not Submitted");
        ArrayAdapter<String> feesAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,feesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feesSpinnerId.setAdapter(feesAdapter);
        feesSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                feesListSpinner = adapterView.getItemAtPosition(i).toString();

                //Toast.makeText(SignUp2Activity.this,genderListSpinner , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        List<String> districtPakistanList = new ArrayList<>();
        districtPakistanList.add("FSC Pre_Engineering");
        districtPakistanList.add("FSC Pre_Medical");
        districtPakistanList.add("ICS");
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,districtPakistanList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtPakistanId.setAdapter(districtAdapter);
        districtPakistanId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                districtListSpinner = adapterView.getItemAtPosition(i).toString();

                //Toast.makeText(SignUp2Activity.this,districtListSpinner , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submitbuttonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(connectionDetect.isConnected()) {
                    if(!firstusernameTextId.getText().toString().isEmpty() && !fathernameTextID.getText().toString().isEmpty() && !mobileNumberTextId.getText().toString().isEmpty() && !homeAddressEditTextId.getText().toString().isEmpty() && !rollnumberTextId.getText().toString().isEmpty() ) {
                        createStudent();
                    }else {
                        Toast.makeText(getActivity(), "Plz Enter Data.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Turn Internet Connection on for Creating Accout!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void createStudent() {

        if(mImageUri != null){

            mProgressDialog.setMessage("Adding Data....");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();


            StorageReference storageReference = mStorage.child("students_images")
                    .child(mImageUri.getLastPathSegment());

            storageReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabaseReferrence.push();
                    String ID = newPost.getKey();
                    Map<String,String> data = new HashMap<>();
                    data.put("stud_name",firstusernameTextId.getText().toString().trim());
                    data.put("stud_fname",fathernameTextID.getText().toString().trim());
                    data.put("stud_image",downloadurl.toString());
                    data.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    data.put("userid",mUser.getUid());
                    data.put("month",birthdaySpinner);
                    data.put("day",dateListSpinner);
                    data.put("year",yearListSpinner);
                    data.put("gender",genderListSpinner);
                    data.put("phone_number",mobileNumberTextId.getText().toString().trim());
                    data.put("home_address",homeAddressEditTextId.getText().toString().trim());
                    data.put("stud_rollnumb",rollnumberTextId.getText().toString().trim());
                    data.put("stud_attendmonth",monthListSpinner);
                    data.put("stud_attendpercent",attendenceListSpinner);
                    data.put("stud_result",resultListSpinner);
                    data.put("stud_feesRecord",feesListSpinner);
                    data.put("program",districtListSpinner);
                    data.put("stud_id",ID);
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
                profile_signupId.setImageURI(mImageUri);
            }

        }
    }
}
