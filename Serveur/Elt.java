package objets;

public class Elt {
    
    int idElt;
    String nom;
    String couleur;

    public Elt (int id, String n, String c) {
        idElt = id;
        nom = n;
        couleur = c;
    }

    public String getNom() {
        return nom;
    }

    public int getIdElt() {
        return idElt;
    }


    public String getCouleur() {
        return couleur;
    }
}