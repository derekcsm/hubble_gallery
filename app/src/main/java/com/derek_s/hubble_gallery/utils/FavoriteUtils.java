package com.derek_s.hubble_gallery.utils;

import android.content.Context;

import com.derek_s.hubble_gallery.base.TinyDB;
import com.derek_s.hubble_gallery.model.TileObject;
import com.derek_s.hubble_gallery.model.Tiles;

import java.util.ArrayList;

/**
 * Created by dereksmith on 15-04-23.
 */
public class FavoriteUtils {

    private static final String FAVORITES_KEY = "favorites_key";
    TinyDB tinyDB;
    private Context context;

    public FavoriteUtils(Context context) {
        this.context = context;
    }

    public void saveFavorite(TileObject tileObject) {
        tinyDB = getTinyDB();
        Tiles currentFaves = Tiles.create(tinyDB.getString(FAVORITES_KEY));
        if (currentFaves == null)
            currentFaves = new Tiles();

        if (currentFaves.getTiles() == null)
            currentFaves.setTiles(new ArrayList<TileObject>());

        currentFaves.getTiles().add(tileObject);

        tinyDB.putString(FAVORITES_KEY, currentFaves.serialize());
    }

    public Tiles getFavorites() {
        tinyDB = getTinyDB();
        return Tiles.create(tinyDB.getString(FAVORITES_KEY));
    }

    public void removeFavorite(TileObject tileObject) {
        tinyDB = getTinyDB();
        Tiles currentFaves = Tiles.create(tinyDB.getString(FAVORITES_KEY));

        for (int i = 0; i < currentFaves.getTiles().size(); i++) {
            TileObject curTile = currentFaves.getTiles().get(i);

            if (curTile.getId().equals(tileObject.getId()))
                currentFaves.getTiles().remove(i);
        }

        tinyDB.putString(FAVORITES_KEY, currentFaves.serialize());
    }

    public boolean isFavorited(TileObject tileObject) {
        tinyDB = getTinyDB();
        Tiles currentFaves = Tiles.create(tinyDB.getString(FAVORITES_KEY));

        if (currentFaves == null)
            return false;

        for (TileObject curTile : currentFaves.getTiles()) {
            if (curTile.getId().equals(tileObject.getId()))
                return true;
        }
        return false;
    }

    private TinyDB getTinyDB() {
        if (tinyDB == null) {
            tinyDB = new TinyDB(context);
        }
        return tinyDB;
    }


}
