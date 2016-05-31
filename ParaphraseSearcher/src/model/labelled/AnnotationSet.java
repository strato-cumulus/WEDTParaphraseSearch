package model.labelled;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class AnnotationSet {

    @SerializedName("rank")
    @Expose
    public Integer rank;

    @SerializedName("score")
    @Expose
    public Float score;

    @SerializedName("frameElements")
    @Expose
    public List<Argument> arguments = new ArrayList<>();

}