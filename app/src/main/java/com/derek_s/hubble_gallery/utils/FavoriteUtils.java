package com.derek_s.hubble_gallery.utils;

import com.derek_s.hubble_gallery.base.TinyDB;
import com.derek_s.hubble_gallery.model.TileObject;
import com.derek_s.hubble_gallery.model.Tiles;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FavoriteUtils {

  private static final String FAVORITES_KEY = "favorites_key";

  @Inject
  TinyDB tinyDB;

  @Inject
  public FavoriteUtils() {
  }

  public void saveFavorite(TileObject tileObject) {
    Tiles currentFaves = Tiles.Companion.create(tinyDB.getString(FAVORITES_KEY));
    if (currentFaves == null)
      currentFaves = new Tiles();

    if (currentFaves.getTiles() == null)
      currentFaves.setTiles(new ArrayList<>());

    currentFaves.getTiles().add(tileObject);

    tinyDB.putString(FAVORITES_KEY, currentFaves.serialize());
  }

  public Tiles getFavorites() {
    return Tiles.Companion.create(tinyDB.getString(FAVORITES_KEY));
  }

  public void removeFavorite(TileObject tileObject) {
    Tiles currentFaves = Tiles.Companion.create(tinyDB.getString(FAVORITES_KEY));

    for (int i = 0; i < currentFaves.getTiles().size(); i++) {
      TileObject curTile = currentFaves.getTiles().get(i);

      if (curTile.getId().equals(tileObject.getId()))
        currentFaves.getTiles().remove(i);
    }

    tinyDB.putString(FAVORITES_KEY, currentFaves.serialize());
  }

  public boolean isFavorited(TileObject tileObject) {
    Tiles currentFaves = Tiles.Companion.create(tinyDB.getString(FAVORITES_KEY));

    if (currentFaves == null)
      return false;

    for (TileObject curTile : currentFaves.getTiles()) {
      if (curTile.getId().equals(tileObject.getId()))
        return true;
    }
    return false;
  }
}
