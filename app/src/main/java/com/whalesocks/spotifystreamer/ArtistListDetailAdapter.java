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
 * Created by B on 6/24/2015.
 */
final class ArtistListDetailAdapter extends BaseAdapter {

    Context context;
    List<RowItem> artist;

    public ArtistListDetailAdapter (Context context, List<RowItem> items) {
        this.context = context;
        this.artist = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_result, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.list_image);
            holder.text = (TextView) view.findViewById(R.id.text_view_artist_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        RowItem rowItem = (RowItem) getItem(position);

        holder.text.setText(rowItem.getArtistName());

        String url = rowItem.getArtistImageURL();
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
        TextView text;
    }

    @Override
    public int getCount() {
        return artist.size();
    }

    @Override
    public Object getItem(int position) {
        return artist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
