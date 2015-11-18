package com.example.handsome.thenewtest.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.entity.MovieTime;

import java.util.ArrayList;

public class MovieTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MovieTime> mtList;
    View view;

    public MovieTimeAdapter(ArrayList<MovieTime> mtList) {
        this.mtList = mtList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if(mtList.size()>0){
            view = LayoutInflater.from(context).inflate(R.layout.content_movietime, parent, false);
            return new RecyclerItemViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.content_no_time, parent, false);
            return null;
        }



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;

        MovieTime mt = mtList.get(position);

        if(mt != null){
            holder.thName.setText(mt.getThName());
            holder.hallName.setText(mt.getHall());
            // holder.time.setText(mt.getTimeStr());
            holder.stLayout.removeAllViews();//According to the below,to avoid reinsert the time layout.
            holder.stLayout.addView(setTimeText(mt.getTimeStr()));
        }else{

        }



    }

    @Override
    public int getItemCount() {
        return mtList == null ? 0 : mtList.size();
    }

    public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {


        TextView thName, hallName, time;
        TableLayout stLayout;

        public RecyclerItemViewHolder(View parent) {
            super(parent);


            thName = (TextView) parent.findViewById(R.id.thName_text);
            hallName = (TextView) parent.findViewById(R.id.hall_text);
            //time = (TextView) parent.findViewById(R.id.time_text);
            stLayout = (TableLayout) parent.findViewById(R.id.time_table_layout);

        }


    }

    public View setTimeText(String text) {

        TableLayout stLayout = new TableLayout(view.getContext());
        if (text != null) {
            TableRow tableRow = new TableRow(view.getContext());
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);

            text = text.replaceAll("：", ":");
            String[] timeStr = text.split("\\s");
            int record = 0;
            for (int x = 0; x < timeStr.length; x++) {
                String time = timeStr[x];

                final Button timeButton = new Button(view.getContext());
                timeButton.setText(time);
                timeButton.setPadding(12, 0, 12, 0);
                // timeButton.setTextColor(R.color.black);
                timeButton.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.transparent));
                timeButton.setTypeface(Typeface.SERIF);
                /*
                if (isAvaliable) {
                    // same day
                    timeButton.setEnabled(DateHelper
                            .isNowTimeBefore(convertedTime));
                } else
                    timeButton.setEnabled(false);
                // --------------------------
                */
                tableRow.addView(timeButton);

                record++;
                if (record % 4 == 0 && record != 0) {
                    if (tableRow.getParent() != null) {
                        ((ViewGroup) tableRow.getParent()).removeView(tableRow);
                    }
                    stLayout.addView(tableRow);
                    tableRow = new TableRow(view.getContext());
                    tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            }
            if (record % 4 != 0 && record != 0) {
                if (tableRow.getParent() != null) {
                    ((ViewGroup) tableRow.getParent()).removeView(tableRow);
                }
                stLayout.addView(tableRow);

            }
        }
        return stLayout;
    }


}
