/**
 * 
 */
package model;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

/**
 * This class represents a single sentence.
 */
public final class Sentence {
	private String sentence;
	private int sentenceID;
	private Tree tuples;
	Collection<Tuple> simpleTuples;
	
	public Sentence(String sentence, int sentenceID, LexicalizedParser lexicalizedParser) {
		this.sentence = sentence;
		this.sentenceID = sentenceID;
		this.tuples = getSentenceTuples(tokenizeSentence(sentence),lexicalizedParser);
		this.simpleTuples = Tuple.createSimpleTuplesFromSentence(this.tuples);
	}
	
	public String getSentence() {
		return sentence;
	}
	
	public void setSentence(String sentence, LexicalizedParser lexicalizedParser) {
		this.sentence = sentence;
		this.tuples = getSentenceTuples(tokenizeSentence(sentence),lexicalizedParser);
		this.simpleTuples = Tuple.createSimpleTuplesFromSentence(this.tuples);
	}
	
	public int getSentenceID() {
		return sentenceID;
	}
	
	public void setSentenceID(int sentenceID) {
		this.sentenceID = sentenceID;
	}
	
	public Tree getTuples() {
		return tuples;
	}
	
	@SuppressWarnings("unused")
	private void setTuples(Tree tuples) {
		this.tuples = tuples;
	}

	public Collection<Tuple> getSimpleTuples() {
		return simpleTuples;
	}

	@SuppressWarnings("unused")
	private void setSimpleTuples(Collection<Tuple> simpleTuples) {
		this.simpleTuples = simpleTuples;
	}
	
	public List<CoreLabel> tokenizeSentence(String sentence) {
        TokenizerFactory<CoreLabel> tokenizerFactory =
                PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tokenizer =
                tokenizerFactory.getTokenizer(new StringReader(sentence));
        List<CoreLabel> rawWords2 = tokenizer.tokenize();
		return rawWords2;
	}
	
	public Tree getSentenceTuples(List<CoreLabel> tokenizedSentence, LexicalizedParser lexicalizedParser) {
        Tree tuples = lexicalizedParser.apply(tokenizedSentence);
		return tuples;
	}
}
