package model.labelled;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Frame {

    @SerializedName("target")
    @Expose
    public Target target;

    @SerializedName("annotationSets")
    @Expose
    public List<AnnotationSet> annotationSets = new ArrayList<>();

}