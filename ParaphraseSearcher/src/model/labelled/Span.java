
package model.labelled;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Span {

/*
    @SerializedName("start")
    @Expose
    public Integer start;

    @SerializedName("end")
    @Expose
    public Integer end;
*/

    @SerializedName("text")
    @Expose
    public String text;

}