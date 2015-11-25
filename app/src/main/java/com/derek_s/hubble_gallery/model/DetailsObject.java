package com.derek_s.hubble_gallery.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dereksmith on 15-03-15.
 */
public class DetailsObject extends SerializableModel{
    @Expose
    private String description;
    @Expose
    private String names;
    @SerializedName("image_type")
    @Expose
    private String imageType;
    @Expose
    private String credit;

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The names
     */
    public String getNames() {
        return names;
    }

    /**
     * @param names The names
     */
    public void setNames(String names) {
        this.names = names;
    }

    /**
     * @return The imageType
     */
    public String getImageType() {
        return imageType;
    }

    /**
     * @param imageType The image_type
     */
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    /**
     * @return The credit
     */
    public String getCredit() {
        return credit;
    }

    /**
     * @param credit The credit
     */
    public void setCredit(String credit) {
        this.credit = credit;
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
