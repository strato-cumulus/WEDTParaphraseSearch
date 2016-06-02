package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.TupledSentence;
import model.TupleConverter;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ParaphraseSearcher {

    public static void main(String[] args) {
        ParaphraseSearcher paraphraseSearcher = new ParaphraseSearcher();
        Collection<model.labelled.Sentence> sentences = paraphraseSearcher.loadLabelledSentences("ParaphraseSearcher/data/outParaphrasesTest");

        List<TupledSentence> tupledSentences = TupleConverter.fromSentences(sentences);
        List<TupledSentence> sample = tupledSentences.subList(0, 2);



        for (int i=0; i<sample.size(); i++) {
            for (int j=i+1; j<sample.size(); j++) {
                if (i==j) {
                    continue;
                }
                TupledSentence t1 = sample.get(i), t2 = sample.get(j);
                //System.out.println("wynik: " + t1.compare(t2) +" zdania: " + t1.sentence + " : " + t2.sentence);
            }
        }


    }


    public Collection<model.labelled.Sentence> loadLabelledSentences(String filePath) {

        LinkedList<model.labelled.Sentence> sentencesList = new LinkedList<>();

        try {
            Reader reader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
            BufferedReader buf = new BufferedReader(reader);

            String line;
            Gson gson = new GsonBuilder().create();
            while ((line = buf.readLine()) != null) {
                model.labelled.Sentence sentence = gson.fromJson(line, model.labelled.Sentence.class);
                sentencesList.add(sentence);
            }
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sentencesList;
    }

}

