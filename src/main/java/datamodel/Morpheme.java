package datamodel;

import java.io.Serializable;

// todo add getType() method
public class Morpheme implements IMorpheme, Serializable {

    private static final long serialVersionUID = 5151888290418566863L;
    private String morpheme;

    public Morpheme(String morpheme){
        this.morpheme = morpheme;
    }

    @Override
    public String getText() {
        return morpheme;
    }

    @Override
    public int hashCode() {
        return morpheme.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Morpheme)) return false;

        Morpheme morpheme1 = (Morpheme) o;

        return morpheme != null ? morpheme.equals(morpheme1.morpheme) : morpheme1.morpheme == null;

    }
}
