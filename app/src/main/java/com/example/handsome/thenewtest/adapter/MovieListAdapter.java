package com.example.handsome.thenewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.handsome.thenewtest.util.AppController;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 2015/10/14.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {


    private Context context;
    private ViewHolder viewHolder;
    public  List<Movie> allData = new ArrayList<Movie>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public MovieListAdapter(Context context, List<Movie> allData) {
        //super(context, 0, allData);

        this.context = context;
        this.allData = allData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_movielist, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        this.viewHolder = viewHolder;
        Movie mvItem = allData.get(i);
       // Log.i("hs", "onBindViewHolder");

        String mvName = mvItem.getMvName();
        String enName = mvItem.getEnName();
        String playingDate = mvItem.getPlayingDate();
        double IMDbRating = mvItem.getIMDbRating();
        double tomatoesRating = mvItem.getTomatoesRating();
        String imgUrl = mvItem.getImgLink();

        viewHolder.mvName_text.setText(mvName);
        viewHolder.enName_text.setText(enName);
        if(IMDbRating == 0){
            viewHolder.IMDbRating_text.setText(" --");
        }else{
            viewHolder.IMDbRating_text.setText(String.valueOf(IMDbRating));
        }
        if(tomatoesRating == 0){
            viewHolder.tomatoesRating_text.setText(" --");
        }else{
            viewHolder.tomatoesRating_text.setText(String.valueOf((int)tomatoesRating)+"%");
        }

        viewHolder.playingDate_text.setText(playingDate);
        viewHolder.mv_pic.setImageUrl(imgUrl, imageLoader);
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mvName_text;
        TextView enName_text;
        TextView playingDate_text;
        TextView tomatoesRating_text;
        TextView IMDbRating_text;
        NetworkImageView mv_pic;

        public ViewHolder(View itemView) {
            super(itemView);

            mv_pic = (NetworkImageView) itemView.findViewById(R.id.mv_pic);
            mvName_text = (TextView) itemView.findViewById(R.id.mvName_text);
            enName_text = (TextView) itemView.findViewById(R.id.enName_text);
            playingDate_text = (TextView) itemView.findViewById(R.id.playingDate_text);
            IMDbRating_text = (TextView) itemView.findViewById(R.id.IMDbRating_text);
            tomatoesRating_text =  (TextView) itemView.findViewById(R.id.tomato_text);

            mvName_text.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (v instanceof TextView) {
                            Toast.makeText(context, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        }


       /* @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
        */
    }





}

