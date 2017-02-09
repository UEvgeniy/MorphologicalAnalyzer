package maslov_segalovich_based;

import datamodel.IWord;
import datamodel.Word;

import java.io.Serializable;
import java.util.Objects;

/**
 * Lexeme
 */
class Lexeme implements Comparable<Lexeme>, Serializable{

    private static final long serialVersionUID = -4844700843850205547L;
    private String basis;
    private IWord lexeme;
    private int repeats;

    public Lexeme(String basis){
        this(basis, new Word(basis, basis, ""), 0);
    }

    public Lexeme(String basis, IWord lexeme, int repeats){
        this.basis = Objects.requireNonNull(basis);
        this.lexeme = Objects.requireNonNull(lexeme);
        this.repeats = repeats;
    }

    public String getBasis() {
        return basis;
    }

    public IWord getLexeme() {
        return lexeme;
    }

    public int getRepeats() {
        return repeats;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lexeme)) return false;

        Lexeme lexeme1 = (Lexeme) o;

        if (repeats != lexeme1.repeats) return false;
        if (!basis.equals(lexeme1.basis)) return false;
        return lexeme.equals(lexeme1.lexeme);

    }

    @Override
    public int hashCode() {
        return Objects.hash(basis, lexeme, repeats);
    }



    @Override
    public int compareTo(Lexeme o) {
        return new StringBuilder(this.getBasis()).reverse().toString()
                .compareTo(
                        new StringBuilder(o.getBasis()).reverse().toString()
                );
    }
}
