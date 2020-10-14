package com.marslannasr.automatedstudentdiaryadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by ArslanNasr on 6/28/2018.
 */

public class ScheduleRecyclerVireAdapter extends RecyclerView.Adapter<ScheduleRecyclerVireAdapter.ViewHolder> {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private Context context;
    private ConnectionDetect connectionDetect;
    private List<Schedule> scheduleList;

    public ScheduleRecyclerVireAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ScheduleRecyclerVireAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row,parent,false);
        return new ScheduleRecyclerVireAdapter.ViewHolder(view,context);
    }



    @Override
    public void onBindViewHolder(@NonNull ScheduleRecyclerVireAdapter.ViewHolder holder, int position) {

        Schedule schedule = scheduleList.get(position);
        String imageUrl = null;

        holder.postTitleList.setText(schedule.getPostTitleEt());
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(schedule.getTimestamp())).getTime());
        holder.timeStampList.setText(formattedDate);
        imageUrl = schedule.getImageButton();
        //TODO: USE PICASOO library to load images
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.postImageList);
    }




    @Override
    public int getItemCount() {
        return scheduleList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView postImageList;
        public TextView postTitleList;
        public TextView timeStampList;
        public Button deleteButton;
        public DatabaseReference databaseReference;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            postImageList = itemView.findViewById(R.id.postImageList);
            postTitleList = itemView.findViewById(R.id.postTitleList);
            timeStampList = itemView.findViewById(R.id.timeStampList);
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
                            Schedule schedule = scheduleList.get(position);
                            databaseReference = FirebaseDatabase.getInstance().getReference("ScheduleData").child(schedule.getSchedule_ID());
                            databaseReference.removeValue();
                            scheduleList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();


                        }
                    });
                }
            });



        }
    }

}
