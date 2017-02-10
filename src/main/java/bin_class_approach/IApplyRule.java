package bin_class_approach;

import datamodel.IWord;

/**
 * Analog of ILemmaRule interface. Interface does not need morpheme extraction
 */
public interface IApplyRule {

    boolean isApplicable(String word);

    IWord apply(String word);

    String getMorphProperties();

}
