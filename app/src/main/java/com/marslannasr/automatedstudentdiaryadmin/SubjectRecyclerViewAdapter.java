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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

/**
 * Created by ArslanNasr on 5/26/2018.
 */

public class SubjectRecyclerViewAdapter extends RecyclerView.Adapter<SubjectRecyclerViewAdapter.ViewHolder> {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private Context context;
    private ConnectionDetect connectionDetect;
    private List<Subject> subjectList;

    public SubjectRecyclerViewAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_rowdata,parent,false);
        return new SubjectRecyclerViewAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectRecyclerViewAdapter.ViewHolder holder, int position) {

        Subject subject = subjectList.get(position);

        holder.teacher_name.setText(subject.getTeacher_name());
        holder.subject_name.setText(subject.getSubject_name());
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(subject.getTimestamp())).getTime());
        holder.date.setText(formattedDate);
    }


    @Override
    public int getItemCount() {
        return subjectList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView teacher_name,subject_name,date;
        public Button deleteButton;
        public DatabaseReference databaseReference;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            teacher_name = itemView.findViewById(R.id.teacher_name);
            subject_name = itemView.findViewById(R.id.subject_name);
            date = itemView.findViewById(R.id.date);
            deleteButton = itemView.findViewById(R.id.deleteButton);

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
                            Subject subject = subjectList.get(position);
                            databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(subject.getSubject_nodeID());
                            databaseReference.removeValue();
                            subjectList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();


                        }
                    });
                }
            });



        }
    }
}
