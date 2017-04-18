package by.trafimchyk.universitiesapp.api;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
