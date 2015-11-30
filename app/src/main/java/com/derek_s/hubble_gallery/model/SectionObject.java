package com.derek_s.hubble_gallery.model;

/**
 * Created by dereksmith on 15-03-05.
 */
public class SectionObject extends SectionChildObject{

    private boolean isExpandable;

    public void setExpandable(boolean expandable) {
        this.isExpandable = expandable;
    }

    public boolean getIsExpandable() {
        return isExpandable;
    }
}
