package objets;

import inputOutput.EcritLire;

public class Personne {
    int idPersonne;
    int idEmp;
    String nom;

    public Personne (int id, int ie,String n) {
        idPersonne = id;
        idEmp = ie;
        nom = n;
    }

    public void setIdPersonne(int idPersonne) {
        this.idPersonne = idPersonne;
    }

    public void setIdEmp(int idEmp) {
        this.idEmp = idEmp;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getIdPersonne() {
        return idPersonne;
    }

    public int getIdEmp() {
        return idEmp;
    }

    public String getNom() {
        return nom;
    }
}