package com.example.handsome.thenewtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by handsome on 2015/9/16.
 */
public class Movie implements Parcelable {

    protected Movie(Parcel in) {
        state = in.readString();
        atMoviesMvId = in.readString();
        mvName = in.readString();
        enName = in.readString();
        mvlength = in.readString();
        playingDate = in.readString();
        gate = in.readString();
        story = in.readString();
        imgLink = in.readString();
        director = in.readString();
        writer = in.readString();
        actor = in.readString();
        IMDbMvId = in.readString();
        mv_IMDbMoblieUrl = in.readString();
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;
    private String atMoviesMvId;
    private String mvName;
    private String enName;
    private String mvlength = null;
    private String playingDate = null;
    private String gate;

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

    public String getIMDbMvId() {
        return IMDbMvId;
    }

    public void setIMDbMvId(String IMDbMvId) {
        this.IMDbMvId = IMDbMvId;
    }

    public String getMv_IMDbMoblieUrl() {
        return mv_IMDbMoblieUrl;
    }

    public void setMv_IMDbMoblieUrl(String mv_IMDbMoblieUrl) {
        this.mv_IMDbMoblieUrl = mv_IMDbMoblieUrl;
    }

    public boolean isThisWeek() {
        return isThisWeek;
    }

    public void setIsThisWeek(boolean isThisWeek) {
        this.isThisWeek = isThisWeek;
    }

    public List<String> getYoutubeUrlList() {
        return youtubeUrlList;
    }

    public void setYoutubeUrlList(List<String> youtubeUrlList) {
        this.youtubeUrlList = youtubeUrlList;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setIsOut(boolean isOut) {
        this.isOut = isOut;
    }

    public Double getIMDbRating() {
        return IMDbRating;
    }

    public void setIMDbRating(Double IMDbRating) {
        this.IMDbRating = IMDbRating;
    }

    public List<String> getAllMvThShowtimeList() {
        return allMvThShowtimeList;
    }

    public void setAllMvThShowtimeList(List<String> allMvThShowtimeList) {
        this.allMvThShowtimeList = allMvThShowtimeList;
    }

    private String imgLink;

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    private String director = null;
    private String writer = null;
    private String actor = null;
    private String story = null;//in GAE, the attribute of story is Text which store more String data in GAE.
    private String IMDbMvId = null;
    private String mv_IMDbMoblieUrl;
    private boolean isThisWeek;
    private List<String> youtubeUrlList;
    private boolean isOut;
    private Double IMDbRating;
    private List<String> allMvThShowtimeList;

    public Movie() {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(state);
        parcel.writeString(atMoviesMvId);
        parcel.writeString(mvName);
        parcel.writeString(enName);
        parcel.writeString(mvlength);
        parcel.writeString(playingDate);
        parcel.writeString(gate);
        parcel.writeString(story);
        parcel.writeString(imgLink);
        parcel.writeString(director);
        parcel.writeString(writer);
        parcel.writeString(actor);
        parcel.writeString(IMDbMvId);
        parcel.writeString(mv_IMDbMoblieUrl);
        parcel.writeStringList(allMvThShowtimeList);
    }
}
