package com.derek_s.hubble_gallery.model

import com.google.gson.Gson
import java.util.*

class Tiles {

  var tiles: ArrayList<TileObject> = ArrayList()

  fun serialize(): String {
    val gson = Gson()
    return gson.toJson(this)
  }

  companion object {
    fun create(serializedData: String): Tiles {
      val gson = Gson()
      return gson.fromJson(serializedData, Tiles::class.java)
    }
  }
}