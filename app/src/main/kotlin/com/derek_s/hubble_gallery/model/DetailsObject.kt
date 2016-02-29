package com.derek_s.hubble_gallery.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class DetailsObject {

  @Expose
  var description: String = ""

  @Expose
  var names: String = ""

  @SerializedName("image_type")
  @Expose
  var imageType: String = ""

  @Expose
  var credit: String = ""

  fun serialize(): String {
    val gson = Gson()
    return gson.toJson(this)
  }

  companion object {
    fun create(serializedData: String): DetailsObject {
      val gson = Gson()
      return gson.fromJson(serializedData, DetailsObject::class.java)
    }
  }
}