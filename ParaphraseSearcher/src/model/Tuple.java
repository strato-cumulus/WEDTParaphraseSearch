package model;

import edu.stanford.nlp.trees.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Tuple {
    private static HeadFinder headFinder = new UniversalSemanticHeadFinder();

    private Tree head;
    private List<Tree> arguments;

    private Tuple(Tree sentence) {
        arguments = new ArrayList<>(sentence.size() - 1);
        head = headFinder.determineHead(sentence);
        for (Tree argument : sentence.children()) {
            if (!argument.equals(head)) {
                arguments.add(argument);
            }
        }
    }

    private void getSimpleTuples(Tree sentence, Collection<Tuple> tuples) {
        if (head != null) {
            if (head.isPhrasal()) {
                for (Tree tree : arguments) {
                    if (!tree.isLeaf()) {
                        Tuple tuple = new Tuple(tree);
                        tuple.getSimpleTuples(tuple.head, tuples);
                    }
                }
                Tuple tuple = new Tuple(head);
                tuple.getSimpleTuples(tuple.head, tuples);
            } else {
                tuples.add(this);
            }
        }
    }

    public static Collection<Tuple> createSimpleTuplesFromSentence(Tree sentence) {
        Collection<Tuple> tuples = new ArrayList<>();
        (new Tuple(sentence)).getSimpleTuples(sentence, tuples);
        return tuples;
    }
}
