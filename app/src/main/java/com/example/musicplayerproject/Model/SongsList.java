package com.example.musicplayerproject.Model;

public class SongsList {

    //Promenljive za naziv i putanju do pesme
    private String title;
    private String subTitle;
    private String path;

    //Getter
    public String getPath() {
        return path;
    }
    //Setter
    public void setPath(String path) {
        this.path = path;
    }
    //Konstruktor
    public SongsList(String title, String subTitle, String path) {
        this.title = title;
        this.subTitle = subTitle;
        this.path = path;

    }

    //Getter
    public String getTitle() {
        return title;
    }
    //Setter
    public void setTitle(String title) {
        this.title = title;
    }
    //Getter
    public String getSubTitle() {
        return subTitle;
    }

}
