package model;

import com.google.common.collect.ImmutableList;
import model.labelled.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class TupleConverter {

    public static List<TupledSentence> fromSentences(Iterable<Sentence> sentences) {
        return StreamSupport.stream(sentences.spliterator(), false).map(TupleConverter::fromSentence).collect(Collectors.toList());
    }

    public static TupledSentence fromSentence(Sentence sentence) {
        return new TupledSentence(String.join(" ", sentence.tokens), extractTuples(sentence));
    }

    private static List<Tuple> extractTuples(Sentence sentence) {
        List<Tuple> ret = new LinkedList<>();

        for (Frame frame : sentence.frames) {
            String targetType = frame.target.name;
            AnnotationSet set = frame.annotationSets.get(0); //i tak zawsze jest tylko jeden element - magia javaskryptu :3
            float targetScore = set.score;
            String targetText = frame.target.spans.get(0).text;

            List<TupleArgument> argumentList = new ArrayList<>();
            for (model.labelled.Argument a : set.arguments) {
                String argumentType = a.name;
                String argumentText = a.spans.get(0).text;
                TupleArgument tupleArgument = new TupleArgument(argumentType, argumentText);
                argumentList.add(tupleArgument);
            }

            Tuple t = new Tuple(targetScore, targetType, targetText, argumentList);
            ret.add(t);
        }
        return ret;
    }


}
