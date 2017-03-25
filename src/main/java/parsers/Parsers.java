package parsers;

import datamodel.IWord;
import datamodel.Word;
import datamodel.properties.RusCorporaProps;
import datamodel.properties.SyntagRusProps;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Collection of different parsers
 */
public class Parsers {

    /**
     * Represents a function that accepts 3 Strings and produces a IWord.
     */
    @FunctionalInterface
    interface IWordConverter {
        IWord convert(String word, String lemma, String properties);
    }

    // Suppresses default constructor, ensuring non-instantiability.
    private Parsers(){
    }


    /**
     * Parser for RusCorpora tagged texts
     * @param file List of files, where for parse
     * @return Collection of extracted tagged words
     */
    public static Collection<IWord> RusCorpora(File file){
        String pattern =
                "<w><ana lex=\"([^\"]+)\" gr=\"([^\"]+)\"></ana>([^<]+)</w>.*";

        IWordConverter converter =
                (form, lemma, props) -> new Word(form, lemma, new RusCorporaProps(props));

        return commonParser(file, ".xhtml", pattern,
                "windows-1251", converter );
    }


    /**
     * Parser for SyntagRus tagged texts
     * @param file List of files, where for parse
     * @return Collection of extracted tagged words
     */
    public static Collection<IWord> SyntagRus(File file){

        String pattern = "\\d+\\s(?<word>[\\S]+)\\s(?<lemma>[\\S]+)\\s(?<props>.+)";

        IWordConverter converter =
                (form, lemma, props) -> new Word(form, lemma, new SyntagRusProps(props));

        return commonParser(file, ".ud", pattern,
                "utf-8", converter);
    }




    private static Collection<IWord> commonParser(File file,
                                                  String extension,
                                                  String pattern,
                                                  String charsetName,
                                                  IWordConverter converter) {

        if (!hasExtension(file, extension)){
            return new HashSet<>();
        }


        final Pattern wordPattern =
                Pattern.compile(pattern);


        Set<IWord> result = new HashSet<>();

        try {

            BufferedReader bis = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), charsetName
                    )
            );

            String line;

            while ((line = bis.readLine()) != null) {

                Matcher matcher = wordPattern.matcher(line);
                if(matcher.matches()){
                    String word = matcher.group("word").replace("`", "").toLowerCase();;
                    String lemma = matcher.group("lemma").toLowerCase();
                    String properties = matcher.group("props");

                    result.add(
                            converter.convert(word, lemma, properties)
                    );
                }
            }

            bis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    private static boolean hasExtension(File file, String extension){
        String name = file.getName();
        int dot = name.lastIndexOf('.');

        return !file.isDirectory() &&
                dot > 0 &&
                extension.equals(name.substring(dot));

    }
}
