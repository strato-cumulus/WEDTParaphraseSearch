package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.nlp.ling.Sentence;
import model.TupledSentence;
import model.TupleConverter;
import service.SentenceComparator;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ParaphraseSearcher {

    public static void main(String[] args) {
        ParaphraseSearcher paraphraseSearcher = new ParaphraseSearcher();
        Collection<model.labelled.Sentence> sentences = paraphraseSearcher.loadLabelledSentences("ParaphraseSearcher/data/file9");

        List<TupledSentence> tupledSentences = TupleConverter.fromSentences(sentences);
        List<TupledSentence> sample = tupledSentences.subList(0, 8);

        for (int i=0; i<sample.size(); i+=2) {
            SentenceComparator.compare(sample.get(i), sample.get(i+1), true);
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

