package com.whalesocks.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bchee on 24-Jun-15.
 */
public class Top10TracksAdapter extends BaseAdapter {

    Context context;
    List<RowItem> tracks;

    public Top10TracksAdapter (Context context, List<RowItem> items) {
        this.context = context;
        this.tracks = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_top10_tracks, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.image_view_top10_tracks);
            holder.trackName = (TextView) view.findViewById(R.id.text_view_top10_track_name);
            holder.albumName = (TextView) view.findViewById(R.id.text_view_top10_track_album_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        RowItem rowItem = (RowItem) getItem(position);

        holder.trackName.setText(rowItem.getTrackName());
        holder.albumName.setText(rowItem.getAlbumName());

        String url = rowItem.getAlbumImageURL();
        if (url.isEmpty()) {
            holder.image.setImageResource(R.drawable.icon_no_images);
        }
        else {
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .tag(context)
                    .into(holder.image);
        }
        return view;
    }

    static class ViewHolder {
        ImageView image;
        TextView trackName;
        TextView albumName;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

