package datamodel;

/**
 * An interface for classes containing the initial form of the word and its own PoS and properties
 */
public interface IWord {

    /**
     * @return String representation of the word
     */
    String getWord();

    /**
     * @return The initial form of the word
     */
    String getLemma();

    /**
     * @return String representation of properties
     */
    String getProperties();
}
