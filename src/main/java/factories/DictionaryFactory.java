package factories;

import analyzers.DictionaryMorphAnalyzer;
import analyzers.IMorphAnalyzer;
import datamodel.IWord;
import datamodel.Word;

import java.io.*;
import java.util.*;

/**
 * Class forms dictionary from texts, saves it and creates DictionaryMorphAnalyzer instance.
 */
public class DictionaryFactory implements IMorphAnalyzerFactory {

    private List<File> files;

    /**
     * @param dataset Dataset is a text file or a folder containing text files.
     */
    public DictionaryFactory(File dataset) {

        if (dataset == null) {
            final String EXC_MESSAGE = "Data set in Dictionary factory cannot be null.";
            throw new IllegalArgumentException(EXC_MESSAGE);
        }

        files = new ArrayList<>();

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
    }

    public IMorphAnalyzer create() {
        HashSet<IWord> words = new HashSet<>();

        for (File f : files) {
            words.addAll(
                    extractWords(f)
            );
        }

        return new DictionaryMorphAnalyzer(words);
    }


    private Collection<IWord> extractWords(File file) {

        Collection<IWord> result = new ArrayList<>();

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

                // Condition for situations when openTag was found before, but no closeTag yet
                if (insideWordTag) {

                    // If closeTag was founded
                    if (line.matches(any.concat(closeTag).concat(any))) {

                        // Split the line by the </w> separator
                        String[] splitted = line.split(closeTag, 2);

                        wordLine = wordLine.concat(splitted[0]);
                        tryAddWord(result, wordLine);

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

            System.out.println(file.getName());

            bis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    private void tryAddWord(Collection<IWord> words, String line){

        IWord word = parse(line);
        if (word != null){
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
