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

import com.android.volley.toolbox.ImageLoader;
import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.util.AppController;

import java.util.List;

public class TheaterTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String[]>  mvUnitList;
    Context context ;
    View view;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public TheaterTimeAdapter(List<String[]>  mvUnitList) {
        this.mvUnitList =  mvUnitList ;
    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.content_theatertime, parent, false);


        return new RecyclerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        String[] mvUnit = mvUnitList.get(position);

        String mvId =  mvUnit[0];
        String mvTitle = mvUnit[1];
        String picUrl = mvUnit[2];
        String length = mvUnit[3];
        String version = mvUnit[4];
        String timeStr = mvUnit[5];

        holder.mvName.setText(mvTitle);

        if(version.equalsIgnoreCase("")||version == null){
            holder.version.setVisibility(View.GONE);
        }else{
            holder.version.setText(version);
        }


        //holder.mv_pic.setImageUrl(picUrl, imageLoader);
        //holder.setTimeText(mt.getTimeStr());
        holder.stLayout.removeAllViews();
        holder.stLayout.addView(setTimeText(timeStr));

    }

    @Override
    public int getItemCount() {
        return mvUnitList == null ? 0 : mvUnitList.size();
    }

    public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        TextView mvName, version;
      //  NetworkImageView mv_pic;
        TableLayout stLayout;

        public RecyclerItemViewHolder(View parent) {
            super(parent);

            //mv_pic = (NetworkImageView) itemView.findViewById(R.id.mv_pic);
            mvName = (TextView) parent.findViewById(R.id.mvName_text);
            version = (TextView) parent.findViewById(R.id.version_text);
            stLayout = (TableLayout) parent.findViewById(R.id.time_table_layout);
        }



    }

    public View setTimeText( String  text) {
        TableLayout stLayout = new TableLayout(view.getContext());
        if(text != null){

            TableRow tableRow  = new TableRow(view.getContext());
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
                timeButton.setTypeface(Typeface.SERIF );
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
                    if(tableRow.getParent()!=null){
                        ((ViewGroup)tableRow.getParent()).removeView(tableRow );
                    }
                    stLayout.addView(tableRow);
                    tableRow = new TableRow(view.getContext());
                    tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            }
            if (record % 4 != 0 && record != 0) {
                if(tableRow.getParent()!=null){
                    ((ViewGroup)tableRow.getParent()).removeView(tableRow );
                }
                stLayout.addView(tableRow);

            }
        }
        return stLayout;
    }

}
