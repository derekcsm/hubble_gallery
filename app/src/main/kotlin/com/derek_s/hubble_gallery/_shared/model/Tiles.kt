package com.derek_s.hubble_gallery._shared.model

import com.google.gson.Gson
import java.util.*

class Tiles {

  var tiles: ArrayList<TileObject> = ArrayList()

  fun serialize(): String {
    val gson = Gson()
    return gson.toJson(this)
  }

}