package service;


import model.Tuple;
import model.TupleArgument;
import model.TupledSentence;
import model.labelled.Argument;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SentenceComparator {

    private static ThesaurusService thesaurusService = ThesaurusService.getInstance();

    public static float compare(TupledSentence tupledSentences1, TupledSentence tupledSentences2) {

        List<Tuple> l1 = new ArrayList<>(tupledSentences1.tuples),
                l2 = new ArrayList<>(tupledSentences2.tuples);

        List<Pair<Tuple, Tuple>> pairedTuples = new ArrayList<>();

        //po ponizszej petli w liscie pairedTuples sa sparowane tuple, a w listach l1 i l2 pozostale.
        Iterator<Tuple> it1 = l1.iterator();
        while (it1.hasNext()) {
            Tuple outer = it1.next();
            Iterator<Tuple> it2 = l2.iterator();
            while (it2.hasNext()) {
                Tuple inner = it2.next();
                if (outer.targetType.equalsIgnoreCase(inner.targetType)) {   //porownanie typow argumentow; mozna dac bonus jesli zgadza sie tez text
                    pairedTuples.add(new ImmutablePair<>(outer, inner));
                    it1.remove();
                    it2.remove();
                    break;
                }
            }
        }

        return comparePaired(pairedTuples);

        //return 0f;
    }

    private static float comparePaired(List<Pair<Tuple, Tuple>> pairedTuples){
        //moze warto brac jeszcze pod uwage score obliczony przez parser?
        if(pairedTuples.isEmpty()){
            return 0;
        }

        float score = 0;
        float normalizationFactor = 0;

        for (Pair<Tuple, Tuple> pair: pairedTuples)  {

            Tuple outer = pair.getLeft(), inner = pair.getRight();
            if(inner.arguments.isEmpty() && outer.arguments.isEmpty()){
                score += 1.5; //teoretycznie mozna dzieki temu wyjsc poza skale, ale jezeli targety sie zgadzaja i nie maja arguementow to chyba mozna uznac to za pewnik
                normalizationFactor+=1;
                continue;
            }

            Iterator<TupleArgument> it1 = outer.arguments.iterator();
            while (it1.hasNext()){
                TupleArgument arg1 = it1.next();
                Iterator<TupleArgument> it2 = inner.arguments.iterator();
                while(it2.hasNext()){
                    TupleArgument arg2 = it2.next();
                    if(arg1.argumentType.equalsIgnoreCase(arg2.argumentType)){
                        if(argumentsEqual(arg1, arg2)){
                            score +=1;
                            it1.remove();   //po tym zostaja tuple ogolocone z argumentow (no chyba ze argumenty sie nie sparowaly)
                            it2.remove();
                        }
                        normalizationFactor +=1;
                    }

                }
            }
        }

        return score/normalizationFactor;
    }

    //przemyslec to jak to ma dzialac
    private static boolean argumentsEqual(TupleArgument arg1, TupleArgument arg2) {
        return arg1.argumentText.equalsIgnoreCase(arg2.argumentText)
                || thesaurusService.get(arg1.argumentText).getFlatContents().contains(arg2.argumentText);
    }

}
