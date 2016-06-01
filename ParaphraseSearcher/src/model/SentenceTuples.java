package model;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class SentenceTuples implements Iterable<Tuple> {
    public final String sentence;
    public final ImmutableList<Tuple> tuples;

    public SentenceTuples(String sentence, ImmutableList<Tuple> tuples) {
        this.sentence = sentence;
        this.tuples = tuples;
    }

    public SentenceTuples(String sentence, Iterable<Tuple> tuples) {
        ImmutableList.Builder<Tuple> builder = ImmutableList.builder();
        builder.addAll(tuples);
        this.sentence = sentence;
        this.tuples = builder.build();
    }

    @Override
    public Iterator<Tuple> iterator() {
        return tuples.iterator();
    }

    @Override
    public void forEach(Consumer<? super Tuple> action) {
        tuples.forEach(action);
    }

    @Override
    public Spliterator<Tuple> spliterator() {
        return tuples.spliterator();
    }

    public int compare(SentenceTuples others) {
        int score = 0;
        for(Tuple tuple: tuples) {
            for(Tuple other: others) {
                score += tuple.compare(other);
            }
        }
        return score;
    }
}
