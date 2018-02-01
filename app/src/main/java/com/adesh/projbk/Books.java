package com.adesh.projbk;

import android.graphics.Bitmap;

public class Books {
    public Bitmap bitmap1, bitmap2, bitmap3;
    private String Title;

    public Books(String Title, Bitmap bitmap1) {
        this.Title = Title;
        this.bitmap1 = bitmap1;
    }

    public String getTitle() {
        return Title;
    }

    public void addTitle(String Title) {
        this.Title = Title;
    }

    public Bitmap getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        this.bitmap1 = bitmap1;
    }

    public Bitmap getBitmap2() {
        return bitmap2;
    }

    public void setBitmap2(Bitmap bitmap2) {
        this.bitmap2 = bitmap2;
    }

    public Bitmap getBitmap3() {
        return bitmap3;
    }

    public void setBitmap3(Bitmap bitmap3) {
        this.bitmap3 = bitmap3;
    }
}
