package com.example.handsome.thenewtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by handsome on 2015/9/16.
 */
public class Movie implements Parcelable {
    public Movie(){

    }



    private String gaeId;
    private String state;
    private String atMoviesMvId;
    private String mvName;
    private String enName;
    private String mvlength = null;
    private String playingDate = null;
    private String gate;
    private String imgLink;
    private String director = null;
    private String writer = null;
    private String actor = null;
    private String story = null;//in GAE, the attribute of story is Text which store more String data in GAE.
    private String mv_IMDbMoblieUrl;
    private String mv_TomatoesMoblieUrl;
    private List<String> youtubeUrlList;
    private Double IMDbRating;
    private Double tomatoesRating;

    protected Movie(Parcel in) {
        gaeId = in.readString();
        state = in.readString();
        atMoviesMvId = in.readString();
        mvName = in.readString();
        enName = in.readString();
        mvlength = in.readString();
        playingDate = in.readString();
        gate = in.readString();
        imgLink = in.readString();
        director = in.readString();
        writer = in.readString();
        actor = in.readString();
        story = in.readString();
        mv_IMDbMoblieUrl = in.readString();
        mv_TomatoesMoblieUrl = in.readString();
        youtubeUrlList = in.createStringArrayList();
        allMvThShowtimeList = in.createStringArrayList();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getGaeId() {
        return gaeId;
    }

    public void setGaeId(String gaeId) {
        this.gaeId = gaeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAtMoviesMvId() {
        return atMoviesMvId;
    }

    public void setAtMoviesMvId(String atMoviesMvId) {
        this.atMoviesMvId = atMoviesMvId;
    }

    public String getMvName() {
        return mvName;
    }

    public void setMvName(String mvName) {
        this.mvName = mvName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getMvlength() {
        return mvlength;
    }

    public void setMvlength(String mvlength) {
        this.mvlength = mvlength;
    }

    public String getPlayingDate() {
        return playingDate;
    }

    public void setPlayingDate(String playingDate) {
        this.playingDate = playingDate;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getMv_IMDbMoblieUrl() {
        return mv_IMDbMoblieUrl;
    }

    public void setMv_IMDbMoblieUrl(String mv_IMDbMoblieUrl) {
        this.mv_IMDbMoblieUrl = mv_IMDbMoblieUrl;
    }

    public String getMv_TomatoesMoblieUrl() {
        return mv_TomatoesMoblieUrl;
    }

    public void setMv_TomatoesMoblieUrl(String mv_TomatoesMoblieUrl) {
        this.mv_TomatoesMoblieUrl = mv_TomatoesMoblieUrl;
    }

    public List<String> getYoutubeUrlList() {
        return youtubeUrlList;
    }

    public void setYoutubeUrlList(List<String> youtubeUrlList) {
        this.youtubeUrlList = youtubeUrlList;
    }

    public Double getIMDbRating() {
        return IMDbRating;
    }

    public void setIMDbRating(Double IMDbRating) {
        this.IMDbRating = IMDbRating;
    }

    public Double getTomatoesRating() {
        return tomatoesRating;
    }

    public void setTomatoesRating(Double tomatoesRating) {
        this.tomatoesRating = tomatoesRating;
    }

    public List<String> getAllMvThShowtimeList() {
        return allMvThShowtimeList;
    }

    public void setAllMvThShowtimeList(List<String> allMvThShowtimeList) {
        this.allMvThShowtimeList = allMvThShowtimeList;
    }

    private List<String> allMvThShowtimeList;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gaeId);
        dest.writeString(state);
        dest.writeString(atMoviesMvId);
        dest.writeString(mvName);
        dest.writeString(enName);
        dest.writeString(mvlength);
        dest.writeString(playingDate);
        dest.writeString(gate);
        dest.writeString(imgLink);
        dest.writeString(director);
        dest.writeString(writer);
        dest.writeString(actor);
        dest.writeString(story);
        dest.writeString(mv_IMDbMoblieUrl);
        dest.writeString(mv_TomatoesMoblieUrl);
        dest.writeStringList(youtubeUrlList);
        dest.writeStringList(allMvThShowtimeList);
    }
}
