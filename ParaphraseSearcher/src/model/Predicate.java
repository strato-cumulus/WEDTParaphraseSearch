package model;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.util.EditDistance;

public class Predicate {
    private static EditDistance editDistance;
    public final String type;
    public final ImmutableList<String> subjects;

    Predicate(String type, ImmutableList<String> subjects) {
        this.type = type;
        this.subjects = subjects;
    }

    public int compare(Predicate other) {
        if(!this.type.equals(other.type)) {
            return 0;
        }
        int score = 0;
        for(int i = 0; i < subjects.size(); ++i) {
            for(int j = i+1; j < other.subjects.size(); ++j) {
                int distance = (int) editDistance.score(subjects.get(i), other.subjects.get(j));
                if(distance <= 2) {
                    score += 10;
                }
            }
        }
        return score;
    }
}
