package com.whalesocks.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import kaaes.spotify.webapi.android.*;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> mSearchResults;
    List<String> artistsName = new ArrayList<String>();
    List<String> spotifyId = new ArrayList<String>();
    List<String> artistImageURL = new ArrayList<String>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final String LOG_TAG = getActivity().getLocalClassName();

        mSearchResults = new ArrayAdapter<String> (
            getActivity(),
            R.layout.list_item_result,
            R.id.listview_artist_search_result,
            new ArrayList<String>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist_search_result);
        listView.setAdapter(mSearchResults);

        EditText searchEditText = (EditText) rootView.findViewById(R.id.search_artist_text);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchForArtist(v.getText().toString());
                    Log.v(LOG_TAG, v.getText().toString());
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    private void searchForArtist (String artistName) {
        SearchFetchArtistTask searchTask = new SearchFetchArtistTask();
        searchTask.execute(artistName);
    }

    public class SearchFetchArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        private final String LOG_TAG = SearchFetchArtistTask.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            ArtistsPager results = spotify.searchArtists(params[0]);
            return results;
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            super.onPostExecute(artistsPager);

            List<Artist> artistList = artistsPager.artists.items;
            Iterator<Artist> iterator = artistList.iterator();

            while (iterator.hasNext()) {

                Artist artist = iterator.next();

                artistsName.add(artist.name);
                spotifyId.add(artist.id);

                Log.v(LOG_TAG, "Artist Name = " + artist.name);
                Log.v (LOG_TAG, "Spotify ID = " + artist.id);

                if (artist.images.size() != 0) {

                    //Log.v(LOG_TAG, "Image size = " + artist.images.size());

                    //Only require the 1st image of the Artist
                    String url = artist.images.get(0).url;
                    artistImageURL.add(url);

                    Log.v(LOG_TAG, "Image URL = " + url);

                } else {
                    //No image for this Artist
                    artistImageURL.add("");
                }
            }

            mSearchResults.clear();
            mSearchResults.addAll(artistsName);
        }
    }
}
