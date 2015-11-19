package com.example.handsome.thenewtest.fragment;

import android.content.Intent;
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
import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.WebActivity;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.helper.ButtonHighlighterOnTouchListener;
import com.example.handsome.thenewtest.util.AppController;

/**
 * Created by handsome on 2015/10/18.
 */
public class MovieInfoFragment extends Fragment implements View.OnClickListener {

    TextView gate, story, length, director, actor, writer, playingDate, enName;
    NetworkImageView pic;
    Boolean isTextViewClicked = false;
    ImageButton IMDb_btn, Tomato_btn;
    TextView IMDbRating, tomatoRating;
    Movie mv;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String IMDbUrl, tomatoesUrl;

    public static MovieInfoFragment createInstance(Movie m) {
        Log.i("hs", "MovieInfoFragment createInstance ");
        MovieInfoFragment f = new MovieInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", m);
        Log.i("hs", "MovieInfoFragment m.getIMDB" + m.getIMDbRating());
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("hs", "MovieInfoFragment onCreateView");

        return inflater.inflate(R.layout.activity_movie_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("hs", "MovieInfoFragment onViewCreated");
        enName =  (TextView) view.findViewById(R.id.enName_text);
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
            mv = bundle.getParcelable("movie");
            enName.setText(mv.getEnName());
            if(mv.getGate() != null){
                gate.setText(transGateToChinese(mv.getGate()));
            }

            if (mv.getStory().length() > 10){
                story.setText(mv.getStory().substring(10, mv.getStory().length() - 2));//{"value":"XXXXXXX"}
            }

            pic.setImageUrl(mv.getImgLink(), imageLoader);
            length.setText(mv.getMvlength());
            playingDate.setText(mv.getPlayingDate());
            director.setText(mv.getDirector());
            actor.setText(mv.getActor());
            writer.setText(mv.getWriter());
            Log.i("hs", "setInfoData() mv.getImgLink()" + mv.getImgLink());
            Log.i("hs", "setInfoData() mv.getIMDB" + mv.getIMDbRating());
            if(mv.getIMDbRating() != null){
                if(mv.getIMDbRating() == 0){
                    IMDbRating.setText("--");
                }else{
                    IMDbRating.setText(String.valueOf(mv.getIMDbRating()));
                }
            }

            if(mv.getTomatoesRating()  != null){
                if(mv.getTomatoesRating() == 0){
                    tomatoRating.setText("--");
                }else{
                    tomatoRating.setText(String.valueOf(mv.getTomatoesRating())+"%");
                }

            }

            IMDbUrl = mv.getMv_IMDbMoblieUrl();
            tomatoesUrl = mv.getMv_TomatoesMoblieUrl();


        }

    }

    String transGateToChinese(String gate) {

        switch (gate) {
            case "G":
                gate = "普遍級 G";
                break;
            case "P":
                gate = "保護級 P";
                break;
            case "PG":
                gate = "輔導級 PG";
                break;
            case "R":
                gate = "限制級 R";
                break;
            case "F2":
                gate = "輔導級 12+";
                break;
            case "F5":
                gate = "輔導級 15+";
                break;

            default:

                break;
        }
        return gate;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.story_text:
                //story text expanded and collapsed.
                if (isTextViewClicked) {
                    //This will shrink textview to 3 lines if it is expanded.
                    story.setMaxLines(3);
                    isTextViewClicked = false;
                } else {
                    //This will expand the textview if it is of 3 lines
                    story.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked = true;
                }
                break;

            case R.id.IMDb_img:
                if(IMDbUrl != null && IMDbUrl.contains("imdb")){
                    Intent web = new Intent(getActivity(), WebActivity.class);
                    web.putExtra("url", IMDbUrl);
                    startActivity(web);
                }
                break;

            case R.id.tomato_img:
                if(tomatoesUrl != null&&  tomatoesUrl.contains("rottentomatoes")){

                    Intent web = new Intent(getActivity(), WebActivity.class);
                    web.putExtra("url", tomatoesUrl);
                    startActivity(web);
                }
                break;


            default:

                break;
        }
    }


}
