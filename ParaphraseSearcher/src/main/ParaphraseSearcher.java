package main;

import edu.stanford.nlp.trees.PennTreeReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParaphraseSearcher {
    public ParaphraseSearcher(String filename) throws IOException {
        PennTreeReader reader = new PennTreeReader(new InputStreamReader(new FileInputStream(filename)));
    }
}
