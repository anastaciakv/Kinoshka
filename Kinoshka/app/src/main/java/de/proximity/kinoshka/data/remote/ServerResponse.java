package de.proximity.kinoshka.data.remote;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponse<T> {
    public int id;
    public int page;
    @SerializedName("results")
    public List<T> items;
    public int totalPages;
    public int totalResults;
}
