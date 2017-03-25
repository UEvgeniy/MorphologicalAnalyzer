package datamodel.properties;

import java.io.Serializable;
import java.util.*;

/**
 * Morph properties in usual representation
 */

// todo change implement methods
public class RusCorporaProps implements IMorphProperties, Serializable{

    private static final long serialVersionUID = -2218704355685995948L;
    private Set<String> properties;

    public RusCorporaProps(String properties){

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
    public RusCorporaProps(RusCorporaProps properties) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RusCorporaProps that = (RusCorporaProps) o;
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

    @Override
    public PoS getPos() {
        return null;
    }

    @Override
    public Set<String> getProperties() {
        return null;
    }
}
