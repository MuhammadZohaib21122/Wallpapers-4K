package com.example.a4k8kwallpaper;

import androidx.annotation.NonNull;

public class Wallpaper {
    private int no;
    private String imageToUse;
    private String imageToShow;

    public Wallpaper(int no, String imageToUse, String imageToShow) {
        this.no = no;
        this.imageToUse = imageToUse;
        this.imageToShow = imageToShow;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getImageToUse() {
        return imageToUse;
    }

    public void setImageToUse(String imageToUse) {
        this.imageToUse = imageToUse;
    }

    public String getImageToShow() {
        return imageToShow;
    }

    public void setImageToShow(String imageToShow) {
        this.imageToShow = imageToShow;
    }

    @NonNull
    @Override
    public String toString() {
        return "Wallpaper{" +
                "no=" + no +
                ", imageToUse='" + imageToUse + '\'' +
                ", imageToShow='" + imageToShow + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallpaper wallpaper = (Wallpaper) o;

        if (no != wallpaper.no) return false;
        if (!imageToUse.equals(wallpaper.imageToUse)) return false;
        return imageToShow.equals(wallpaper.imageToShow);
    }

    @Override
    public int hashCode() {
        int result =no;
        result = 31 * result + imageToUse.hashCode();
        result = 31 * result + imageToShow.hashCode();
        return result;
    }
}
