package com.derek_s.hubble_gallery.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery._shared.model.TileObject;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.RecyclerViewHolder> {

  private static LayoutInflater inflater = null;
  public ArrayList<TileObject> mTiles;
  private final Context context;
  private final Listener listener;

  public MainGridAdapter(Activity activity, Context context, Listener listener) {
    this.mTiles = new ArrayList<>();
    this.context = context;
    this.listener = listener;
    inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public interface Listener {
    void onItemClicked(TileObject tileObject);
  }

  @Override
  public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.item_tile, parent, false);
    return new RecyclerViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
    holder.tvTitle.setTypeface(FontFactory.getCondensedRegular(context));
    holder.tvTitle.setText(getItemAtPosition(position).getTitle());
    Picasso.with(context).load(getItemAtPosition(position).getSrc()).into(holder.ivTile);

    holder.rlBg.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onItemClicked(getItemAtPosition(position));
      }
    });
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

    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.iv_tile)
    ImageView ivTile;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public RecyclerViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

  }
}
