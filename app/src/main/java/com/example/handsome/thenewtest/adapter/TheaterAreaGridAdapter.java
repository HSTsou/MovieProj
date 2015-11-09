package com.example.handsome.thenewtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.TheaterListActivity;
import com.example.handsome.thenewtest.entity.AreaObject;

import java.util.List;

public class TheaterAreaGridAdapter extends BaseAdapter {
    private LayoutInflater layoutinflater;
    private List<AreaObject> listStorage;
    private Context context;

    public TheaterAreaGridAdapter(Context context, List<AreaObject> customizedListView) {
        this.context = context;
        layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }
    @Override
    public int getCount() {
        return listStorage.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.theater_grid_item, parent, false);
           // listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.textView);
            //listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.imageView);
            listViewHolder.areaBtn = (Button)convertView.findViewById(R.id.area_btn);
            listViewHolder.rootlayout = (RelativeLayout)convertView.findViewById(R.id.rootLayout);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.areaBtn.setText(listStorage.get(position).getAreaName());


        listViewHolder.areaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("hs", listStorage.get(position).getAreaId() );
                Intent i = new Intent();
                i.setClass(context, TheaterListActivity.class);
                i.putExtra("areaId",  listStorage.get(position).getAreaId());
                i.putExtra("areaName",  listStorage.get(position).getAreaName());
                context.startActivity(i);
            }
        });


      //  int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(), "drawable", this.context.getPackageName());
       // listViewHolder.imageInListView.setImageResource(imageResourceId);
        return convertView;
    }
    static class ViewHolder{
        //TextView textInListView;
        //ImageView imageInListView;
        Button areaBtn;
        RelativeLayout rootlayout;
    }
}