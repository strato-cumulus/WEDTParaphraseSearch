package model;

import com.google.common.collect.ImmutableList;
import model.labelled.AnnotationSet;
import model.labelled.Frame;
import model.labelled.Sentence;
import model.labelled.Span;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by strato on 31.05.16.
 */
public class TupleConverter {

    public static List<SentenceTuples> fromSentences(Iterable<Sentence> sentences) {
        return StreamSupport.stream(sentences.spliterator(), false).map(TupleConverter::fromSentence).collect(Collectors.toList());
    }

    public static SentenceTuples fromSentence(Sentence sentence) {
        ImmutableList.Builder<Tuple> builder = ImmutableList.builder();
        for (Frame frame : sentence.frames) {
            String name = frame.target.name;
            ImmutableList<String> spans = fromSpans(frame.target.spans);
            ImmutableList<Argument> arguments = fromAnnotationSet(frame.annotationSets);
            builder.add(new Tuple(name, spans, arguments));
        }
        return new SentenceTuples(String.join(" ", sentence.tokens), builder.build());
    }

    private static ImmutableList<Argument> fromAnnotationSet(Iterable<AnnotationSet> annotationSets) {
        ImmutableList.Builder<Argument> builder = ImmutableList.builder();
        for (AnnotationSet annotationSet : annotationSets) {
            for (model.labelled.Argument argument : annotationSet.arguments) {
                builder.add(new Argument(argument.name, fromSpans(argument.spans)));
            }
        }
        return builder.build();
    }

    private static ImmutableList<String> fromSpans(Iterable<Span> spans) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (Span span : spans) {
            builder.add(span.text);
        }
        return builder.build();
    }
}
