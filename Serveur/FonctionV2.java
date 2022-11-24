package inc;

import java.util.Vector;
import java.util.Collections;

import automate.Grammaire;
import inputOutput.EcritLire;
import objets.*;

import java.lang.reflect.*;
import java.rmi.server.ObjID;

public class FonctionV2 extends Fonction {

    public Table selection(Table table, String colonne, String operateur, String valeur, int idUsedTable)
            throws Exception {
        EcritLire e = new EcritLire();
        Object[] colonnes = table.getEntete();
        Object[][] data = table.getData();
        int[] indCol = getIndColonneTab(colonne, table);
        Object[] typesCol = e.getTypesTableInFile(table.getNom());
        
        String typeColonne = typesCol[indCol[0]].toString();
        System.out.println("selection par " + table.getNom());

        Vector dataVect = new Vector();

        int verifTrueValue = 0;

        for (int i = 0; i < data.length; i++) {

            // for (int j = 0; j < indCol.length; j++) {
                
                // System.out.println("verifTrue "+verifTrueValue);
                // String dataStr = (String) data[i][indCol[j]];
                // System.out.println("data["+i+"][indCol["+j+"]] : "+dataStr);
                String dataStr = (String) data[i][indCol[idUsedTable]];

                if (operateur.equals("=")) {
                    if (typeColonne.equalsIgnoreCase("string") == true
                            || typeColonne.equalsIgnoreCase("date") == true) {
                        if (dataStr.equals(valeur) == true) {
                            // if (verifTrueValue == idUsedTable) {
                                dataVect.add(data[i]);
                            // }

                            // verifTrueValue++;
                        }
                    } else { // int / double / float
                        if (Double.parseDouble(dataStr) == Double.parseDouble(valeur)) {
                            // if (verifTrueValue == idUsedTable) {
                                dataVect.add(data[i]);
                            // }

                            // verifTrueValue++;
                        }
                    }
                } else if (operateur.equals(">")) {
                    if (Double.parseDouble(dataStr) > Double.parseDouble(valeur)) {
                        // if (verifTrueValue == idUsedTable) {
                            dataVect.add(data[i]);
                        // }

                        // verifTrueValue++;
                    }
                } else if (operateur.equals(">=")) {
                    if (Double.parseDouble(dataStr) >= Double.parseDouble(valeur)) {
                        // if (verifTrueValue == idUsedTable) {
                            dataVect.add(data[i]);
                        // }

                        // verifTrueValue++;
                    }
                } else if (operateur.equals("<")) {
                    if (Double.parseDouble(dataStr) < Double.parseDouble(valeur)) {
                        // if (verifTrueValue == idUsedTable) {
                            dataVect.add(data[i]);
                        // }

                        // verifTrueValue++;
                    }
                } else if (operateur.equals("<=")) {
                    if (Double.parseDouble(dataStr) <= Double.parseDouble(valeur)) {
                        // if (verifTrueValue == idUsedTable) {
                            dataVect.add(data[i]);
                        // }

                        // verifTrueValue++;
                    }
                } else if ((operateur.equalsIgnoreCase("like")) == true) {
                    // System.out.println("verif if");
                    if (dataStr.contains(valeur) == true) {
                        // System.out.println("it contains "+verifTrueValue);
                        // if (verifTrueValue == idUsedTable) {
                            dataVect.add(data[i]);
                            // System.out.println("data true"+dataStr);
                        // }
                        // System.out.println("true "+verifTrueValue);
                        // verifTrueValue++;
                    }
                }
            // }

            // else throw new Exception("operateur invalide : "+operateur);
            verifTrueValue = 0;
        }

        // System.out.println("tapitra");

        Object[][] dataSelect = vectorToObj2Dim(dataVect);
        // System.out.println("conversion vector ended");
        Table select = new Table(colonnes, dataSelect);
        select.setUsedTables(table.getUsedTables());
        // System.out.println("retour select");
        return select;
    }

    public boolean isInIntTab(int n, int[] t) throws Exception {
        if (t.length == 0)
            throw new Exception("int [] length 0 ");

        for (int i : t) {
            // System.out.println("j = "+n+" int[i] = "+i);
            if (n == i)
                return true;
        }

        return false;
    }

    public String[] vectToStringTab(Vector v) throws Exception {
        if (v.size() <= 0)
            throw new Exception("vector empty");

        String[] r = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            r[i] = (String) v.get(i);
        }

        return r;
    }

    public String constrSelection(String[] colonnes) throws Exception {
        int k = 0;
        String c = "";
        for (int i = 0; i < colonnes.length - 1; i++) { // le farany tsy misy virgule
            c = c + colonnes[i] + ",";
            k++;
        }

        c = c + colonnes[k];
        return c;
    }

    public int[] getIndNonCommonCol(Table t1, Table t2) throws Exception {
        int[] indColCom = getIndCommonCol(t1, t2);
        Vector indVect = new Vector();

        Object[] entete1 = t1.getEntete();
        for (int j = 0; j < entete1.length; j++) {
            if (isInIntTab(j, indColCom) == false) {
                indVect.add(j);
            }
        }

        if (indVect.size() == 0)
            throw new Exception("Pas de colonne non en commun");

        int[] indCol = new int[indVect.size()];
        for (int i = 0; i < indCol.length; i++) {
            indCol[i] = (int) indVect.get(i);
            // System.out.println(indCol[i]);
        }

        return indCol;
    }

    public int[] getIndCommonCol(Table t1, Table t2) throws Exception {
        Vector indVect = new Vector();
        Object[] entete1 = t1.getEntete();
        Object[] entete2 = t2.getEntete();
        for (int i = 0; i < entete1.length; i++) {
            for (int j = 0; j < entete2.length; j++) {
                if (entete1[i].toString().equals(entete2[j].toString()) == true) {
                    indVect.add(i);
                }
            }
        }

        if (indVect.size() == 0)
            throw new Exception("Pas de colonne en commun");

        int[] indCol = new int[indVect.size()];
        for (int i = 0; i < indCol.length; i++) {
            indCol[i] = (int) indVect.get(i);
        }

        return indCol;
    }

    public Table division(Table t1, Table t2) throws Exception {
        System.out.println("division");

        int[] indColCommune = getIndCommonCol(t1, t2); // construction selection des Colonnes communes
        Vector colComVect = new Vector<String>();
        Object[] entete1 = t1.getEntete();
        int ind = 0;
        for (int i = 0; i < entete1.length; i++) {
            if (i == indColCommune[ind]) {
                colComVect.add(entete1[i].toString());
                ind++;
            }
        }
        String[] colComTab = vectToStringTab(colComVect);
        String selectionColCom = constrSelection(colComTab);
        System.out.println("Colonnes communes : " + selectionColCom);

        Table R3 = projection(t1.getEntete(), t1.getData(), colComTab);

        int[] indColNonCommune = getIndNonCommonCol(t1, t2); // construction selection des Colonnes non communes (il y a
                                                             // la solution)

        Vector colNonComVect = new Vector<String>();
        // int ind2 = 0;
        for (int i = 0; i < entete1.length; i++) {
            if (isInIntTab(i, indColNonCommune) == true) {
                colNonComVect.add(entete1[i].toString());
                // ind2 ++ ;
            }
        }
        String[] colNonComTab = vectToStringTab(colNonComVect);
        String selectionColNonCom = constrSelection(colNonComTab);
        System.out.println("Colonnes non communes : " + selectionColNonCom);

        R3.setNom(t2.getNom());
        System.out.println("R3 obtenu : Projection de t1(" + selectionColCom + ")");
        // displayAll(R3);

        Table R4 = projection(t1.getEntete(), t1.getData(), colNonComTab);
        R4.setNom(t1.getNom());

        System.out.println("R4 obtenu :  Projection de t1(" + selectionColNonCom + ")");
        // displayAll(R4);

        Table R5 = produitCartesien(R4, R3);
        R5.setNom(t1.getNom());

        System.out.println("R5 obtenu : R4 X R3");
        // displayAll(R5);

        Table R6 = difference(R5, t1);
        R6.setNom(t1.getNom());

        System.out.println("R6 obtenu : R5 diff t1");
        // displayAll(R6);

        Table R7 = projection(R6.getEntete(), R6.getData(), colNonComTab);
        R7.setNom(t1.getNom());

        System.out.println("R7 obtenu : projection de R6(" + selectionColNonCom + ")");
        // displayAll(R7);

        System.out.println("R8 obtenu : R4 diff R7");
        Table R8 = difference(R4, R7);
        R8.setNom(t1.getNom());

        R8.setData(enlevDoublure(R8));
        Table[] u = { t1, t2 };
        R8.setUsedTables(u);
        return R8;
    }

    public boolean isInTable(Object[] ligne, Table t) throws Exception {
        Object[][] data = t.getData();
        for (Object[] l : data) {
            if (isDoublureString(ligne, l) == true) {
                return true;
            }
        }
        return false;
    }

    public Table difference(Table t1, Table t2) throws Exception {
        System.out.println("Difference");
        Table inter = intersection(t1, t2);
        // System.out.println("intersection");
        // displayAll(inter);
        Object[][] dataInter = inter.getData();
        Object[][] data1 = t1.getData();
        Vector dataDiffVect = new Vector();
        for (int i = 0; i < data1.length; i++) {
            if (isInTable(data1[i], inter) == false) {
                dataDiffVect.add(data1[i]);
            }
        }

        Object[][] dataDiff = null;
        Table difference = null;
        Table[] u = { t1, t2 };
        if (dataDiffVect.size() > 0) {
            dataDiff = vectorToObj2Dim(dataDiffVect);
            difference = new Table(t1.getEntete(), dataDiff);
            difference.setUsedTables(u);
        }

        else {
            difference = new Table();
            Object[] entete = { "Avis important" };
            Object[][] data = new Object[1][1];
            data[0][0] = "Pas de resultat";
            difference.setEntete(entete);
            difference.setData(data);
            difference.setUsedTables(u);
        }

        return difference;
    }

    public Table selection(Table table, String colonne, String operateur, String valeur) throws Exception {
        EcritLire e = new EcritLire();
        Object[] colonnes = table.getEntete();
        Object[][] data = table.getData();
        int indCol = getIndColonne(colonne, table);
        Object[] typesCol = e.getTypesTableInFile(table.getNom());
        System.out.println("selection par " + table.getNom());
        String typeColonne = typesCol[indCol].toString();

        Vector dataVect = new Vector();

        for (int i = 0; i < data.length; i++) {
            // System.out.println("pendant selection");
            String dataStr = (String) data[i][indCol];
            if (operateur.equals("=")) {
                if (typeColonne.equalsIgnoreCase("string") == true || typeColonne.equalsIgnoreCase("date") == true) {
                    if (dataStr.equals(valeur) == true) {
                        dataVect.add(data[i]);
                    }
                } else { // int / double / float
                    if (Double.parseDouble(dataStr) == Double.parseDouble(valeur)) {
                        dataVect.add(data[i]);
                    }
                }
            } else if (operateur.equals(">")) {
                if (Double.parseDouble(dataStr) > Double.parseDouble(valeur)) {
                    dataVect.add(data[i]);
                }
            } else if (operateur.equals(">=")) {
                if (Double.parseDouble(dataStr) >= Double.parseDouble(valeur)) {
                    dataVect.add(data[i]);
                }
            } else if (operateur.equals("<")) {
                if (Double.parseDouble(dataStr) < Double.parseDouble(valeur)) {
                    dataVect.add(data[i]);
                }
            } else if (operateur.equals("<=")) {
                if (Double.parseDouble(dataStr) <= Double.parseDouble(valeur)) {
                    dataVect.add(data[i]);
                }
            } else if ((operateur.equalsIgnoreCase("like")) == true) {
                if (data[i][indCol].toString().contains(valeur) == true) {
                    dataVect.add(data[i]);
                }
            }
            // else throw new Exception("operateur invalide : "+operateur);
        }

        Object[][] dataSelect = vectorToObj2Dim(dataVect);
        Table select = new Table(colonnes, dataSelect);
        select.setUsedTables(table.getUsedTables());
        return select;
    }

    public Table naturalJoin(Table t1, Table t2) throws Exception {

        System.out.println("Natural join");

        // 1-Produit cartesien
        // 2-Suppression des lignes tsy mitovy eo am colonne communne
        // 3-Suppression duplication colonne commune

        // 1-Produit cartesien
        Table produitCartesien = produitCartesien(t1, t2);

        // 2-Suppression des lignes tsy mitovy eo am colonne communne
        /// 2-a-Avoir indice colonne commune dans table 1 et dans table 2
        int[] indicesColCommune = new int[2]; // [0] : indice col commune dans t1 et [1] : indice col commune dans t2
        Object[] indColComObj = new Object[2];

        for (int i = 0; i < t1.getEntete().length; i++) {
            String nomCol1 = t1.getEntete()[i].toString();
            for (int j = 0; j < t2.getEntete().length; j++) {
                String nomCol2 = t2.getEntete()[j].toString();
                if (nomCol1.equalsIgnoreCase(nomCol2) == true) {
                    System.out.println("Colonne commune : " + nomCol1);
                    indColComObj[0] = i;
                    indColComObj[1] = j; // pour levée exception si == null --> exception
                    indicesColCommune[0] = (int) indColComObj[0];
                    indicesColCommune[1] = (int) indColComObj[1];
                }
            }
            if (indColComObj[0] != null)
                break; // 1ere colonne commune au cas où plusieurs colonnes communes
        }

        if (indColComObj[0] == null)
            throw new Exception("a commune trouvée");

        /// 2-b-Suppression des lignes tsy mitovy eo am colonne communne
        Vector lignesVect = new Vector();
        for (int i = 0; i < produitCartesien.getData().length; i++) {
            if (produitCartesien.getData()[i][indicesColCommune[0]].toString()
                    .equals(produitCartesien.getData()[i][indicesColCommune[1]].toString())) {
                lignesVect.add(produitCartesien.getData()[i]);
            }
        }

        Object[][] dataJoinedWithDuplicata = vectorToObj2Dim(lignesVect);

        // 3-Suppression duplication colonne commune
        /// 3-a-Suppression duplication colonne commune entete
        Object[] enteteJoined = new Object[produitCartesien.getEntete().length - 1]; // 1 duplication de moins
        int ind = 0;
        for (int i = 0; i < produitCartesien.getEntete().length; i++) {
            if (i != indicesColCommune[1]) {
                enteteJoined[ind] = produitCartesien.getEntete()[i];
                ind++;
            }
        }

        /// 3-b-Suppression duplication colonne commune data
        Object[][] dataJoined = new Object[dataJoinedWithDuplicata.length][dataJoinedWithDuplicata[0].length - 1]; // 1
                                                                                                                   // duplication
                                                                                                                   // de
                                                                                                                   // moins
        int indData = 0;
        for (int i = 0; i < dataJoinedWithDuplicata.length; i++) {
            for (int j = 0; j < dataJoinedWithDuplicata[0].length; j++) {
                if (j != indicesColCommune[1]) {
                    dataJoined[i][indData] = dataJoinedWithDuplicata[i][j];
                    indData++;
                }
            }
            indData = 0;
        }

        Table join = new Table(enteteJoined, dataJoined);
        Table[] u = { t1, t2 };
        join.setUsedTables(u);
        return join;
    }

    public Table produitCartesien(Table t1, Table t2) throws Exception {
        /// Produit entete
        Object[] entete1 = t1.getEntete();
        Object[] entete2 = t2.getEntete();
        Object[] enteteProd = fusionnerListObj(entete1, entete2);

        /// Produit data
        Object[][] data1 = t1.getData();
        Object[][] data2 = t2.getData();

        Object[][] infos1 = data1;
        Object[][] infos2 = data2;

        EcritLire e = new EcritLire();

        if (t1.getNom() != null) {
            Object[] type = e.getTypesTableInFile(t1.getNom());
            if (type[0].toString().equalsIgnoreCase("string") == false) {
                infos1 = tri2Dim(data1);
                infos2 = tri2Dim(data2);
            }
        } else {
            infos1 = tri2Dim(data1);
            infos2 = tri2Dim(data2);
        }
        int nbLignes = data1.length * infos2.length;
        // System.out.println("nb lignes "+nbLignes);
        int nbCol = infos1[0].length + infos2[0].length;
        // System.out.println(infos2[0].length);

        Object[][] rep = new Object[nbLignes][nbCol];

        int k2 = 0;
        int indLigneInfo1 = 0;
        int indLigneInfo2 = 0;

        for (int i = 0; i < nbLignes; i++) {
            // System.out.println("ind ligne info2 -> "+indLigneInfo2);

            if (indLigneInfo2 >= infos2.length) {
                indLigneInfo2 = 0;
                indLigneInfo1++;
            }
            for (int k = 0; k < infos1[0].length; k++) {
                rep[i][k] = infos1[indLigneInfo1][k];
                k2 = k;
            }

            int k3 = k2 + 1;
            for (int l = 0; l < infos2[0].length; l++) {
                rep[i][k3 + l] = infos2[indLigneInfo2][l];
            }

            if (indLigneInfo2 < infos2.length) {
                // System.out.println("ind ligne info2 -> "+indLigneInfo2);
                indLigneInfo2++;
            }
        }

        Table p = new Table(enteteProd, rep);
        Table[] u = { t1, t2 };
        p.setUsedTables(u);
        return p;
    }

    public Table projection(Object[] entete, Object[][] data, String[] selections) throws Exception {
        Vector enteteProjVect = new Vector();
        for (int i = 0; i < entete.length; i++) { // selection entete
            for (int j = 0; j < selections.length; j++) {
                if (entete[i].toString().equalsIgnoreCase(selections[j])) {
                    enteteProjVect.add(i);
                }
            }
        }

        Object[] enteteProj = new Object[enteteProjVect.size()];
        Object[] listInd = enteteProjVect.toArray();

        int ind = 0;
        for (int i = 0; i < entete.length; i++) {
            int k = Integer.parseInt(listInd[ind].toString());
            if (i == k) {
                enteteProj[ind] = entete[i];
                // System.out.println(enteteProj[ind]);
                ind++;
                i = 0;
            }

            if (ind >= enteteProjVect.size()) {
                break;
            }
        }

        Object[][] dataSelect = new Object[data.length][enteteProjVect.size()];

        int indProj = 0;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                // System.out.println("hellooo");
                // System.out.println(j);
                if (j == (int) listInd[indProj]) {
                    dataSelect[i][indProj] = data[i][j];
                    // System.out.println(data[i][j]);
                    indProj++;
                    j = 0;
                }

                if (indProj >= dataSelect[0].length) {
                    break;
                }
            }
            indProj = 0;
        }

        return new Table(enteteProj, dataSelect);
    }

    public boolean isDoublureString(Object[] lo1, Object[] lo2) {
        for (int i = 0; i < lo1.length; i++) {
            if (lo1[i].toString().equals(lo2[i].toString()) == false) {
                return false;
            }
        }
        return true;
    }

    public void decrire(Object[] lo) {
        for (Object object : lo) {
            System.out.print(object + "  ");
        }
        System.out.print(" | ");
    }

    public Table intersection(Table le1, Table le2) throws Exception {
        System.out.println("Intersection");
        System.out.println();
        Table u = new Table();
        Table[] usedTab = { le1, le2 };
        u.setUsedTables(usedTab);
        Object[][] data1 = le1.getData();
        Object[][] data2 = le2.getData();
        Vector interVect = new Vector();

        for (Object[] ligne1 : data1) {
            for (Object[] ligne2 : data2) {
                // decrire(ligne1); decrire(ligne2);
                // System.out.println(isDoublure(ligne1, ligne2));
                // System.out.println();
                if (isDoublureString(ligne1, ligne2) == true) {
                    interVect.add(ligne1);
                }
            }
        }

        // System.out.println("aoue "+interVect.size());
        Object[][] inter = vectorToObj2Dim(interVect);

        u.setEntete(le1.getEntete());
        u.setData(inter);
        return u;
    }

    public Object[][] tri2Dim(Table t) throws Exception {
        Object[][] lo2 = t.getData();
        Object[][] rep = new Object[lo2.length][lo2[0].length];
        Vector vToTri = new Vector();
        // System.out.println("lo2.length " +lo2.length);
        for (int i = 0; i < lo2.length; i++) {
            vToTri.add(lo2[i][0]); // tri par colonne 1 par defaut
        }

        EcritLire e = new EcritLire();
        Object[] type = e.getTypesTableInFile(t.getNom());
        String typeStr = type[0].toString();
        if (typeStr.equalsIgnoreCase("string") == false && typeStr.equalsIgnoreCase("date") == false
                && typeStr.equalsIgnoreCase("boolean") == false) {
            System.out.println("tri numerique");
            vToTri.sort(null); // tri numerique
            Object[] indTri = vToTri.toArray();

            for (Object[] ligne : lo2) {
                for (int i = 0; i < indTri.length; i++) {
                    if (Integer.parseInt(indTri[i].toString()) == Integer.parseInt(ligne[0].toString())) {
                        rep[i] = ligne;
                    }
                }
            }
        } else {
            System.out.println("tri alphabetique");
            Collections.sort(vToTri); // tri alphabetique
            // System.out.println(vToTri.size());
            Object[] indTri = vToTri.toArray();

            for (Object[] ligne : lo2) {
                for (int i = 0; i < indTri.length; i++) {
                    if (indTri[i].toString().compareTo(ligne[0].toString()) == 0) {
                        rep[i] = ligne;
                        // if(R == 4) {
                        // System.out.print("ligne "+i+" : ");
                        // for (Object object : ligne) {
                        // System.out.print(object+" ");
                        // }
                        // System.out.println();
                        // }
                    }
                }
            }
        }

        return rep;
    }

    public Object[][] tri2Dim(Object[][] lo2) throws Exception {
        Object[][] rep = new Object[lo2.length][lo2[0].length];
        Vector vToTri = new Vector();
        for (int i = 0; i < lo2.length; i++) {
            vToTri.add(lo2[i][0]); // tri par colonne 1 par defaut
        }

        EcritLire e = new EcritLire();
        Object[] type = e.getTypesTableInFile("inscription");
        String typeStr = type[0].toString();
        if (typeStr.equalsIgnoreCase("string") == true) {
            vToTri.sort(null); // tri
        }

        Object[] indTri = vToTri.toArray();

        for (Object[] ligne : lo2) {
            for (int i = 0; i < indTri.length; i++) {
                if (Integer.parseInt(indTri[i].toString()) == Integer.parseInt(ligne[0].toString())) {
                    rep[i] = ligne;
                }
            }
        }

        return rep;
    }

    public Object[][] enlevDoublure(Table t) throws Exception {
        Object[][] lo = t.getData();
        EcritLire e = new EcritLire();
        Object[] type = e.getTypesTableInFile(t.getNom());
        Object[][] vitatri = lo;
        // if(type[0].toString().equalsIgnoreCase("string") == false) {
        vitatri = tri2Dim(t);
        // System.out.println("ouuuut table "+t.getNom());
        // displayAll(new Table(t.getEntete(), vitatri));
        // }
        Vector ligneVect = new Vector();
        Object[] ligneInitial = vitatri[0];
        ligneVect.add(ligneInitial);

        for (int i = 0; i < vitatri.length; i++) {
            // System.out.println(isDoublure(ligneInitial, vitatri[i]));
            if (isDoublureString(ligneInitial, vitatri[i]) == false) {
                ligneInitial = vitatri[i];
                ligneVect.add(ligneInitial);
            }
        }

        Object[][] rep = vectorToObj2Dim(ligneVect);
        return rep;
    }

    public Object[][] enlevDoublure(Object[][] lo) throws Exception {
        EcritLire e = new EcritLire();
        Object[] type = e.getTypesTableInFile("inscription");
        Object[][] vitatri = lo;
        if (type[0].toString().equalsIgnoreCase("string") == false) {
            vitatri = tri2Dim(lo);
        }
        Vector ligneVect = new Vector();
        Object[] ligneInitial = vitatri[0];
        ligneVect.add(ligneInitial);

        for (int i = 0; i < vitatri.length; i++) {
            // System.out.println(isDoublure(ligneInitial, vitatri[i]));
            if (isDoublureString(ligneInitial, vitatri[i]) == false) {
                ligneInitial = vitatri[i];
                ligneVect.add(ligneInitial);
            }
        }

        Object[][] rep = vectorToObj2Dim(ligneVect);
        return rep;
    }

    public boolean isDoublure(Object[] lo1, Object[] lo2) {
        if (lo1.equals(lo2)) {
            return true;
        }
        return false;
    }

    public Object[][] fusionTwo2Dim(Object[][] l21, Object[][] l22) {
        int nbLignes = l21.length + l22.length;
        int nbCol = l21[0].length;
        Object[][] rep = new Object[nbLignes][nbCol];
        int indLigne = 0;
        int indCol = 0;

        for (int i = 0; i < l21.length; i++) {
            for (int j = 0; j < nbCol; j++) {
                rep[indLigne][indCol] = l21[i][j];
                indCol++;
            }
            indLigne++;
            indCol = 0;
        }

        indCol = 0;
        for (int k = 0; k < l22.length; k++) {
            for (int l = 0; l < nbCol; l++) {
                rep[indLigne][indCol] = l22[k][l];
                indCol++;
            }
            indLigne++;
            indCol = 0;
        }

        return rep;
    }

    public Table union(Table le1, Table le2) throws Exception {
        System.out.println("Union");
        System.out.println();

        Table u = new Table();
        Object[][] data = fusionTwo2Dim(le1.getData(), le2.getData());
        Object[][] delDouble = enlevDoublure(data);
        u.setEntete(le1.getEntete());
        u.setData(delDouble);
        Table[] t = { le1, le2 };
        u.setUsedTables(t);
        return u;
    }
}