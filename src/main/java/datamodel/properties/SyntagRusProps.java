package datamodel.properties;

import datamodel.IWord;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SyntagRusProps
 */
public class SyntagRusProps implements IMorphProperties {

    private PoS pos;
    private final Set<String> properties;

    private static Map<PoS, Set<String>> importantProps;

    static {
        importantProps = new HashMap<>();

        putImpProp(PoS.NOUN, "Gender", "Number", "Case");
        putImpProp(PoS.ADJ, "Gender", "Number", "Case", "Degree", "Variant");
        putImpProp(PoS.VERB, "Mood", "Person", "Tense", "Number");
        putImpProp(PoS.PRON, "Gender", "Number", "Case", "Person");
        putImpProp(PoS.ADV, "Degree");
        putImpProp(PoS.NUM, "Gender", "Number", "Case", "NumForm");
    }

    private static void putImpProp(PoS pos, String... props){
        importantProps.put(pos, Stream.of(props).collect(Collectors.toSet()));
    }

    public SyntagRusProps(String context){
        String[] posAndProps = context.split("\t");

        if (posAndProps.length != 2)
            throw new IllegalArgumentException(
                    "Property '" + context + "' cannot be parsed");

        // Add pos
        try {
            pos = PoS.valueOf(posAndProps[0]);
        }
        catch (IllegalArgumentException e){
            pos = PoS.NONE;
        }

        // Add props
        String[] props = posAndProps[1].split("\\|");

        if (props.length == 0)
            throw new IllegalArgumentException(
                    "Property '" + context + "' cannot be parsed");

        properties = extractImportantProps(this.pos, props);
    }

    private static Set<String> extractImportantProps(PoS pos, String[] props){

        Set<String> result = new HashSet<>();

        for (String s : props) {

            if (importantProps.getOrDefault(pos, new HashSet<>()).contains(s.split("=")[0])){
                    result.add(s);
            }

        }
        return result;
    }


    @Override
    public PoS getPos() {
        return pos;
    }

    @Override
    public Set<String> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyntagRusProps)) return false;
        SyntagRusProps that = (SyntagRusProps) o;
        return pos == that.pos &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, properties);
    }

    @Override
    public String toString() {
        return "[" + getPos().toString() + "]"
                +  Arrays.toString(properties.toArray());
    }
}
