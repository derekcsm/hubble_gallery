package com.derek_s.hubble_gallery.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by dereksmith on 15-03-03.
 */
public class Tiles extends SerializableModel{

    private ArrayList<TileObject> tiles = new ArrayList<>();

    /**
     * @return The tiles
     */
    public ArrayList<TileObject> getTiles() {
        return tiles;
    }

    /**
     * @param tiles The chats
     */
    public void setTiles(ArrayList<TileObject> tiles) {
        this.tiles = tiles;
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        super();
    }

    static public DetailsObject create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
            super(serializedData);
    }
}
