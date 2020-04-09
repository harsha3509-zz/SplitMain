package com.example.slip_admin;
public class Artist {
    String artistId;
    String artistName;
    String email;
    String address;
    String coordinates;
    public Artist(){

    }

    public Artist(String artistId, String artistName, String email,String address, String coordinates) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.email= email;
        this.address = address;
        this.coordinates = coordinates;

    }

    public Artist(String artistId, String artistName, String email) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.email=email;

    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
    public String getCoordinates() {
        return coordinates;
    }


}
