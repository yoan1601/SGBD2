package objets;

public class Dept {
    int numDept;
    String nomDept;

    public Dept (int nd, String nomd) {
        numDept = nd;
        nomDept = nomd;
    }

    public String getNomDept() {
        return nomDept;
    }

    public int getNumDept() {
        return numDept;
    }
}