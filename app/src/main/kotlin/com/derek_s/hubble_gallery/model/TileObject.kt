package com.derek_s.hubble_gallery.model

import com.google.gson.Gson

class TileObject {

  var id: String = ""
  var title: String = ""
  var href: String = ""
  var src: String = ""

  fun serialize(): String {
    val gson = Gson()
    return gson.toJson(this)
  }

  companion object {
    fun create(serializedData: String): TileObject {
      val gson = Gson()
      return gson.fromJson(serializedData, TileObject::class.java)
    }
  }
}