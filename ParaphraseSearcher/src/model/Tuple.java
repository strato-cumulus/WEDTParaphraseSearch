package model;

import service.ThesaurusService;

import java.util.List;

public class Tuple {

    public final String targetType;

    public final String targetText;

    public final List<TupleArgument> arguments;

    public final float score;


    public Tuple(float score, String targetType, String targetText, List<TupleArgument> arguments) {
        this.score = score;
        this.targetType = targetType;
        this.targetText = targetText;
        this.arguments = arguments;
    }


}
