package objets;

import java.io.Serializable;

import javax.crypto.interfaces.PBEKey;

public class Table implements Serializable {
    String nom;
    Object[] entete;
    Object[][] data;
    Table [] usedTables;

    public Table(){}

    public Table(Object[]e, Object[][] d) {
        setEntete(e);
        setData(d);
    }

    public Table(String n, Object[]e, Object[][] d) {
        setNom(n);
        setEntete(e);
        setData(d);
    }

    public int getIdUsedTable(String nomTable) throws Exception {
        if(usedTables == null) throw new Exception("Cette table ne contient aucune usedTable");
        for (int i = 0; i < usedTables.length; i++) {
            if(usedTables[i].getNom().equals(nomTable) == true) {
                return i;
            }
        }
        return -1;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public Object[] getEntete() {
        return entete;
    }

    public Object[][] getData() {
        return data;
    }
    
    public Table[] getUsedTables() {
        return usedTables;
    }

    public void setUsedTables(Table[] usedTables) {
        this.usedTables = usedTables;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public void setEntete(Object[] entete) {
        this.entete = entete;
    }
}