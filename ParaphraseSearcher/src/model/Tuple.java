package model;

import com.google.common.collect.ImmutableList;
import service.ThesaurusService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

public class Tuple {

    private ThesaurusService thesaurusService = ThesaurusService.getInstance();

    private final String targetType;
    private final ImmutableList<String> targetSubjects;
    private final ImmutableList<Predicate> predicates;

    Tuple(String targetType, ImmutableList<String> targetSubjects, ImmutableList<Predicate> predicates) {
        this.targetType = targetType;
        this.targetSubjects = targetSubjects;
        this.predicates = predicates;
    }

    public int compare(Tuple other) {
        try {
            if(this.targetType.equals(other.targetType)) {
                int score = 17;
                score += scoreLists(targetSubjects, other.targetSubjects);
                return score;
            }
            return 0;
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

    private int scoreArguments(Iterable<Predicate> arguments, Iterable<Predicate> others) throws IOException, SQLException {
        int score = 0;
        for(Predicate predicate : arguments) {
            for(Predicate other: others) {
                score += scoreIsSynonym(predicate.type, other.type);
            }
        }
        return score;
    }
}
