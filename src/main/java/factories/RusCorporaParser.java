package factories;

import datamodel.IWord;
import datamodel.Word;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * RusCorpora dictionary parser
 */
class RusCorporaParser implements IDatasetParser{

    private List<File> files;

    public RusCorporaParser(File dataset) {
    	this(getFileList(dataset));
    }
    
    private static List<File> getFileList(File dataset){
    	if (dataset == null) {
            final String EXC_MESSAGE = "Data set in Dictionary factory cannot be null.";
            throw new IllegalArgumentException(EXC_MESSAGE);
        }

        List<File> files = new ArrayList<>();

        // If dataset is directory, than read all files from this dir
        if (dataset.isDirectory()) {
            File[] filesInDir = dataset.listFiles();

            if (filesInDir == null || filesInDir.length == 0) {
                final String EXC_MESSAGE = "Dataset directory must contain at least one file.";
                throw new IllegalArgumentException(EXC_MESSAGE);
            }

            files = Arrays.asList(filesInDir);
        }
        // else dataset is considered as file
        else {
            files.add(dataset);
        }
        return files;
    }
    
    public RusCorporaParser(List<File> dataset){

        if (dataset == null || dataset.size() == 0) {
            final String EXC_MESSAGE = "Data set in Dictionary cannot be null.";
            throw new IllegalArgumentException(EXC_MESSAGE);
        }

        files = new ArrayList<>();

        files.addAll(
                dataset
                        .stream()
                        .filter(f -> f.getName().endsWith(".xhtml"))
                        .collect(Collectors.toList()
                        )
        );
    }

    
    @Override
    public HashSet<IWord> getDictionary(){

        HashSet<IWord> words = new HashSet<>();

        for (File f : files) {
            Collection<IWord> fromFile = extractWords(f);
            for (IWord word : fromFile){
                if (!words.contains(word)){
                    words.add(word);
                }
            }
        }

        return words;
    }
    
    private static Pattern wordPattern = Pattern.compile("<w><ana lex=\"([^\"]+)\" gr=\"([^\"]+)\"></ana>([^<]+)</w>");

    private Collection<IWord> extractWords(File file) {

        Set<IWord> result = new HashSet<>();

        try {

            BufferedReader bis = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "windows-1251"
                    )
            );

            final String openTag = "<w>";
            final String closeTag = "</w>";
            final String any = ".*";
            String wordLine = "";
            boolean insideWordTag = false;
            String line;

            while ((line = bis.readLine()) != null) {
            	/*TODO use Matcher.
            	Matcher matcher = wordPattern.matcher(line);
            	while(matcher.find()){
            		String lemma = matcher.group(1);
            		String pos = matcher.group(2);
            		String word = matcher.group(3);
            	}*/

                // Condition for situations when openTag was found before, but no closeTag yet
                if (insideWordTag) {

                    // If closeTag was founded
                    if (line.matches(any.concat(closeTag).concat(any))) {

                        // Split the line by the </w> separator
                        String[] splitted = line.split(closeTag, 2);

                        wordLine = wordLine.concat(splitted[0]);
                        result.add(parse(wordLine));
                        //tryAddWord(result, wordLine);

                        line = splitted[1];

                        insideWordTag = false;
                    }
                    else {
                        wordLine = wordLine.concat(line);
                    }
                }

                // while line containing both opening and closing tags (regex = ".*<w>.*</w>.*")
                while (line.matches(any.concat(openTag).concat(any).concat(closeTag).concat(any))) {

                    String[] splitted = line.split(closeTag, 2);

                    wordLine = splitted[0].split(openTag, 2)[1];

                    tryAddWord(result, wordLine);

                    line = splitted[1];

                }

                if (line.matches(any.concat(openTag).concat(any))) {

                    wordLine =  line.split(openTag, 2)[1];
                    insideWordTag = true;
                }


            }

            bis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    
    private void tryAddWord(Collection<IWord> words, String line){

        IWord word = parse(line);
        if (word != null && !words.contains(word)){
            words.add(word);
        }

    }

    private IWord parse(String line) {

        if (!line.matches("^<ana lex=\".*\".*gr=\".*\"></ana>.+")){
            return null;
        }

        // Example of parsed string:
        // <ana lex="результат" gr="S,m,inan=sg,loc"></ana>результ`ате

        String word, lemma, props;

        String[] splitted = line.split("lex=\"|gr=\"");

        lemma = splitted[1].split("\"")[0].toLowerCase();
        props = splitted[2].split("\"")[0].toLowerCase();

        word = line.split("</ana>")[1];
        word = word.replace("`", "").toLowerCase();

        return new Word(word, lemma, props);
    }
}
