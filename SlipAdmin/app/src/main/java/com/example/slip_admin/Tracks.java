package com.example.slip_admin;


public class Tracks {
    String trackId;
    String trackName;
    int trackRating;

    public Tracks() {

    }

    public Tracks(String trackId, String trackName, int trackRating) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.trackRating = trackRating;
    }


    public String getTrackId() {
        return trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public int getTrackRating() {
        return trackRating;
    }
}