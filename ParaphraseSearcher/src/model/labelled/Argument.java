package model.labelled;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Argument {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("spans")
    @Expose
    public List<Span> spans = new ArrayList<>();

}
