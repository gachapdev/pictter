package com.elzup.pictter.pictter;

public class NavDrawerItem {
    private boolean isFavorite;
    private String name;
    // TODO: add icon

    NavDrawerItem(boolean isFavorite, String name) {
        this.isFavorite = isFavorite;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void toggle() {
        this.isFavorite = !this.isFavorite;
    }
}
