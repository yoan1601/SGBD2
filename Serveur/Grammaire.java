package automate;

public class Grammaire {
    String syntaxe;
    Grammaire next;
    Grammaire previous;

    public Grammaire (String n) {
        setSyntaxe(n);
    }

    public String getSyntaxe() {
        return syntaxe;
    }

    public Grammaire getPrevious() {
        return previous;
    }
    
    public Grammaire getNext() {
        return next;
    }

    public void setSyntaxe(String syntaxe) {
        this.syntaxe = syntaxe;
    }

    public void setNext(Grammaire next) {
        this.next = next;
    }

    public void setPrevious(Grammaire previous) {
        this.previous = previous;
    }
}