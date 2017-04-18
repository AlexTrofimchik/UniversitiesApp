package by.trafimchyk.universitiesapp.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse<T> {

    @SerializedName("response")
    private Response<T> response;

    public List<T> getItems() {
        return response.items;
    }

    private class Response<R> {
        @SerializedName("count")
        long count;
        @SerializedName("items")
        List<R> items;
    }
}
