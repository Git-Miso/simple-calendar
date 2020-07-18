package com.example.minimalisticcalendar.More;

import android.app.Activity;
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
import com.example.minimalisticcalendar.Startpage.StartpageBackground;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class GeburtstageAdapter extends RecyclerView.Adapter<GeburtstageAdapter.ViewHolderMoreView>{


    private ArrayList<Birthday> mBirthdays;
    private Context mContext;

    public GeburtstageAdapter(Context context, ArrayList<Birthday> Birthdays){
        mBirthdays = Birthdays;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolderMoreView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.geburtstag_rec_item,parent,false);
        final ViewHolderMoreView holder = new ViewHolderMoreView(view);

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTileClicked(holder, v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMoreView holder, int position) {
        holder.title.setText(mBirthdays.get(holder.getAdapterPosition()).title());
        String string = mBirthdays.get(holder.getAdapterPosition()).time();
        holder.time.setText(string);

            char[] date = mBirthdays.get(holder.getAdapterPosition()).time().toCharArray();

            Calendar c = Calendar.getInstance();
            int currentday = c.get(Calendar.DAY_OF_MONTH);
            int currentmonth = c.get(Calendar.MONTH)+1;

            StringBuilder day = new StringBuilder();
            day.append(date[0]);
            day.append(date[1]);
            StringBuilder month = new StringBuilder();
            month.append(date[3]);
            month.append(date[4]);
            if (currentmonth == Integer.parseInt(month.toString())) {
                if (Integer.parseInt(day.toString())-currentday < 14 && Integer.parseInt(day.toString())-currentday >= 0) {
                    holder.title.setText("> "+mBirthdays.get(holder.getAdapterPosition()).title());
                }
            }
    }

    @Override
    public int getItemCount(){
        return mBirthdays.size();
    }

    private void onTileClicked(final ViewHolderMoreView holder, final View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete the birthday '"+holder.title.getText()+"'");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ArrayList<Birthday> Geburtstage;
                Gson gson = new Gson();

                String fulltext = cFiles.loadBirthdays(mContext);
                Type BirthdayType = new TypeToken<ArrayList<Birthday>>(){}.getType();
                Geburtstage = gson.fromJson(fulltext, BirthdayType);
                Geburtstage.remove(holder.getAdapterPosition());

                cFiles.saveBirthdays(mContext, Geburtstage);

                Intent intent = new Intent(view.getContext(), StartpageBackground.class);
                intent.putExtra("goGeburtstage",true);
                view.getContext().startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(0,0);
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    static class ViewHolderMoreView extends RecyclerView.ViewHolder
        {

            TextView title;
            TextView time;
            RelativeLayout parentlayout;

            ViewHolderMoreView(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                time = itemView.findViewById(R.id.time);
                parentlayout = itemView.findViewById(R.id.parentlayout);
            }
        }
}
