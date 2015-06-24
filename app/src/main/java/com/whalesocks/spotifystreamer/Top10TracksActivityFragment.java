package com.whalesocks.spotifystreamer;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TracksActivityFragment extends Fragment {

    private List<RowItem> rowItems;
    Top10TracksAdapter tracksAdapter;
    String artistSportifyId = null;
    String artistName = null;

    public Top10TracksActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top10_tracks, container, false);


        getActivity().requestWindowFeature(Window.FEATURE_ACTION_BAR);
        ActionBar actionbar = getActivity().getActionBar();

        rowItems = new ArrayList<>();

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist_top_10_tracks);
        tracksAdapter = new Top10TracksAdapter(getActivity(),rowItems);
        listView.setAdapter(tracksAdapter);

        Intent intent = getActivity().getIntent();
        if (intent !=null && intent.hasExtra("spotifyId")) {
            artistSportifyId = intent.getStringExtra("spotifyId");
            artistName = intent.getStringExtra("artist");
            actionbar.setSubtitle(artistName);
        }

        fetchTop10Tracks(artistSportifyId);

        return rootView;
    }

    private void fetchTop10Tracks (String id) {
        fetchTop10TracksTask fetchTask = new fetchTop10TracksTask();
        fetchTask.execute(id);
    }

    public class fetchTop10TracksTask extends AsyncTask<String, Void, Tracks> {

        private final String LOG_TAG = fetchTop10TracksTask.class.getSimpleName();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());


        @Override
        protected Tracks doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            HashMap query = new HashMap();
            query.put("country", "US");

            return spotify.getArtistTopTrack(params[0], query);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Fetching Top 10 Tracks...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            super.onPostExecute(tracks);

            progressDialog.dismiss();

            if (tracks.tracks.isEmpty()) {
                Toast.makeText(getActivity(), "No Tracks Found", Toast.LENGTH_SHORT).show();
            } else {
                List<Track> trackList = tracks.tracks;
                Iterator<Track> iterator = trackList.iterator();

                RowItem mTracks = null;
                rowItems.clear();

                while (iterator.hasNext()) {

                    Track trackSimple = iterator.next();
                    String name = trackSimple.name;
                    String albumName = trackSimple.album.name;

                    Log.v(LOG_TAG, "Track Name = " + name);
                    Log.v(LOG_TAG, "Track Album Name = " + albumName);

                    int thumb = trackSimple.album.images.size();

                    if (thumb != 0) {
                        String url = trackSimple.album.images.get(thumb-1).url;
                        mTracks = new RowItem(name, albumName, artistSportifyId, url);
                        rowItems.add(mTracks);
                    } else {
                        mTracks = new RowItem(name, albumName, artistSportifyId, "");
                        rowItems.add(mTracks);
                    }
                }

                tracksAdapter.notifyDataSetChanged();
            }
        }
    }
}

