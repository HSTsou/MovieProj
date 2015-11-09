package com.example.handsome.thenewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.helper.DatabaseHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by handsome on 2015/10/28.
 */
public class TheaterListAdapter extends  RecyclerView.Adapter<TheaterListAdapter.ViewHolder> {

    private Context context;
    private ViewHolder viewHolder;

    public List<Map<String, String>> allData ;



    public TheaterListAdapter(Context context, List<Map<String, String>> allData ) {
        //super(context, 0, allData);

        this.context = context;
        this.allData = allData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_theaterlist, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int p) {
        this.viewHolder = viewHolder;

        Map<String, String> item = allData.get(p);

        String[] col = DatabaseHelper.COL_THEATER_LIST;
        for (int i = 0; i < col.length; i++) {
            String datum = item.get(col[i]);
            if (datum != null) {
                switch (i) {
                    case 0:
                        // Log.i("hs", "data = "+datum);
                        viewHolder.thName_text.setText(datum);
                        break;
                    case 1:
                        viewHolder.address_text.setText(datum);
                        break;
                }
            }
        }

        String strDistance = item.get("distance");
        if (strDistance != null)
            viewHolder.distance_text.setText(strDistance + " km");
        else {
            viewHolder.distance_text.setVisibility(View.GONE);
            // Toast.makeText(getApplicationContext(), "目前無法偵測您的位置",
            // Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView thName_text;
        TextView address_text;
        TextView distance_text;

        public ViewHolder(View itemView) {
            super(itemView);

            thName_text = (TextView) itemView.findViewById(R.id.thName_text);
            address_text =  (TextView)itemView.findViewById(R.id.address_text);
            distance_text = (TextView) itemView.findViewById(R.id.distance_text);

        }


    }


}
