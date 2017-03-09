package datamodel;

import java.util.Set;

/**
 * Interface for morphological properties of the word
 */
public interface IMorphProperties {
    /**
     *
     * @return String representation of properties
     */
    Set<String> get();
    // todo String getPoS(); may be useful soon...
}
