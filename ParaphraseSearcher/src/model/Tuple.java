package model;

import com.google.common.collect.ImmutableList;
import service.ThesaurusService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

public class Tuple {

    private ThesaurusService thesaurusService = ThesaurusService.getInstance();

    private final String name;
    private final ImmutableList<String> spans;
    private final ImmutableList<Argument> predicates;

    Tuple(String name, ImmutableList<String> spans, ImmutableList<Argument> predicates) {
        this.name = name;
        this.spans = spans;
        this.predicates = predicates;
    }

    public int compare(Tuple other) {
        try {
            int score = this.name.equals(other.name)? 17: 0;
            score += scoreLists(spans, other.spans);
            score += scoreArguments(predicates, other.predicates);
            return score;
        }
        catch (Exception e) {
            return 0;
        }
    }

    private int scoreIsSynonym(String word, String other) throws IOException, SQLException {
        if(word.equalsIgnoreCase(other)){
            return 10;
        }else{
            return thesaurusService.get(word).getFlatContents().contains(other) ? 10 : 0;
        }
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
/*        while(remaining.hasNext()) {
            remaining.next();
            score -= 10;
        }*/
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
