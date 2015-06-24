package com.whalesocks.spotifystreamer;

/**
 * Created by B on 6/24/2015.
 */
public class RowItem {
    private String artistName;
    private String spotifyId;
    private String artistImageURL;

    public RowItem(String artistName, String spotifyId, String artistImageURL) {
        this.artistName = artistName;
        this.spotifyId = spotifyId;
        this.artistImageURL = artistImageURL;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId){
        this.spotifyId = spotifyId;
    }

    public String getArtistImageURL() {
        return artistImageURL;
    }

    public void setArtistImageURL(String artistImageURL) {
        this.artistImageURL = artistImageURL;
    }

}
