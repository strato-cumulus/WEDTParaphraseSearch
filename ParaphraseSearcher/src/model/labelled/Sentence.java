package model.labelled;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Sentence {

    @SerializedName("frames")
    @Expose
    public List<Frame> frames = new ArrayList<>();

    @SerializedName("tokens")
    @Expose
    public List<String> tokens = new ArrayList<>();

}