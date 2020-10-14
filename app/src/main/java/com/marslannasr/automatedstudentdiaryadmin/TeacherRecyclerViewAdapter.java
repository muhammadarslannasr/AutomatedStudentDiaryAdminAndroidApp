package com.marslannasr.automatedstudentdiaryadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ArslanNasr on 5/25/2018.
 */

public class TeacherRecyclerViewAdapter extends RecyclerView.Adapter<TeacherRecyclerViewAdapter.ViewHolder> {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private Context context;
    private ConnectionDetect connectionDetect;
    private List<Teacher> teacherList;
    private EditText teacherName_ID,teacherQulifiacationTextId,teacherUniTextID,teachermobilenumberTextId;
    private Spinner teacherspinner2genderId;
    private Button teacher_submitbuttonId;
    String genderListSpinner;
    private ProgressDialog mProgressDialog;

    public TeacherRecyclerViewAdapter(Context context, List<Teacher> teacherList) {
        this.context = context;
        this.teacherList = teacherList;
        connectionDetect = new ConnectionDetect(context);
    }

    @NonNull
    @Override
    public TeacherRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_datarow,parent,false);
        return new TeacherRecyclerViewAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherRecyclerViewAdapter.ViewHolder holder, int position) {

        Teacher teacher = teacherList.get(position);
        holder.teacher_name.setText(teacher.getTeacher_name());
        holder.teacher_qualification.setText(teacher.getTeacher_qualification());
        holder.teacher_uniID.setText(teacher.getTeacher_university());
        holder.teacher_phoneNumber.setText(teacher.getPhone_number());
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(teacher.getTimestamp())).getTime());
        holder.dateAdded.setText(formattedDate);
    }

    public void setfilter(List<Teacher> itemList){
        teacherList = new ArrayList<>();
        teacherList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView teacher_name,teacher_qualification,teacher_uniID,teacher_phoneNumber,dateAdded;
        public Button editButton,deleteButton;
        public DatabaseReference databaseReference;
        public FirebaseAuth mAuth;
        public FirebaseUser mUser;
        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);

            context = ctx;
            mProgressDialog = new ProgressDialog(context);
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            teacher_name = (TextView) itemView.findViewById(R.id.teacher_name);
            teacher_qualification = (TextView) itemView.findViewById(R.id.teacher_qualification);
            teacher_uniID = (TextView) itemView.findViewById(R.id.teacher_uniID);
            teacher_phoneNumber = (TextView) itemView.findViewById(R.id.teacher_phoneNumber);
            dateAdded = (TextView) itemView.findViewById(R.id.dateAdded);
            editButton = (Button) itemView.findViewById(R.id.editButton);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);


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
                            Teacher t = teacherList.get(position);
                            databaseReference = FirebaseDatabase.getInstance().getReference("TeacherData").child(t.getTeacher_nodeID());
                            databaseReference.removeValue();
                            teacherList.remove(getAdapterPosition());
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
                    view = inflater.inflate(R.layout.teacher_adddata, null);
                    teacherName_ID = view.findViewById(R.id.teacherName_ID);
                    teacherQulifiacationTextId = view.findViewById(R.id.teacherQulifiacationTextId);
                    teacherUniTextID = view.findViewById(R.id.teacherUniTextID);
                    teachermobilenumberTextId = view.findViewById(R.id.teachermobilenumberTextId);
                    teacherspinner2genderId = view.findViewById(R.id.teacherspinner2genderId);
                    teacher_submitbuttonId = view.findViewById(R.id.teacher_submitbuttonId);


                    int position = getAdapterPosition();
                    Teacher teacher = teacherList.get(position);

                    //Setting Teacher Name From List
                    teacherName_ID.setText(teacher.getTeacher_name());
                    teacherName_ID.setSelection(teacherName_ID.getText().length());

                    //Setting Teacher Qualification From List
                    teacherQulifiacationTextId.setText(teacher.getTeacher_qualification());
                    teacherQulifiacationTextId.setSelection(teacherQulifiacationTextId.getText().length());

                    //Setting Teacher University From List
                    teacherUniTextID.setText(teacher.getTeacher_university());
                    teacherUniTextID.setSelection(teacherUniTextID.getText().length());

                    //Setting Teacher Mobile Number From List
                    teachermobilenumberTextId.setText(teacher.getPhone_number());
                    teachermobilenumberTextId.setSelection(teachermobilenumberTextId.getText().length());

                    dialogBuilder.setView(view);
                    dialog = dialogBuilder.create();
                    dialog.show();



                    List<String> genderList = new ArrayList<>();
                    genderList.add("Male");
                    genderList.add("Female");
                    genderList.add("Other");
                    genderList.add("Rather not say");
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,genderList);
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
                                    updateTeacher();
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

        private void updateTeacher() {

            mProgressDialog.setMessage("Adding Data....");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            int position = getAdapterPosition();
            final Teacher teacher = teacherList.get(position);
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("TeacherData").child(teacher.getTeacher_nodeID());
            String ID = databaseReference.getKey();
            Map<String,String> data = new HashMap<>();
            data.put("teacher_name",teacherName_ID.getText().toString().trim());
            teacher.setTeacher_name(teacherName_ID.getText().toString().trim());
            data.put("teacher_qualification",teacherQulifiacationTextId.getText().toString().trim());
            teacher.setTeacher_qualification(teacherQulifiacationTextId.getText().toString().trim());
            data.put("teacher_university",teacherUniTextID.getText().toString().trim());
            teacher.setTeacher_university(teacherUniTextID.getText().toString().trim());
            data.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
            data.put("userid",mUser.getUid());
            data.put("gender",genderListSpinner);
            teacher.setGender(genderListSpinner);
            data.put("phone_number",teachermobilenumberTextId.getText().toString().trim());
            teacher.setPhone_number(teachermobilenumberTextId.getText().toString().trim());
            data.put("teacher_nodeID",ID);
            databaseReference.setValue(data);
            notifyItemChanged(getAdapterPosition(),teacher);
            Toast.makeText(context, "Data Updated!", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            dialog.dismiss();
        }
    }


}
