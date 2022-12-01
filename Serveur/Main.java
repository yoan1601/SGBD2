package main;

import java.util.Vector;

import inc.Fonction;
import inc.FonctionV2;
import inputOutput.EcritLire;
import objets.*;

public class Main {
    public static void main(String[] args) throws Exception {


        Fonction f = new Fonction();
        EcritLire eclire = new EcritLire(); 
        // String requete = "alaivo * itovizana ny elt1 sy elt2"; //intersection
        // String requete = "alaivo nomEmp,salaire ao employe"; //projection
        // String requete = "alaivo * produit ny elt2 sy elt1"; //produit cartesien
        // String requete = "alaivo * ampifandraiso ny elt2 sy elt1"; //natural join
        // String requete = "alaivo * atambatra ny elt1 sy elt2"; //union
        // String requete = "   alaivo * ao     elt1 izay idElt >  1  "; //selection
        // String requete = "alaivo * analana ny elt1 @ elt2"; //difference
        // String requeteCours = "alaivo idCours,cours ao cours2";
        // String requeteInsc = "alaivo nom,prenom,cours ao inscription";
        // String requete = "alaivo * division ny inscription @ cours2"; //division

        ///////multi-conditions

        // String requete = "alaivo * atambatra ny elt1 sy elt2 izay idElt >= 5 ary couleur like c ary idElt < 9"; //union
        // String requete = "alaivo * itovizana ny elt1 sy elt2   izay couleur = noir"; //intersection
        // String requete = "alaivo * ampifandraiso ny elt2 sy elt1 izay elt2->couleur like c ary elt1->couleur like noir ary elt2->nom like 6"; //natural join
        // String requete = "alaivo * produit ny elt1 sy elt2 izay elt2->idElt >= 7 ary elt1->couleur like blanc"; //produit cartesien
        // String requete = "alaivo * produit ny elt1 sy elt2 izay couleur like blanc"; //produit cartesien
        // String requete = "alaivo * analana ny elt1 @ elt2 izay couleur like bleu"; //difference
        // String requete = "alaivo * division ny inscription @ cours2 izay nom like T"; //division


        /////Create table
        // String requete = "create table newTable ( colonne1 type1 , colonne2 type2 , colonne3 type3 )";
        // String requete = "create table rebeka ( fahazazana int , fahamanjana string , fahaizana double )";

        ////Insert 
        // String requete = "insert ao newTable values ( val1, val2, val3 )";

        // update : reparer les subStrings de update et subString de condition
        // req : update nomTable ataovy [nomCol = value] izay [nomColCondition = valueCondition]
        String requete = "update personne ataovy idEmp = good izay idPersonne = 3";
        
        // String [][] wheresUpdate = Fonction.getwheresUpdate(requete, set, where, and);
        // String [][] setsUpdate = Fonction.getsetsUpdate(requete, set, where, and);
        // Object[][] data = eclire.lire("personne");
        // Object[] entete = eclire.getEnteteTableInFile("personne");
        // Table table = new Table(entete, data);
        // table.setNom("personne");

        // System.out.println("wheres update");
        // for (String[] wheres : wheresUpdate) {
        //     System.out.println(wheres[0] + " " + wheres[1]+ " " + wheres[2]);
        // }

        // System.out.println("sets update");
        // for (String[] sets : setsUpdate) {
        //     System.out.println(sets[0] + " " + sets[1]+ " " + sets[2]);
        // }

        //insert
        // String requete = "insert ao nomTable ireto ( 6, Elt69, c69 ) ";


        //delete 
        // requete = "delete ao nomTable izay nom = jean";

        try {
            Table relation = f.traiterRequete(requete);
            f.display(relation);
            // f.displayAll(relation);
            // relation = f.traiterRequete(requeteInsc);
            // f.displayAll(relation);
            // relation = f.traiterRequete(requete);
            //f.displayAll(relation);
            //f.setDatabaseToFile(); Je t'aime pas
            // int [] listeIndice = f.getIndiceColUpdate(table, setsUpdate);
            // for (int i : listeIndice) {
            //     System.out.println(i);
            // }
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }

        // Personne p1 = new Personne(1, 1, "p1");
        // Personne p2 = new Personne(2, 2, "p2");
        // Personne p3 = new Personne(3, 3, "p3");
        // Personne[]lp = {p1, p2, p3};

        EcritLire ec = new EcritLire();
        // // ec.create(lp, "personne");
        // Personne p4 = new Personne(4, 4, "p4");
        // // ec.insert(p4, "personne");

        Object[] entete1 = ec.getEnteteTableInFile("inscription");
        Object[][]data1 = ec.lire("inscription");
        Table t1 = new Table(entete1, data1);

        Object[] entete2 = ec.getEnteteTableInFile("cours");
        Object[][]data2 = ec.lire("cours");
        Table t2 = new Table(entete2, data2);

        // f.displayAll(t1);
        // f.displayAll(t2);

        FonctionV2 f2 = new FonctionV2();
        // int[] indCol = f2.getIndCommonCol(t1, t2);
        // for (int i : indCol) {
        //     System.out.println(i);
        // }
        // Table t = f2.division(t1, t2);
        System.out.println("hello git");

    }

}