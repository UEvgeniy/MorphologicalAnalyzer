package factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import analyzers.IPropertyPredictor;
import analyzers.TrivialRulePropertyPredictor;
import datamodel.ILemmaRule;
import datamodel.IMorpheme;
import datamodel.IWord;
import datamodel.LemmaRule;
import datamodel.Morpheme;

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

        Set<ILemmaRule> resultRules = new HashSet<>();

        // Now for each IWord new Lemma Rule will be defined and added to collection


        for (IWord word : words){

            String w = word.toString();
            String lemma = word.getLemma();

            short i = SuffixesHelper.getCommonPrefixLength(w, lemma);

            //Build LemmaRule object
            List<IMorpheme> removed = new ArrayList<>();
            removed.add(new Morpheme(w.substring(i, w.length())));

            List<IMorpheme> added = new ArrayList<>();
            added.add(new Morpheme(lemma.substring(i, lemma.length())));

            LemmaRule newLR = new LemmaRule(removed, added, word.getProperties());


            // guarantee that removed and added morphemes will not be empty and rule is unique
            /*if ((i != w.length() && i != lemma.length() || lemma.length() == w.length())) {
                resultRules.add(newLR);
            }*/
            
            resultRules.add(newLR);
        }

        return new TrivialRulePropertyPredictor(resultRules);

    }
}
