package datamodel.properties;

import java.util.Set;

/**
 * Interface for morphological properties of the word
 */
public interface IMorphProperties {

    PoS getPos();

    Set<String> getProperties();


}
