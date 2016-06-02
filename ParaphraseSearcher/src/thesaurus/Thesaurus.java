package thesaurus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import com.google.inject.Singleton;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

@Singleton
public final class Thesaurus {
	private final WordNetDatabase database;
    private static Thesaurus theInstance;
	
    static {
        try {
            theInstance = new Thesaurus();
        }
        catch (Exception e) {
            theInstance = null;
        }
        
    }
    
    protected Thesaurus() {
		File f=new File("C:\\Program Files (x86)\\WordNet\\2.1\\dict");
	    System.setProperty("wordnet.database.dir", f.toString());
	    database = WordNetDatabase.getFileInstance();
	}
	
    public static Thesaurus getInstance() {
        return Thesaurus.theInstance;
    }
	
	public ArrayList<String> getSynonyms(final String wordForm) {
		
		Synset[] synsets = database.getSynsets(wordForm);
		
        ArrayList<String> synonyms = new ArrayList<String>();
		
        if (synsets.length > 0) {
            HashSet distinctSynonyms = new HashSet();
            for (int i = 0; i < synsets.length; i++){
               String[] wordForms = synsets[i].getWordForms();
                 for (int j = 0; j < wordForms.length; j++)
                 {
                	 synonyms.add(wordForms[j]);
                 }

             distinctSynonyms.addAll(synonyms);
             synonyms.clear();
             synonyms.addAll(distinctSynonyms);
            }
        }
		return synonyms;
		
	}
}
