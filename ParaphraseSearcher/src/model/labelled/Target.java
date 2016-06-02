package model.labelled;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Target {

    @SerializedName("type")
    @Expose
    public String name;

    @SerializedName("subjects")
    @Expose
    public List<Span> spans = new ArrayList<>();

}