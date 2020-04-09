package com.example.myapplication;

public class Artist {
    String artistId;
    String artistName;
    String Email;
    String Address;
    String Coordinates;
    public Artist(){

    }

    public Artist(String artistId, String artistName, String Email,String Address, String Coordinates) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.Email= Email;
        this.Address = Address;
        this.Coordinates = Coordinates;

    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }
    public String getCoordinates() {
        return Coordinates;
    }


}
