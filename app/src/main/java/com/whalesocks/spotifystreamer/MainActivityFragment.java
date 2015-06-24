package com.whalesocks.spotifystreamer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private List<RowItem> rowItems;
    ArtistListDetailAdapter artistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final String LOG_TAG = getActivity().getLocalClassName();

        rowItems = new ArrayList<RowItem>();

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artist_search_result);
        artistAdapter = new ArtistListDetailAdapter(getActivity(),rowItems);
        listView.setAdapter(artistAdapter);

        final EditText searchEditText = (EditText) rootView.findViewById(R.id.search_artist_text);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //hide keyboard after enter key
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    searchForArtist(v.getText().toString());
                    Log.v(LOG_TAG, v.getText().toString());
                    return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String artistSpotifyId = rowItems.get(position).getSpotifyId();
                String artistName = rowItems.get(position).getArtistName();
                Log.v(LOG_TAG, "artist = " + artistName);
                Intent intent = new Intent(getActivity(), Top10TracksActivity.class);
                intent.putExtra("spotifyId", artistSpotifyId);
                intent.putExtra("artist", artistName);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private void searchForArtist (String searchArtistName) {

        rowItems.clear();
        SearchFetchArtistTask searchTask = new SearchFetchArtistTask();
        searchTask.execute(searchArtistName);
    }

    public class SearchFetchArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        private final String LOG_TAG = SearchFetchArtistTask.class.getSimpleName();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected ArtistsPager doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            ArtistsPager results = spotify.searchArtists(params[0]);

            return results;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            super.onPostExecute(artistsPager);

            progressDialog.dismiss();

            if (artistsPager.artists.items.isEmpty()) {
                Toast.makeText(getActivity(), "No Artist Found", Toast.LENGTH_SHORT).show();
            } else {

                List<Artist> artistList = artistsPager.artists.items;
                Iterator<Artist> iterator = artistList.iterator();

                RowItem mArtist = null;
                rowItems.clear();

                while (iterator.hasNext()) {

                    Artist artist = iterator.next();

                    int thumb = artist.images.size();
                    if (thumb != 0) {

                        //Get the smallest size image in the image list
                        String url = artist.images.get(thumb - 1).url;
                        mArtist = new RowItem(artist.name, artist.id, url);
                        rowItems.add(mArtist);

                    } else {
                        //No image for this Artist
                        mArtist = new RowItem(artist.name, artist.id, "");
                        rowItems.add(mArtist);
                    }
                }

                artistAdapter.notifyDataSetChanged();
            }
        }
    }
}
