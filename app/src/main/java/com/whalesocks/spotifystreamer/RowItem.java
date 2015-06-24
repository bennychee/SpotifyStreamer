package com.whalesocks.spotifystreamer;

/**
 * Created by B on 6/24/2015.
 */
public class RowItem {
    private String artistName;
    private String spotifyId;
    private String artistImageURL;

    private String trackName;
    private String albumName;
    private String albumImageURL;

    public RowItem(String artistName, String spotifyId, String artistImageURL) {
        this.artistName = artistName;
        this.spotifyId = spotifyId;
        this.artistImageURL = artistImageURL;
    }

    public RowItem(String trackName, String albumName, String spotifyId, String albumImageURL) {
        this.trackName = trackName;
        this.spotifyId = spotifyId;
        this.albumName = albumName;
        this.albumImageURL = albumImageURL;
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

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumImageURL() {
        return albumImageURL;
    }

    public void setAlbumImageURL(String albumImageURL) {
        this.albumImageURL= albumImageURL;
    }


}
