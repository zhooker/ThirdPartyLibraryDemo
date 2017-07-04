package com.example.thirdparty.retrofit.cache;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entry {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "error=" + error +
                '}';
    }
}