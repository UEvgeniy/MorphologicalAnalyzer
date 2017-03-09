package datamodel;

import java.util.*;

/**
 * Morph properties in usual representation
 */
public class MorphProperties implements IMorphProperties{

    private Set<String> properties;

    public MorphProperties(Set<String> properties){
        this.properties = properties;
    }

    MorphProperties(String properties){

        String[] propsArray = properties.split("=");
        String usefulProps = propsArray.length > 1 ? propsArray[1] : "";

        this.properties = new HashSet<>(Arrays.asList(usefulProps.split(",")));

        //this.properties = new HashSet<>(Arrays.asList(properties.split(",")[0]));

        /*String[] props = properties.split(",|=");
        Set<String>  tags = new HashSet<>(Arrays.asList(props));
        // todo set of important tags
        Set<PoS> propsSet = PoS.get(tags);

        init(propsSet);
        */



    }

    /*
    public MorphProperties(MorphProperties properties) {
        this.properties = properties.get();
    }
    private void init(Set<PoS> props){
        StringBuilder builder = new StringBuilder();

        for (PoS pos: props) {
            builder.append(pos.getValue()).append(" ");
        }

        this.properties = builder.toString();
    }
    */

    @Override
    public Set<String> get() {
        return properties;
    }

    /*
    enum PoS {

        S("Сущ"), A("Прил"), V("Глаг");

        private String value;

        PoS(String value){
            this.value = value;
        }

        String getValue(){
            return value;
        }

        static Set<PoS> get(Collection<String> properties){
            Set<PoS> result = new HashSet<>();

            for (String property : properties) {
                switch (property) {
                    case "s":
                        result.add(S);
                        break;
                    case "a":
                        result.add(A);
                        break;
                    case "v":
                        result.add(V);
                        break;
                }
            }
            return result;
        }

    }
    */


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MorphProperties that = (MorphProperties) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public String toString() {
        return Arrays.toString(properties.toArray());
    }
}
