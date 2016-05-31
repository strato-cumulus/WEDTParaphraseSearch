package model;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import service.ThesaurusService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

public class Tuple {

    private ThesaurusService thesaurusService = ThesaurusService.getInstance();

    private final String name;
    private final ImmutableList<String> spans;
    private final ImmutableList<Argument> arguments;

    Tuple(String name, ImmutableList<String> spans, ImmutableList<Argument> arguments) {
        this.name = name;
        this.spans = spans;
        this.arguments = arguments;
    }

    public int compare(Tuple other) {
        try {
            int score = 17 * (this.name.equals(other.name)? 0: 1);
            score += scoreLists(spans, other.spans);
            score += scoreArguments(arguments, other.arguments);
            return score;
        }
        catch (Exception e) {
            return 0;
        }
    }

    private int scoreIsSynonym(String word, String other) throws IOException, SQLException {
        return word.equalsIgnoreCase(other)?
                thesaurusService.get(word).getFlatContents().contains(other)?
                        10:
                        0:
                0;
    }

    private int scoreLists(Iterable<String> spans, Iterable<String> others) throws IOException, SQLException {
        int score = 0;
        Iterator<String> spansIterator = spans.iterator();
        Iterator<String> othersIterator = others.iterator();
        while(spansIterator.hasNext() && othersIterator.hasNext()) {
            String span = spansIterator.next();
            String other = othersIterator.next();
            score += scoreIsSynonym(span, other);
        }
        Iterator<String> remaining = spansIterator.hasNext()? spansIterator: othersIterator;
        while(remaining.hasNext()) {
            remaining.next();
            score -= 10;
        }
        return score;
    }

    private int scoreArguments(Iterable<Argument> arguments, Iterable<Argument> others) throws IOException, SQLException {
        int score = 0;
        for(Argument argument: arguments) {
            for(Argument other: others) {
                score += scoreIsSynonym(argument.name, other.name);
            }
        }
        return score;
    }
}
