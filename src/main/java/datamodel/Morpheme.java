package datamodel;

// todo add getType() method
public class Morpheme implements IMorpheme {

    private String morpheme;

    public Morpheme(String morpheme){
        this.morpheme = morpheme;
    }

    @Override
    public String getText() {
        return morpheme;
    }


    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == Morpheme.class && this.morpheme.equals(((Morpheme)obj).morpheme);
    }
}
