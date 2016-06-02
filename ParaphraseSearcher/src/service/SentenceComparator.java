package service;


import model.Tuple;
import model.TupleArgument;
import model.TupledSentence;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SentenceComparator {

    private static ThesaurusService thesaurusService = ThesaurusService.getInstance();
    private static final float PENALTY_UNPAIRED_TUPLE_COUNT = 0.78f;


    public static float compare(TupledSentence tupledSentence1, TupledSentence tupledSentence2, boolean printResults) {

        List<Tuple> l1 = new ArrayList<>(tupledSentence1.tuples),
                l2 = new ArrayList<>(tupledSentence2.tuples);

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



        float penaltyFactor = 1.0f;
        if(l1.size() > 0 && l2.size() > 0){
            int unpairedDifference = l1.size() + l2.size();
            penaltyFactor *= Math.pow(PENALTY_UNPAIRED_TUPLE_COUNT, unpairedDifference);
        }

        float returnedScore = comparePaired(pairedTuples) * penaltyFactor;

        if(printResults){
            System.out.println(tupledSentence1.sentence);
            System.out.println(tupledSentence2.sentence);
            System.out.println("Wynik: "+returnedScore);
            System.out.println();
        }
        return returnedScore;

    }

    private static float comparePaired(List<Pair<Tuple, Tuple>> pairedTuples){
        //moze warto brac jeszcze pod uwage score obliczony przez parser?
        if(pairedTuples.isEmpty()){
            return 0;
        }

        float score = 0;
        float normalizationFactor = 0;
        float penaltyFactor = 1f;

        for (Pair<Tuple, Tuple> pair: pairedTuples)  {

            Tuple outer = pair.getLeft(), inner = pair.getRight();
            if(inner.arguments.isEmpty() && outer.arguments.isEmpty()){
                score += 1.5; //teoretycznie mozna dzieki temu wyjsc poza skale, ale jezeli targety sie zgadzaja i nie maja arguementow to chyba mozna uznac to za pewnik
                normalizationFactor+=1;
                continue;
            }
            else if(!areSimilar(outer.targetText, inner.targetText)){
                penaltyFactor /=2;  //duza kara w przypadku gdy zdania maja podobna konstrukcje(te same typy), ale inne argumenty (inan sprawa ze cos nie dziala ta kara xd)
                normalizationFactor++;
                continue;
            }

            Iterator<TupleArgument> it1 = outer.arguments.iterator();
            while (it1.hasNext()){
                TupleArgument arg1 = it1.next();
                Iterator<TupleArgument> it2 = inner.arguments.iterator();
                while(it2.hasNext()){
                    TupleArgument arg2 = it2.next();
                    if(arg1.argumentType.equalsIgnoreCase(arg2.argumentType)){
                        if(areSimilar(arg1.argumentText, arg2.argumentText)){   //pomyslec nad lepszym algorytmem
                            score +=1;// * (inner.score + outer.score);
                            //it1.remove();   //po tym zostaja tuple ogolocone z argumentow (no chyba ze argumenty sie nie sparowaly)
                            //it2.remove();
                        }
                        normalizationFactor +=1;
                    }
                }
            }
        }

        float parserScoreFactor = 0;
        for(Pair<Tuple, Tuple> p : pairedTuples){
            parserScoreFactor +=p.getLeft().score;
            parserScoreFactor +=p.getRight().score;
        }


        return score/**penaltyFactor*//normalizationFactor;///parserScoreFactor;
    }



    private static boolean areSimilar(String text1, String text2){
        return text1.equalsIgnoreCase(text2)
                || thesaurusService.get(text1).getFlatContents().contains(text2);
    }

}
