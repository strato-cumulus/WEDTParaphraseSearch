package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class ParaphraseSearcher {

	private Collection<model.Sentence> sentences;

	public ParaphraseSearcher() {
		this.sentences = new ArrayList<model.Sentence>();
	}
	
    public static void main(String[] args) {
        ParaphraseSearcher paraphraseSearcher = new ParaphraseSearcher();
        
		try {
			paraphraseSearcher.readSequencesFromFile("ParaphraseSearcher/data/msr_paraphrase_data_test.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(model.Sentence sen: paraphraseSearcher.sentences) {
			System.out.println(sen.getSentence());
		}
		
        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        if (args.length > 0) {
            parserModel = args[0];
        }
        LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
        // This option shows parsing a list of correctly tokenized words
        String[] sent = {"This", "is", "an", "easy", "sentence", "."};
        List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
        Tree parse = lp.apply(rawWords);
        parse.pennPrint();
        System.out.println();

        // This option shows loading and using an explicit tokenizer
        String sent2 = "This is another sentence.";
        TokenizerFactory<CoreLabel> tokenizerFactory =
                PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tok =
                tokenizerFactory.getTokenizer(new StringReader(sent2));
        List<CoreLabel> rawWords2 = tok.tokenize();
        parse = lp.apply(rawWords2);

        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
        System.out.println(tdl);
        System.out.println();

        // You can also use a TreePrint object to print trees and dependencies
        TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
        tp.printTree(parse);
        //Collection<Tuple> simpleTuples = Tuple.createSimpleTuplesFromSentence(parse);
    }
    
    public void readSequencesFromFile(String FilePath) throws IOException {
        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        LexicalizedParser lexicalizedParser = LexicalizedParser.loadModel(parserModel);
    	Reader reader =
    		      new InputStreamReader(
    		         new FileInputStream(FilePath),"UTF-8");
    	BufferedReader buf = new BufferedReader(reader);
    	String line = null;
    	String[] splittedLine = null;
    	while(true){
            line = buf.readLine();
            if(line == null){  
                break; 
            }
            else {
                splittedLine = line.split("\t");
                this.sentences.add(new model.Sentence(
                	splittedLine[1], 
                	Integer.parseInt(splittedLine[0]),
                	lexicalizedParser
                	));
            }
    	}
    	buf.close();
    }
}
