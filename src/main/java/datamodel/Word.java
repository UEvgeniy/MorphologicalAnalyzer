package datamodel;


public class Word implements IWord {

    private String word, lemma, propeties;

    public Word(String word, String lemma, String properties){
        this.word = word;
        this.lemma = lemma;
        this.propeties = properties;
    }


    @Override
    public String getLemma() {
        return lemma;
    }

    @Override
    public String getProperties() {
        return propeties;
    }

    @Override
    public String toString() {
        return word;
    }

    @Override
    public boolean equals(Object obj) {

        return (obj.getClass() == Word.class)
                && (obj.toString().equals(this.toString()))
                && ((Word) obj).getLemma().equals(this.getLemma())
                && ((Word) obj).getProperties().equals(this.getProperties());
    }
}
