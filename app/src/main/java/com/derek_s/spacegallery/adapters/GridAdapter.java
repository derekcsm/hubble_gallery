package com.derek_s.spacegallery.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek_s.spacegallery.R;
import com.derek_s.spacegallery.model.TileObject;
import com.derek_s.spacegallery.utils.ui.FontFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dereksmith on 15-02-26.
 */
public class GridAdapter extends BaseAdapter {

    public static String TAG = "ChannelAdapter";
    private Activity activity;
    Context context;
    private static LayoutInflater inflater = null;
    public ArrayList<TileObject> mTiles;

    public GridAdapter(Activity a, Context context) {
        activity = a;
        this.context = context;
        mTiles = new ArrayList<>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItems(ArrayList<TileObject> tiles) {
        mTiles = new ArrayList<>();
        mTiles.addAll(tiles);
        notifyDataSetChanged();
    }

    public void addItemsToBottom(ArrayList<TileObject> tiles) {
        mTiles.addAll(tiles);
        notifyDataSetChanged();
    }

    public void clear() {
        mTiles.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public TileObject getItem(int pos) {
        return this.mTiles.get(pos);
    }

    public int getCount() {
        return this.mTiles.size();
    }


    static class ViewHolder {
        @InjectView(R.id.iv_tile)
        ImageView ivTile;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_tile, null);
            holder = new ViewHolder(view);
            holder.ivTile = (ImageView) view.findViewById(R.id.iv_tile);
            view.setTag(holder);
        }

       holder.tvTitle.setTypeface(FontFactory.getCondensedRegular(context));
        holder.tvTitle.setText(getItem(position).getTitle());
       Picasso.with(context).load(getItem(position).getSrc()).into(holder.ivTile);

        return view;
    }

}