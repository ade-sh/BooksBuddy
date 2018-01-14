package com.adesh.projbk;

/**
 * Created by Karunakar on 05-Dec-17.
 */

public class Books {
    private String Title;

    public Books(String Title) {
        this.Title = Title;
    }

    public String getTitle() {
        return Title;
    }

    public void addTitle(String Title) {
        this.Title = Title;
    }
}
