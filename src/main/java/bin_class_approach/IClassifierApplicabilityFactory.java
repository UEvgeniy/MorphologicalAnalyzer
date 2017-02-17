package bin_class_approach;

/**
 * Factory that forms instance of IClassifierApplicability
 */
interface IClassifierApplicabilityFactory {

    /**
     * Method constructs binary classifier
     * @return Returns the insta
     */
    IClassifierApplicability create();
}
