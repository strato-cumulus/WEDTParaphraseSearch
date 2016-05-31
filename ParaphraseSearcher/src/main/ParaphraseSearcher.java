package main;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.stanford.nlp.util.Iterables;
import model.SentenceTuples;
import model.TupleConverter;

public class ParaphraseSearcher {

    public static void main(String[] args) {
        ParaphraseSearcher paraphraseSearcher = new ParaphraseSearcher();
        Collection<model.labelled.Sentence> sentences = paraphraseSearcher.loadLabelledSentences("ParaphraseSearcher/data/outParaphrases");
        Iterable<SentenceTuples> sentenceTuples = TupleConverter.fromSentences(sentences);
        Iterable<SentenceTuples> sample = Iterables.take(sentenceTuples, 3);
        Iterator<SentenceTuples> sampleIterator = sample.iterator();
        sampleIterator.next();
        int score = sampleIterator.next().compare(sampleIterator.next());
        System.out.println(score);
    }

    public Collection<model.labelled.Sentence> loadLabelledSentences(String filePath){

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
        }
        catch(IOException  e){
            e.printStackTrace();
        }

        return sentencesList;
    }

}

