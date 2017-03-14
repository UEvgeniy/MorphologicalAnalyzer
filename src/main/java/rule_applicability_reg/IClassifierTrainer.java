package rule_applicability_reg;

import java.util.Map;
import java.util.function.Function;

import datamodel.MorphemedWord;

public interface IClassifierTrainer extends
        Function<Map<MorphemedWord, Boolean>, IClassifier> {

}
