package factories;

import analyzers.IPropertyPredictor;
import analyzers.TrivialRulePropertyPredictor;
import datamodel.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Factory for classes which predict PoS + properties for morphemed word
 */
class TrivialLemmaRulePropertyPredictorTrainer implements IPropertyPredictorFactory {

    private Collection<IWord> words;

    TrivialLemmaRulePropertyPredictorTrainer(Collection<IWord> words){
        this.words = words;
    }


    @Override
    public IPropertyPredictor create() {

        Collection<ILemmaRule> resultRules = new ArrayList<>();

        // Now for each IWord new Lemma Rule will be defined and added to collection


        for (IWord word : words){

            String w = word.toString();
            String lemma = word.getLemma();

            short i = 0;

            while (i < Math.min(w.length(), lemma.length())){
                if (w.charAt(i) == lemma.charAt(i))
                    i++;
                else
                    break;
            }

            //Build LemmaRule object
            ArrayList<IMorpheme> removed = new ArrayList<>();
            removed.add(new Morpheme(w.substring(i, w.length())));

            ArrayList<IMorpheme> added = new ArrayList<>();
            added.add(new Morpheme(lemma.substring(i, lemma.length())));

            LemmaRule newLR = new LemmaRule(removed, added, word.getProperties());


            // guarantee that removed and added morphemes will not be empty and rule is unique
            if ((i != w.length() && i != lemma.length() || lemma.length() == w.length())
                    && !resultRules.contains(newLR)) {

                resultRules.add(newLR);
            }
        }

        return new TrivialRulePropertyPredictor(resultRules);

    }
}
