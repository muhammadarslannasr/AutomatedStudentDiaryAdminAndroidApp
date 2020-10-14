package com.marslannasr.automatedstudentdiaryadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by ArslanNasr on 5/26/2018.
 */

public class TimeTableRecyclerViewAdapter extends RecyclerView.Adapter<TimeTableRecyclerViewAdapter.ViewHolder> {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private Context context;
    private List<TimeTable> timeTableList;

    public TimeTableRecyclerViewAdapter(Context context, List<TimeTable> timeTableList) {
        this.context = context;
        this.timeTableList = timeTableList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timetable_row,parent,false);
        return new TimeTableRecyclerViewAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableRecyclerViewAdapter.ViewHolder holder, int position) {

        TimeTable timeTable = timeTableList.get(position);
        holder.subject_name.setText(timeTable.getSubject());
        holder.teacher_name.setText(timeTable.getTeacher_name());
        holder.room_numID.setText(timeTable.getRoom_number());
        holder.time_lectureID.setText(timeTable.getTime_date());
    }

    @Override
    public int getItemCount() {
      return   timeTableList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView subject_name,teacher_name,room_numID,time_lectureID;
        public Button editButton,deleteButton;
        public DatabaseReference databaseReference;
        public ViewHolder(View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            subject_name = itemView.findViewById(R.id.subject_name);
            teacher_name = itemView.findViewById(R.id.teacher_name);
            room_numID = itemView.findViewById(R.id.room_numID);
            time_lectureID = itemView.findViewById(R.id.time_lectureID);
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
                            int position = getAdapterPosition();
                            TimeTable timeTable = timeTableList.get(position);
                            databaseReference = FirebaseDatabase.getInstance().getReference("TimeTable").child(timeTable.getTimetable_nodeID());
                            databaseReference.removeValue();
                            timeTableList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();


                        }
                    });
                }
            });

        }




    }
}
