package com.example.handsome.thenewtest.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.handsome.thenewtest.util.AppController;
import com.example.handsome.thenewtest.helper.ButtonHighlighterOnTouchListener;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.R;

/**
 * Created by handsome on 2015/10/18.
 */
public class MovieInfoFragment extends Fragment implements View.OnClickListener {

    TextView gate , story, length, director, actor, writer, playingDate;
    NetworkImageView pic;
    Boolean isTextViewClicked = false;
    ImageButton IMDb_btn, Tomato_btn;
    TextView IMDbRating,tomatoRating;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public static MovieInfoFragment createInstance(Movie m) {
        Log.i("hs", "createInstance ");
        MovieInfoFragment f = new MovieInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", m);
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("hs", "onCreateView");

        return inflater.inflate(R.layout.activity_movie_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("hs", "onViewCreated");
        gate = (TextView) view.findViewById(R.id.gate_text);
        story = (TextView) view.findViewById(R.id.story_text);
        story.setOnClickListener(this);
        length = (TextView) view.findViewById(R.id.length_text);
        pic = (NetworkImageView) view.findViewById(R.id.info_mv_pic);
        director = (TextView) view.findViewById(R.id.director_value_text);
        writer = (TextView) view.findViewById(R.id.writer_value_text);
        actor = (TextView) view.findViewById(R.id.actor_value_text);
        playingDate = (TextView) view.findViewById(R.id.playdate_value_text);

        IMDbRating = (TextView) view.findViewById(R.id.IMDbRating_text);
        IMDbRating.setOnTouchListener(new ButtonHighlighterOnTouchListener(IMDbRating));
        IMDb_btn = (ImageButton) view.findViewById(R.id.IMDb_img);
        IMDb_btn.setOnClickListener(this);
        IMDb_btn.setOnTouchListener(new ButtonHighlighterOnTouchListener(IMDb_btn));


        tomatoRating = (TextView) view.findViewById(R.id.tomatoRating_text);
        tomatoRating.setOnTouchListener(new ButtonHighlighterOnTouchListener(tomatoRating));
        Tomato_btn = (ImageButton) view.findViewById(R.id.tomato_img);
        Tomato_btn.setOnClickListener(this);
        Tomato_btn.setOnTouchListener(new ButtonHighlighterOnTouchListener(Tomato_btn));


        setInfoData();
    }

    void setInfoData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            Movie mv = bundle.getParcelable("movie");
            gate.setText(mv.getGate());
            story.setText(mv.getStory());
            pic.setImageUrl(mv.getImgLink(), imageLoader);
            length.setText(mv.getMvlength());
            playingDate.setText(mv.getPlayingDate());
            director.setText(mv.getDirector());
            actor.setText(mv.getActor());
            writer.setText(mv.getWriter());
            tomatoRating.setText(String.valueOf(mv.getIMDbRating()));

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.story_text:

                if(isTextViewClicked){
                    //This will shrink textview to 3 lines if it is expanded.
                    story.setMaxLines(3);
                    isTextViewClicked = false;
                } else {
                    //This will expand the textview if it is of 3 lines
                    story.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked = true;
                }
                break;

            case R.id.tomato_img:

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.imdb.com/title/tt2361509/"));
                startActivity(intent);
                break;
            case R.id.IMDb_img:

                Intent imdb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.imdb.com/title/tt2361509/"));
                startActivity(imdb);
                break;
            default:

                break;
        }
    }
}
