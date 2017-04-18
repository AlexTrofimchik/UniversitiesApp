package by.trafimchyk.universitiesapp.api;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;

    public Item() {
    }

    public Item(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
