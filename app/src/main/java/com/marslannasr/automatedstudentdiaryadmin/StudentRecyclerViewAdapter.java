package com.marslannasr.automatedstudentdiaryadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ArslanNasr on 5/22/2018.
 */

public class StudentRecyclerViewAdapter extends RecyclerView.Adapter<StudentRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Student> studentList;
    private String  profileImageUrl = null;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private Uri mImageUri = null;
    private final static int GALLERY_CODE = 1;
    private ConnectionDetect connectionDetect;
    private EditText firstusernameTextId,fathernameTextID,mobileNumberTextId,homeAddressEditTextId,rollnumberTextId;
    private Spinner spinnerdateId,spinnerbirthdayId,spinneryearId,spinner2genderId,districtPakistanId,monthSpinnerId,attendenceSpinnerId,resultSpinnerId,feesSpinnerId;
    private Button submitbuttonId;
//    private FirebaseAuth mAuth;
//    private FirebaseUser mUser;
    private CircleImageView profile_signupId;
    private StorageReference mStorage;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReferrence;
    String birthdaySpinner;
    String genderListSpinner;
    String districtListSpinner;
    String dateListSpinner;
    String yearListSpinner;
    String monthListSpinner;
    String attendenceListSpinner;
    String resultListSpinner;
    String feesListSpinner;
    private LayoutInflater inflater;

    public StudentRecyclerViewAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
        connectionDetect = new ConnectionDetect(context);
    }

    @NonNull
    @Override
    public StudentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.allusers_row,parent,false);
        return new StudentRecyclerViewAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentRecyclerViewAdapter.ViewHolder holder, int position) {
        Student student = studentList.get(position);
        String imageUrl = null;
        holder.stud_firstnameId.setText(student.getStud_name());
        holder.stud_fathernameID.setText(student.getStud_fname());
        holder.stud_dateofbitthID.setText(student.getMonth() + "/" + student.getDay() + "/" + student.getYear());
        holder.stud_genderID.setText(student.getGender());
        holder.stud_phonenumID.setText(student.getPhone_number());
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(student.getTimestamp())).getTime());
        holder.stud_dateID.setText(formattedDate);
        holder.stud_programID.setText(student.getProgram());
        holder.stud_addressID.setText(student.getHome_address());
        holder.stud_rollnumbernameId.setText(student.getStud_rollnumb());
        holder.stud_attendenceMonthID.setText(student.getMonth() + ":" + student.getStud_attendpercent());
        holder.stud_resultID.setText(student.getStud_result());
        holder.stud_feesRecordID.setText(student.getStud_feesRecord());
        imageUrl = student.getStud_image();
        //TODO: USE PICASOO library to load images
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.all_profile_image);
    }

    public void setfilter(List<Student> itemList){
        studentList = new ArrayList<>();
        studentList.addAll(itemList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView all_profile_image;
        public TextView stud_firstnameId,stud_fathernameID,stud_dateofbitthID,stud_genderID,stud_phonenumID,stud_addressID,stud_programID,stud_dateID,stud_rollnumbernameId,stud_attendenceMonthID,stud_resultID,stud_feesRecordID;
        public Button editButton,deleteButton;
        public DatabaseReference databaseReference;
        public FirebaseAuth mAuth;
        public FirebaseUser mUser;
        private ProgressDialog mProgressDialog;
        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);

            context = ctx;
            mProgressDialog = new ProgressDialog(context);
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            all_profile_image = (CircleImageView) itemView.findViewById(R.id.all_profile_image);
            stud_firstnameId = (TextView) itemView.findViewById(R.id.stud_firstnameId);
            stud_fathernameID = (TextView) itemView.findViewById(R.id.stud_fathernameID);
            stud_dateofbitthID = (TextView) itemView.findViewById(R.id.stud_dateofbitthID);
            stud_genderID = (TextView) itemView.findViewById(R.id.stud_genderID);
            stud_phonenumID = (TextView) itemView.findViewById(R.id.stud_phonenumID);
            stud_addressID = (TextView) itemView.findViewById(R.id.stud_addressID);
            stud_programID = (TextView) itemView.findViewById(R.id.stud_programID);
            stud_dateID = (TextView) itemView.findViewById(R.id.stud_dateID);
            stud_rollnumbernameId = (TextView) itemView.findViewById(R.id.stud_rollnumbernameId);
            stud_attendenceMonthID = (TextView) itemView.findViewById(R.id.stud_attendenceMonthID);
            stud_resultID  = (TextView) itemView.findViewById(R.id.stud_resultID);
            stud_feesRecordID = (TextView) itemView.findViewById(R.id.stud_feesRecordID);
            editButton = (Button) itemView.findViewById(R.id.editButton);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    Student student = studentList.get(position);

                    //Toast.makeText(ctx, student.getStud_name(), Toast.LENGTH_SHORT).show();

                }
            });


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialogBuilder = new AlertDialog.Builder(context);

                    inflater = LayoutInflater.from(context);
                    view = inflater.inflate(R.layout.confirmation_dialog, null);

                    Button noButton = (Button) view.findViewById(R.id.noButton);
                    Button yesButton = (Button) view.findViewById(R.id.yesButton);

                    alertDialogBuilder.setView(view);
                    dialog = alertDialogBuilder.create();
                    dialog.show();


                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Delete Data
                            mAuth=FirebaseAuth.getInstance();
                            mUser=mAuth.getCurrentUser();
                            int position = getAdapterPosition();
                            Student s = studentList.get(position);
                            databaseReference = FirebaseDatabase.getInstance().getReference("StudentData").child(s.getStud_id());
                            databaseReference.removeValue();
                            studentList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();


                        }
                    });
                }
            });



            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogBuilder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    view = inflater.inflate(R.layout.activity_sign_up2, null);
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

                    int position = getAdapterPosition();
                    Student student = studentList.get(position);
                    //First Name
                    firstusernameTextId.setText(student.getStud_name());
                    firstusernameTextId.setSelection(firstusernameTextId.getText().length());

                    //Second Name
                    fathernameTextID.setText(student.getStud_fname());
                    fathernameTextID.setSelection(fathernameTextID.getText().length());

                    //Roll Number Student
                    rollnumberTextId.setText(student.getStud_rollnumb());
                    rollnumberTextId.setSelection(rollnumberTextId.getText().length());

                    //Image of User
                    //TODO: USE PICASOO library to load images
                    Picasso.with(context)
                            .load(student.getStud_image())
                            .into(profile_signupId);

                   //Phone Number
                    mobileNumberTextId.setText(student.getPhone_number());
                    mobileNumberTextId.setSelection(mobileNumberTextId.getText().length());

                    //Home Address
                    homeAddressEditTextId.setText(student.getHome_address());
                    homeAddressEditTextId.setSelection(homeAddressEditTextId.getText().length());

                    dialogBuilder.setView(view);
                    dialog = dialogBuilder.create();
                    dialog.show();


//                    profile_signupId.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent galleryIntent = new Intent();
//                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                            galleryIntent.setType("image/*");
//                            ((Activity) context).startActivityForResult(galleryIntent,GALLERY_CODE);
//                        }
//                    });

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

                    ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,yearList);
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
                            context, android.R.layout.simple_spinner_item,
                            birthday);
                    spinnerbirthdayId.setAdapter(friendAdapter);

                    ArrayAdapter<String> subFriendAdapter = new ArrayAdapter<String>(
                            context, android.R.layout.simple_spinner_item,
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
                                    context,
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,genderList);
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
                    ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,monthList);
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
                    ArrayAdapter<String> attendenceAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,attendenceList);
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
                    ArrayAdapter<String> resultAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,resultList);
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
                    ArrayAdapter<String> feesAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,feesList);
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
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,districtPakistanList);
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
                                    Toast.makeText(context, "Plz Enter Data.", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(context, "Please Turn Internet Connection on for Creating Accout!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                }

            });

        }

        private void createStudent() {

            //Toast.makeText(context, "Create Account MEthod", Toast.LENGTH_SHORT).show();

            mProgressDialog.setMessage("Adding Data....");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            int position = getAdapterPosition();
            final Student student = studentList.get(position);
//            StorageReference storageReference = mStorage.child("students_images")
//                    .child(student.getStud_image());
//
//            storageReference.putFile(Uri.parse(student.getStud_image())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri downloadurl = taskSnapshot.getDownloadUrl();
//                    //DatabaseReference newPost = mDatabaseReferrence.push();
//                    //String ID = newPost.getKey();
//                    DatabaseReference databaseReference;
//                    databaseReference = FirebaseDatabase.getInstance().getReference("StudentData").child(student.getStud_id());
//                    String ID = databaseReference.getKey();
//                    Map<String,String> data = new HashMap<>();
//                    data.put("stud_name",firstusernameTextId.getText().toString().trim());
//                    student.setStud_name(firstusernameTextId.getText().toString().trim());
//                    data.put("stud_fname",fathernameTextID.getText().toString().trim());
//                    student.setStud_fname(fathernameTextID.getText().toString().trim());
//                    data.put("stud_image",downloadurl.toString());
//                    data.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
//                    student.setTimestamp(String.valueOf(java.lang.System.currentTimeMillis()));
//                    data.put("userid",mUser.getUid());
//                    data.put("month",birthdaySpinner);
//                    student.setMonth(birthdaySpinner);
//                    data.put("day",dateListSpinner);
//                    student.setDay(dateListSpinner);
//                    data.put("year",yearListSpinner);
//                    student.setYear(yearListSpinner);
//                    data.put("gender",genderListSpinner);
//                    student.setGender(genderListSpinner);
//                    data.put("phone_number",mobileNumberTextId.getText().toString().trim());
//                    student.setPhone_number(mobileNumberTextId.getText().toString().trim());
//                    data.put("home_address",homeAddressEditTextId.getText().toString().trim());
//                    student.setHome_address(homeAddressEditTextId.getText().toString().trim());
//                    data.put("stud_rollnumb",rollnumberTextId.getText().toString().trim());
//                    student.setStud_rollnumb(rollnumberTextId.getText().toString().trim());
//                    data.put("stud_attendmonth",monthListSpinner);
//                    student.setStud_attendmonth(monthListSpinner);
//                    data.put("stud_attendpercent",attendenceListSpinner);
//                    student.setStud_attendpercent(attendenceListSpinner);
//                    data.put("stud_result",resultListSpinner);
//                    student.setStud_result(resultListSpinner);
//                    data.put("stud_feesRecord",feesListSpinner);
//                    student.setStud_feesRecord(feesListSpinner);
//                    data.put("program",districtListSpinner);
//                    student.setProgram(districtListSpinner);
//                    data.put("stud_id",ID);
//                    databaseReference.setValue(data);
//                    notifyItemChanged(getAdapterPosition(),student);
//                    Toast.makeText(context, "Data Updated!", Toast.LENGTH_SHORT).show();
//                    mProgressDialog.dismiss();
//                    dialog.dismiss();
////                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
////                    finish();
//                }
//            });

            DatabaseReference databaseReference;
                    databaseReference = FirebaseDatabase.getInstance().getReference("StudentData").child(student.getStud_id());
                    String ID = databaseReference.getKey();
                    Map<String,String> data = new HashMap<>();
                    data.put("stud_name",firstusernameTextId.getText().toString().trim());
                    student.setStud_name(firstusernameTextId.getText().toString().trim());
                    data.put("stud_fname",fathernameTextID.getText().toString().trim());
                    student.setStud_fname(fathernameTextID.getText().toString().trim());
                    data.put("stud_image",student.getStud_image());
                    data.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    student.setTimestamp(String.valueOf(java.lang.System.currentTimeMillis()));
                    data.put("userid",mUser.getUid());
                    data.put("month",birthdaySpinner);
                    student.setMonth(birthdaySpinner);
                    data.put("day",dateListSpinner);
                    student.setDay(dateListSpinner);
                    data.put("year",yearListSpinner);
                    student.setYear(yearListSpinner);
                    data.put("gender",genderListSpinner);
                    student.setGender(genderListSpinner);
                    data.put("phone_number",mobileNumberTextId.getText().toString().trim());
                    student.setPhone_number(mobileNumberTextId.getText().toString().trim());
                    data.put("home_address",homeAddressEditTextId.getText().toString().trim());
                    student.setHome_address(homeAddressEditTextId.getText().toString().trim());
                    data.put("stud_rollnumb",rollnumberTextId.getText().toString().trim());
                    student.setStud_rollnumb(rollnumberTextId.getText().toString().trim());
                    data.put("stud_attendmonth",monthListSpinner);
                    student.setStud_attendmonth(monthListSpinner);
                    data.put("stud_attendpercent",attendenceListSpinner);
                    student.setStud_attendpercent(attendenceListSpinner);
                    data.put("stud_result",resultListSpinner);
                    student.setStud_result(resultListSpinner);
                    data.put("stud_feesRecord",feesListSpinner);
                    student.setStud_feesRecord(feesListSpinner);
                    data.put("program",districtListSpinner);
                    student.setProgram(districtListSpinner);
                    data.put("stud_id",ID);
                    databaseReference.setValue(data);
                    notifyItemChanged(getAdapterPosition(),student);
                    Toast.makeText(context, "Data Updated!", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    dialog.dismiss();
//                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
//                    finish();
        }





    }




}
