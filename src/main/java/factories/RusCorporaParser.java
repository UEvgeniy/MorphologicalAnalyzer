package factories;

import datamodel.IWord;
import datamodel.Word;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * RusCorpora dictionary parser
 */
public class RusCorporaParser implements IDatasetParser{

    private List<File> files;
    private static final Pattern wordPattern =
            Pattern.compile("<w><ana lex=\"([^\"]+)\" gr=\"([^\"]+)\"></ana>([^<]+)</w>");


    public RusCorporaParser(List<File> files) {
    	this.files = Objects.requireNonNull(files, "Dataset cannot be null.");
    }

    
    @Override
    public HashSet<IWord> getDictionary(){

        HashSet<IWord> words = new HashSet<>();

        for (File f : files) {
            words.addAll(
                    extractWords(f)
            );
        }

        return words;
    }

    /**
     * Form a collection of tagged words for one file
     * @param file File (not dictionary)
     * @return Collection of tagged words
     */
    private Collection<IWord> extractWords(File file) {

        Set<IWord> result = new HashSet<>();

        try {

            BufferedReader bis = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "windows-1251"
                    )
            );

            String line;

            while ((line = bis.readLine()) != null) {

            	Matcher matcher = wordPattern.matcher(line);
            	if(matcher.matches()){
            		String lemma = matcher.group(1).toLowerCase();
            		String properties = matcher.group(2).toLowerCase();
            		String word = matcher.group(3).replace("`", "").toLowerCase();;

                    result.add(
                            new Word(word, lemma, properties)
                    );
            	}
            }

            bis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
