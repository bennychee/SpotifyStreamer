package com.whalesocks.spotifystreamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TracksActivityFragment extends Fragment {

    public Top10TracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top10_tracks, container, false);

        String artistSportifyId;

        Intent intent = getActivity().getIntent();
        if (intent !=null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            artistSportifyId = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        return rootView;
    }

    public class fetchTop10TracksTask extends AsyncTask<String, Void, Tracks> {

        private final String LOG_TAG = fetchTop10TracksTask.class.getSimpleName();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());


        @Override
        protected Tracks doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            return spotify.getTracks(params[0]);

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

                while (iterator.hasNext()) {

                }
            }
        }
    }

}

