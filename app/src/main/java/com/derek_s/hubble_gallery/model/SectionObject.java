package com.derek_s.hubble_gallery.model;

/**
 * Created by dereksmith on 15-03-05.
 */
public class SectionObject extends SectionChildObject{

    private boolean isExpandable;

    /**
     * @return The sectionTitle
     */
    public String getSectionTitle() {
        super();
    }

    /**
     * @param sectionTitle The section_title
     */
    public void setSectionTitle(String sectionTitle) {
        super(sectionTitle);
    }

    /**
     * @return The query
     */
    public String getQuery() {
        super();
    }

    /**
     * @param query The query
     */
    public void setQuery(String query) {
        super(query);
    }

    public void setExpandable(boolean expandable) {
        this.isExpandable = expandable;
    }

    public boolean getIsExpandable() {
        return isExpandable;
    }
}
