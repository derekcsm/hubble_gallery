package com.derek_s.hubble_gallery.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery._shared.model.TileObject;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.RecyclerViewHolder> {

  private static LayoutInflater inflater = null;
  public ArrayList<TileObject> mTiles;
  Context context;


  public MainGridAdapter(Activity activity, Context context) {

    this.mTiles = new ArrayList<>();
    this.context = context;
    inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }


  @Override
  public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = inflater.inflate(R.layout.item_tile, parent, false);

    return new RecyclerViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    holder.tvTitle.setTypeface(FontFactory.getCondensedRegular(context));
    holder.tvTitle.setText(mTiles.get(position).getTitle());
    Picasso.with(context).load(mTiles.get(position).getSrc()).into(holder.ivTile);

  }


  @Override
  public int getItemCount() {
    return mTiles.size();
  }

  public void addItems(ArrayList<TileObject> tiles) {
    mTiles.addAll(tiles);
    notifyDataSetChanged();
  }

  public void clear() {
    mTiles.clear();
    notifyDataSetChanged();
  }

  public TileObject getItemAtPosition(int position) {
    return mTiles.get(position);
  }

  public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.iv_tile)
    ImageView ivTile;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    public RecyclerViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }


  }
}
