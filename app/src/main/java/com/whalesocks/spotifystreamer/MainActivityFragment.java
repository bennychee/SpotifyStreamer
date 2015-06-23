package com.whalesocks.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


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
            R.id.text_view_artist_name,
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

    private void clearArrayList () {
        artistsName.clear();
        spotifyId.clear();
        artistImageURL.clear();
    }

    private void searchForArtist (String searchArtistName) {
        clearArrayList();
        SearchFetchArtistTask searchTask = new SearchFetchArtistTask();
        searchTask.execute(searchArtistName);
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
                Log.v(LOG_TAG, "Spotify ID = " + artist.id);

                int thumb = artist.images.size();
                if (thumb != 0) {

                    //Get the smallest size image in the image list
                    String url = artist.images.get(thumb-1).url;
                    artistImageURL.add(url);

                    Log.v(LOG_TAG, "Image URL = " + url);

                } else {
                    //No image for this Artist
                    artistImageURL.add("");
                }
            }

            for (String name : artistsName) {
                Log.v(LOG_TAG, "Artist Name: " + name);
            }

            mSearchResults.clear();
            mSearchResults.addAll(artistsName);
        }
    }
}
