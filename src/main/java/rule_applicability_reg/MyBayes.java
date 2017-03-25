package rule_applicability_reg;

import net.sf.javaml.classification.bayes.NaiveBayesClassifier;

import java.io.Serializable;

/**
 * Make serializable class from JAR
 */
public class MyBayes extends NaiveBayesClassifier implements Serializable {
    private static final long serialVersionUID = 4857944217453450726L;

    public MyBayes(boolean lap, boolean log, boolean sparse) {
        super(lap, log, sparse);
    }
}
