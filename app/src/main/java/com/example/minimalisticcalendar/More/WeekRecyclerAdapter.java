package com.example.minimalisticcalendar.More;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minimalisticcalendar.R;
import com.example.minimalisticcalendar.Startpage.CDate;
import com.example.minimalisticcalendar.Startpage.StartpageBackground;

import java.util.ArrayList;

public class WeekRecyclerAdapter extends RecyclerView.Adapter<WeekRecyclerAdapter.MyViewHolder> {

    private ArrayList<CDate> mDates;
    private Context mContext;
    private String mweekdate;
    private String mcurrent_day;


    public WeekRecyclerAdapter(Context context, ArrayList<CDate> Dates, String weekdate, String current_day){
        mDates = Dates;
        mContext = context;
        mweekdate = weekdate;
        mcurrent_day = current_day;
    }

    @NonNull
    @Override
    public WeekRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_rec_item, parent, false);
        final MyViewHolder holder = new WeekRecyclerAdapter.MyViewHolder(view);

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(holder.title.getText());
                if(mDates.get(holder.getAdapterPosition()).desc()!=null && !mDates.get(holder.getAdapterPosition()).desc().equals("")) {
                    alert.setMessage(mDates.get(holder.getAdapterPosition()).desc());
                }else{
                    alert.setMessage("no description");
                }
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, StartpageBackground.class);
                        intent.putExtra("delpos", holder.getAdapterPosition());
                        intent.putExtra("week", mweekdate);
                        intent.putExtra("day", mcurrent_day);
                        mContext.startActivity(intent);
                    }
                });

                alert.show();
            }
        });

        return holder;
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String time = correctTimeFormat(position);
        holder.title.setText(mDates.get(position).title());
        holder.time.setText(time);
        String name = "roundedbutton_blue";
        switch(mDates.get(position).color()){
            case "red":
                name = "roundedbutton_red";
                break;
            case "blue":
                name = "roundedbutton_blue";
                break;
            case "dark":
                name = "roundedbutton_dark";
                break;
            case "green":
                name = "roundedbutton_green";
                break;
            case "orange":
                name = "roundedbutton_orange";
                break;
            case "yellow":
                name = "roundedbutton_yellow";
                break;
        }
        holder.color.setBackgroundResource(mContext.getResources().getIdentifier(name,"drawable", mContext.getPackageName()));
    }

    private String correctTimeFormat(int position){
        char[] digits1 = mDates.get(position).time().toString().toCharArray();
        if(digits1.length == 3){
            return digits1[0]+":"+digits1[1]+digits1[2];
        }else if(digits1.length == 2){
            return String.valueOf(digits1[0]) + digits1[1];
        }else{
            return String.valueOf(digits1[0]) + digits1[1] + ":" + digits1[2] + digits1[3];
        }
    }


    @Override
    public int getItemCount() {
        return mDates.size();

    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView time;
        TextView color;
        RelativeLayout parentlayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            color = itemView.findViewById(R.id.color);
            parentlayout = itemView.findViewById(R.id.parentlayout);
        }
    }

}
