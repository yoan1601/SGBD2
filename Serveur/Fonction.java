package inc;

import java.util.Vector;

import automate.Grammaire;
import inc.FonctionV2;
import inputOutput.EcritLire;
import objets.*;

import java.lang.reflect.*;

public class Fonction {

    public Table update(Table table, String [][] setsUpdate, String [][] wheresUpdate) throws Exception {
        int [] indColUpdate = getIndiceCol(table, setsUpdate);
        int [] indLigneUpdate = getIndiceLigne(table, wheresUpdate);

        Object [] entete = table.getEntete();
        Object [][] data = table.getData();

        for (int i = 0; i < indLigneUpdate.length; i++) {
            for (int j = 0; j < indColUpdate.length; j++) {
                data[indLigneUpdate[i]][indColUpdate[j]] = setsUpdate[j][2];
            }
        }

        Table rep = new Table(entete, data);
        rep.setNom(table.getNom());

        EcritLire e = new EcritLire();
        e.ecraser(table.getNom(), rep);

        return rep;
    }

    public int [] getIndiceLigne (Table table, String [][] wheresUpdate) throws Exception{
        Vector indVect = new Vector();
        int [] indCol = getIndiceCol(table, wheresUpdate);
        int nbCol = indCol.length;
        Object[][] data = table.getData();
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < nbCol; j++) {
                if(data[i][indCol[j]].toString().equals(wheresUpdate[j][2]) == false) break;
                if(j == nbCol - 1) indVect.add(i);
            }
        }

        int [] rep = vectToTabInt(indVect);
        return rep;
    }
    public int [] vectToTabInt(Vector v) throws Exception {
        if(v.size() == 0) throw new Exception("Vecto size 0 -> vectToTabInt");
        int [] rep = new int[v.size()];
        for (int i = 0; i < v.size(); i++) {
            rep[i] = (int) v.get(i);
        }
        return rep;
    }

    public int [] getIndiceCol (Table table, String [][] setsUpdate) throws Exception {
        Vector indVect = new Vector();
        Object[] colonnes = table.getEntete();
        for (int j = 0; j < setsUpdate.length; j++) {
            for (int j2 = 0; j2 < colonnes.length; j2++) {
                if(colonnes[j2].toString().equalsIgnoreCase(setsUpdate[j][0]) == true) {
                    indVect.add(j2);
                }
            }
        }
        int [] rep = vectToTabInt(indVect);
        return rep;
    }

    public static String[] decompose(String str) {
        String [] strSplit = str.split(" ");
        String [] strTrim = trimAll(strSplit);
        return strTrim;
    }

    public static String[][] getwheresUpdate(String requete, String set, String where, String and) {

        String [] setSplit = requete.split(set);
        String [] whereSplit = setSplit[1].split(where);
       
        String wheres = whereSplit[1];

        String [] whereTab = wheres.split(and);

        String [] whereDecomp = Fonction.decompose(whereTab[0]); // [0] col ; [1] operateur ; [2] value

        String [][] wheresUpdate = new String[whereTab.length][whereDecomp.length];
        for (int i = 0; i < wheresUpdate.length; i++) {
            wheresUpdate[i] = Fonction.decompose(whereTab[i]);
        }

        return wheresUpdate;
    }

    public static String[][] getsetsUpdate(String requete, String set, String where, String and) {

        String [] setSplit = requete.split(set);
        String [] whereSplit = setSplit[1].split(where);
        String sets = whereSplit[0];
        // String wheres = whereSplit[1];

        String [] setTab = sets.split(and);

        String [] setDecomp = Fonction.decompose(setTab[0]); // [0] col ; [1] operateur ; [2] value

        String [][] setsUpdate = new String[setTab.length][setDecomp.length];
        for (int i = 0; i < setsUpdate.length; i++) {
            setsUpdate[i] = Fonction.decompose(setTab[i]);
        }

        return setsUpdate;
    }

    public static String [] getValuesInsert (String requete) {
        String colTypeBloc = requete.substring(requete.lastIndexOf("(") + 1, requete.lastIndexOf(")"));
        String[] colTypeParsed = colTypeBloc.split(",");
        String [] colTypeTrim = Fonction.trimAll(colTypeParsed);   
        return colTypeTrim;
    }

    public static String [][] getColType (String requete) {
        String colTypeBloc = requete.substring(requete.lastIndexOf("(") + 1, requete.lastIndexOf(")"));
        String[] colTypeParsed = colTypeBloc.split(",");
        String [][] colType = new String[colTypeParsed.length][2]; // [i][0] nomCol ; [i][1] type
        int i = 0;
        for (String colTypeSep : colTypeParsed) {
            String [] colTypeOutSp = colTypeSep.split(" ");
            String [] colTypeTrim = Fonction.trimAll(colTypeOutSp);  
            colType[i] = colTypeTrim;
            i++;           
        }
        return colType;
    }

    public int[] getIndColonneTab(String colonne, Table table) throws Exception {
        Vector indVect = new Vector();
        if (colonne.contains("->") == true) {
            colonne = colonne.split("->")[1];
        }
        Object[] colonnes = table.getEntete();
        for (int i = 0; i < colonnes.length; i++) {
            if (colonne.equalsIgnoreCase(colonnes[i].toString()) == true) {
                indVect.add(i);
            }
        }

        if (indVect.size() == 0)
            throw new Exception("Aucune colonne ne correspond a " + colonne);

        int[] rep = new int[indVect.size()];
        for (int i = 0; i < rep.length; i++) {
            rep[i] = (int) indVect.get(i);
        }

        return rep;
    }

    public static Table traiterCondition2(Table table, String[][] condDecomp) throws Exception {
        FonctionV2 f2 = new FonctionV2();
        for (int i = 0; i < condDecomp.length; i++) {
            String[] condition = condDecomp[i];
            String[] tableColonne = condition[0].split("->"); // ex : table->nomColonne
            Table temp = new Table();
            table.setNom(tableColonne[0]);
            // System.out.println("etoooo");
            int idUsedTable = table.getIdUsedTable(tableColonne[0]);
            // System.out.println("id table "+idUsedTable);
            // System.out.println("--------> "+table.getNom());
            if (idUsedTable == 0) {
                temp = f2.selection(table, tableColonne[1], condition[1], condition[2]); // [0] : colonne ; [1] :
                                                                                         // operateur ; [2] : valeur
                temp.setNom(table.getNom());
            } else {
                temp = f2.selection(table, tableColonne[1], condition[1], condition[2], idUsedTable); // [0] : colonne ;
                                                                                                      // [1] : operateur
                                                                                                      // ; [2] : valeur
                temp.setNom(table.getNom());
            }
            table = temp;
        }
        return table;
    }

    public static Table traiterCondition(Table table, String[][] condDecomp) throws Exception {
        FonctionV2 f2 = new FonctionV2();
        for (String[] condition : condDecomp) {
            Table temp = f2.selection(table, condition[0], condition[1], condition[2]); // [0] : colonne ; [1] :
                                                                                        // operateur ; [2] : valeur
            temp.setNom(table.getNom());
            table = temp;
        }
        return table;
    }

    public static String[] getCondition(String condition) {
        String[] rep = new String[3]; // [0] : colonne ; [1] : operateur ; [2] : valeur
        String[] condSplitSpace = condition.split(" ");
        String[] condDecomp = trimAll(condSplitSpace);
        rep[0] = condDecomp[0];
        rep[1] = condDecomp[1];
        rep[2] = condDecomp[2];
        return rep;
    }

    public static String[][] getConditions(String requete, String where, String separateur) {

        int whereLength = where.length();
        int afterWhereIndex = requete.lastIndexOf(where) + whereLength;
        String conditionsBloc = requete.substring(afterWhereIndex);
        String[] conditions = conditionsBloc.split(separateur);

        String[][] condSeparated = new String[conditions.length][3]; // [0] : colonne ; [1] : operateur ; [2] : valeur
        for (int i = 0; i < conditions.length; i++) {
            condSeparated[i] = getCondition(conditions[i]);
        }

        return condSeparated;

    }

    public int getIndColonne(String colonne, Table table) throws Exception {
        if (colonne.contains("->") == true) {
            colonne = colonne.split("->")[1];
            // System.out.println("colonne "+colonne);
            // String[] tabCol = colonne.split(".");
            // System.out.println(tabCol[0]);
            // colonne = tabCol[1];
        }
        Object[] colonnes = table.getEntete();
        for (int i = 0; i < colonnes.length; i++) {
            if (colonne.equalsIgnoreCase(colonnes[i].toString()) == true) {
                System.out.println(i);
                return i;
            }
        }

        throw new Exception("Aucune colonne ne correspond a " + colonne);
    }

    public static String[] trimAll(String[] r) {
        Vector repVect = new Vector<String>();
        for (int i = 0; i < r.length; i++) {
            if (r[i] != "") {
                repVect.add(r[i].trim());
                // System.out.print("|"+r[i]+"|");
            }
        }
        System.out.println();

        String[] rep = new String[repVect.size()];
        for (int i = 0; i < repVect.size(); i++) {
            rep[i] = (String) repVect.get(i);
        }
        return rep;
    }

    public Table getInDataBaseFile(String nomTable) throws Exception {
        Table tab = new Table();
        EcritLire e = new EcritLire();
        Object[] entete = e.getEnteteTableInFile(nomTable);
        Object[][] data = e.lire(nomTable);
        tab.setEntete(entete);
        tab.setData(data);
        return tab;
    }

    public Grammaire[] getMotsCles() {
        Grammaire atambatra = new Grammaire("atambatra"); // 0
        Grammaire itovizana = new Grammaire("itovizana"); // 1
        Grammaire aoe = new Grammaire("ao"); // 2
        Grammaire ny = new Grammaire("ny"); 
        Grammaire sy = new Grammaire("sy"); // 3
        Grammaire ampifandraiso = new Grammaire("ampifandraiso"); //4
        Grammaire produit = new Grammaire("produit"); //5
        Grammaire analana = new Grammaire("analana"); // difference //6
        Grammaire aminy = new Grammaire("@"); //7
        Grammaire division = new Grammaire("division"); //8
        Grammaire ataovy = new Grammaire("ataovy"); //9
        atambatra.setNext(ny);
        itovizana.setNext(ny);
        ampifandraiso.setNext(ny);
        produit.setNext(ny);
        Grammaire[] rep = { atambatra, itovizana, aoe, sy, ampifandraiso, produit, analana, aminy, division, ataovy };
        return rep;
    }

    public Table traiterRequete(String requete) throws Exception {
        System.out.println("Votre requete : " + requete);
        String[] requeteD = requete.split(" ");
        String[] requeteDecomp = trimAll(requeteD);
        // System.out.println("nb syntaxes : "+requeteDecomp.length);
        // for (String string : requeteDecomp) {
        // System.out.print(string);
        // }
        Grammaire[] grammPrincipaux = getGrammairesPrincipaux();
        Grammaire[] motsCles = getMotsCles();
        Table relation = new Table();
        EcritLire ec = new EcritLire();

        ///// conditions
        String where = "izay";
        String and = "ary";
        String set = "ataovy";

        // bloc 1 tsy mitovy "alaivo"
        if (requeteDecomp[0].equalsIgnoreCase(grammPrincipaux[0].getSyntaxe()) == false) { // else de alaivo
            // throw new Exception("Requete invalide : -> "+requeteDecomp[0]+"... reste
            // ignore");
            if(requeteDecomp[0].equalsIgnoreCase(grammPrincipaux[5].getSyntaxe()) == true) { //create 
                if(requeteDecomp[1].equalsIgnoreCase(grammPrincipaux[6].getSyntaxe()) == true) { //table
                    String nomTable = requeteDecomp[2]; //nomTable
                    int tailleReq = requeteDecomp.length;
                    if(requeteDecomp[tailleReq - 1].equalsIgnoreCase(")") == true) { //si requete creqte se termine par ")"
                        String [][] colType = getColType(requete);
                        ec.EcritDescr(nomTable, colType);
                        Object[] descCreated = ec.getEnteteTableInFile(nomTable);
                        Object[][]message = new Object[1][descCreated.length];
                        for (int i = 0; i < descCreated.length; i++) {
                            message[0][i] = "--";
                            // System.out.println(descCreated.length);
                        }
                        System.out.println("Table created with succes");
                        relation = new Table(descCreated, message);
                        relation.setNom(nomTable);
                    }
                    else {
                        throw new Exception("the create request isn't correctly ended (need a ')') "+requete+" instead "+requeteDecomp[tailleReq - 1]);
                    }
                }
                else {
                    throw new Exception("requete invalide a partir de " + requeteDecomp[1]);
                }
            }
        
            else if(requeteDecomp[0].equalsIgnoreCase(grammPrincipaux[8].getSyntaxe()) == true) { //insert
                if(requeteDecomp[1].equalsIgnoreCase(grammPrincipaux[4].getSyntaxe()) == true) { //ao
                    String nomTable = requeteDecomp[2];
                    String [] values = getValuesInsert(requete);
                    ec.insert(nomTable, values);
                    System.out.println("insertion effectuee avec succes");
                    Object[][] data = ec.lire(nomTable);
                    Object[] entete = ec.getEnteteTableInFile(nomTable);
                    relation = new Table(entete, data);
                    relation.setNom(nomTable);
                }
                else {
                    throw new Exception("requete invalide a partir de " + requeteDecomp[1]);
                }
            }

            else if(requeteDecomp[0].equalsIgnoreCase(grammPrincipaux[9].getSyntaxe()) == true) { //update
                String nomTable = requeteDecomp[1];
                if (ec.isInDatabase(nomTable) == true) {
                    if(requeteDecomp[2].equalsIgnoreCase(motsCles[9].getSyntaxe()) == true) { //ataovy
                        String [][] setsUpdate = getsetsUpdate(requete, set, where, and); //les a updater  
                        String [][] wheresUpdate = getwheresUpdate(requete, set, where, and); 
                        Object[][] data = ec.lire(nomTable);
                        Object[] entete = ec.getEnteteTableInFile(nomTable);
                        Table table = new Table(entete, data);
                        table.setNom(nomTable);
                        Table update = update(table, setsUpdate, wheresUpdate);
                        return update;
                    }
                    else {
                        throw new Exception("requete invalide a partir de " + requeteDecomp[2]);
                    }
                }
                else {
                    throw new Exception("aucun database ne correspond a " + nomTable);
                }
            }
            else {
                throw new Exception("requete invalide a partir de " + requeteDecomp[0]);
            }
        }

        else {
            // //bloc 2 tsy mitovy "*" sady tsy mitovy "ny"
            // if(requeteDecomp[1].equalsIgnoreCase(grammPrincipaux[1].getSyntaxe()) ==
            // false && requeteDecomp[1].equalsIgnoreCase(grammPrincipaux[2].getSyntaxe())
            // == false)
            // throw new Exception("Requete invalide : -> "+requeteDecomp[1]+"... reste
            // ignore");

            // bloc 2 == "*"
            if (requeteDecomp[1].equalsIgnoreCase(grammPrincipaux[1].getSyntaxe()) == true) {
                // System.out.println("bloc 2 == '*'");
                // union "atambatra"
                if (requeteDecomp[2].equalsIgnoreCase(motsCles[0].getSyntaxe()) == true) {
                    if (requeteDecomp[3].equalsIgnoreCase(motsCles[0].getNext().getSyntaxe()) == true) { // ny
                        String table = requeteDecomp[4];
                        if (ec.isInDatabase(table) == true) {
                            if (requeteDecomp[5].equalsIgnoreCase(motsCles[3].getSyntaxe()) == true) { // sy
                                String table2 = requeteDecomp[6];
                                if (ec.isInDatabase(table2) == true) {
                                    FonctionV2 f2 = new FonctionV2();
                                    Object[] entete1 = ec.getEnteteTableInFile(table);
                                    Object[][] data1 = ec.lire(table);
                                    Table t1 = new Table(entete1, data1);
                                    t1.setNom(table);
                                    Object[] entete2 = ec.getEnteteTableInFile(table2);
                                    Object[][] data2 = ec.lire(table2);
                                    Table t2 = new Table(entete2, data2);
                                    t2.setNom(table2);
                                    Table union = f2.union(t1, t2);
                                    union.setNom(t1.getNom());

                                    /////// Verifier si existe condition(s)
                                    if (requete.contains(where) == true) {
                                        String[][] condDecomp = getConditions(requete, where, and);
                                        union = traiterCondition(union, condDecomp);
                                    }
                                    //////

                                    return union;
                                } else
                                    throw new Exception("aucun database ne correspond a " + table2);
                            }

                            else
                                throw new Exception("requete invalide a partir de " + requeteDecomp[5]);
                        } else {
                            throw new Exception("aucun database ne correspond a " + table);
                        }
                    } else {
                        throw new Exception("requete invalide a partir de " + requeteDecomp[3]);
                    }
                }
                // intersection "itovizana"
                else if (requeteDecomp[2].equalsIgnoreCase(motsCles[1].getSyntaxe()) == true) {
                    if (requeteDecomp[3].equalsIgnoreCase(motsCles[1].getNext().getSyntaxe()) == true) { // ny
                        String table = requeteDecomp[4];
                        if (ec.isInDatabase(table) == true) {
                            if (requeteDecomp[5].equalsIgnoreCase(motsCles[3].getSyntaxe()) == true) { // sy
                                String table2 = requeteDecomp[6];
                                if (ec.isInDatabase(table2) == true) {
                                    FonctionV2 f2 = new FonctionV2();
                                    Object[] entete1 = ec.getEnteteTableInFile(table);
                                    Object[][] data1 = ec.lire(table);
                                    Table t1 = new Table(entete1, data1);
                                    t1.setNom(table);
                                    Object[] entete2 = ec.getEnteteTableInFile(table2);
                                    Object[][] data2 = ec.lire(table2);
                                    Table t2 = new Table(entete2, data2);
                                    t2.setNom(table2);
                                    Table inter = f2.intersection(t1, t2);
                                    inter.setNom(t1.getNom());
                                    if (requete.contains(where) == true) {
                                        String[][] condDecomp = getConditions(requete, where, and);
                                        inter = traiterCondition(inter, condDecomp);
                                    }
                                    return inter;
                                } else
                                    throw new Exception("aucun database ne correspond a " + table2);
                            } else
                                throw new Exception("requete invalide a partir de " + requeteDecomp[5]);
                        } else {
                            throw new Exception("aucun database ne correspond a " + table);
                        }
                    } else {
                        throw new Exception("requete invalide a partir de " + requeteDecomp[3]);
                    }
                }
                // Selection "ao"
                else if (requeteDecomp[2].equalsIgnoreCase(motsCles[2].getSyntaxe()) == true) {
                    String table = requeteDecomp[3];
                    if (ec.isInDatabase(table) == true) {
                        // String where = "izay";
                        if (requeteDecomp[4].equalsIgnoreCase(where) == true) {
                            String colonne = requeteDecomp[5];
                            if (ec.isColonneExist(colonne, table) == true) {
                                String operateur = requeteDecomp[6];
                                Object[] colonnes = ec.getEnteteTableInFile(table);
                                Object[][] data = ec.lire(table);
                                Table selectAll = new Table(table, colonnes, data);
                                selectAll.setNom(table);
                                String condition = requeteDecomp[7];
                                FonctionV2 f2 = new FonctionV2();

                                return f2.selection(selectAll, colonne, operateur, condition);
                            }
                        } else
                            throw new Exception("requete invalide a partir de " + requeteDecomp[4]);
                    }
                }
                // jointure "ampifandraiso" natural join --> misy colonne 1 itovizana
                else if (requeteDecomp[2].equalsIgnoreCase(motsCles[4].getSyntaxe()) == true) {
                    if (requeteDecomp[3].equalsIgnoreCase(motsCles[0].getNext().getSyntaxe()) == true) { // ny
                        String table = requeteDecomp[4];
                        if (ec.isInDatabase(table) == true) {
                            if (requeteDecomp[5].equalsIgnoreCase(motsCles[3].getSyntaxe()) == true) { // sy
                                String table2 = requeteDecomp[6];
                                if (ec.isInDatabase(table2) == true) {
                                    FonctionV2 f2 = new FonctionV2();
                                    Object[] entete1 = ec.getEnteteTableInFile(table);
                                    Object[][] data1 = ec.lire(table);
                                    Table t1 = new Table(entete1, data1);
                                    t1.setNom(table);
                                    Object[] entete2 = ec.getEnteteTableInFile(table2);
                                    Object[][] data2 = ec.lire(table2);
                                    Table t2 = new Table(entete2, data2);
                                    t2.setNom(table2);
                                    Table join = f2.naturalJoin(t1, t2);
                                    join.setNom(t1.getNom());
                                    if (requete.contains(where) == true) {
                                        String[][] condDecomp = getConditions(requete, where, and);
                                        // join = traiterCondition(join, condDecomp);
                                        if (requete.contains("->") == true) {
                                            join = traiterCondition2(join, condDecomp);
                                        } else {
                                            join = traiterCondition(join, condDecomp);
                                        }
                                    }
                                    return join;
                                } else
                                    throw new Exception("aucun database ne correspond a " + table2);
                            }

                            else
                                throw new Exception("requete invalide a partir de " + requeteDecomp[5]);
                        } else {
                            throw new Exception("aucun database ne correspond a " + table);
                        }
                    } else {
                        throw new Exception("requete invalide a partir de " + requeteDecomp[3]);
                    }
                }
                // Produit cartesien "produit"
                else if (requeteDecomp[2].equalsIgnoreCase(motsCles[5].getSyntaxe()) == true) {
                    if (requeteDecomp[3].equalsIgnoreCase(motsCles[0].getNext().getSyntaxe()) == true) { // ny
                        String table = requeteDecomp[4];
                        if (ec.isInDatabase(table) == true) {
                            if (requeteDecomp[5].equalsIgnoreCase(motsCles[3].getSyntaxe()) == true) { // sy
                                String table2 = requeteDecomp[6];
                                if (ec.isInDatabase(table2) == true) {
                                    FonctionV2 f2 = new FonctionV2();
                                    Object[] entete1 = ec.getEnteteTableInFile(table);
                                    Object[][] data1 = ec.lire(table);
                                    Table t1 = new Table(entete1, data1);
                                    t1.setNom(table);
                                    Object[] entete2 = ec.getEnteteTableInFile(table2);
                                    Object[][] data2 = ec.lire(table2);
                                    Table t2 = new Table(entete2, data2);
                                    t2.setNom(table2);
                                    Table produit = f2.produitCartesien(t1, t2);
                                    produit.setNom(t1.getNom());
                                    if (requete.contains(where) == true) {
                                        String[][] condDecomp = getConditions(requete, where, and);
                                        if (requete.contains("->") == true) {
                                            produit = traiterCondition2(produit, condDecomp);
                                        } else {
                                            produit = traiterCondition(produit, condDecomp);
                                        }
                                    }
                                    return produit;
                                } else
                                    throw new Exception("aucun database ne correspond a " + table2);
                            }

                            else
                                throw new Exception("requete invalide a partir de " + requeteDecomp[5]);
                        } else {
                            throw new Exception("aucun database ne correspond a " + table);
                        }
                    } else {
                        throw new Exception("requete invalide a partir de " + requeteDecomp[3]);
                    }
                }
                // difference "analana"
                else if (requeteDecomp[2].equalsIgnoreCase(motsCles[6].getSyntaxe()) == true) {
                    if (requeteDecomp[3].equalsIgnoreCase(motsCles[0].getNext().getSyntaxe()) == true) { // ny
                        String table = requeteDecomp[4];
                        if (ec.isInDatabase(table) == true) {
                            if (requeteDecomp[5].equalsIgnoreCase(motsCles[7].getSyntaxe()) == true) { // @ : amin'ny
                                String table2 = requeteDecomp[6];
                                if (ec.isInDatabase(table2) == true) {
                                    FonctionV2 f2 = new FonctionV2();
                                    Object[] entete1 = ec.getEnteteTableInFile(table);
                                    Object[][] data1 = ec.lire(table);
                                    Table t1 = new Table(entete1, data1);
                                    t1.setNom(table);
                                    Object[] entete2 = ec.getEnteteTableInFile(table2);
                                    Object[][] data2 = ec.lire(table2);
                                    Table t2 = new Table(entete2, data2);
                                    t2.setNom(table2);
                                    Table difference = f2.difference(t1, t2);
                                    difference.setNom(t1.getNom());
                                    ////// condition
                                    if (requete.contains(where) == true) {
                                        String[][] condDecomp = getConditions(requete, where, and);
                                        difference = traiterCondition(difference, condDecomp);
                                    }
                                    //////
                                    return difference;
                                } else
                                    throw new Exception("aucun database ne correspond a " + table2);
                            }

                            else
                                throw new Exception("requete invalide a partir de " + requeteDecomp[5]);
                        } else {
                            throw new Exception("aucun database ne correspond a " + table);
                        }
                    } else {
                        throw new Exception("requete invalide a partir de " + requeteDecomp[3]);
                    }
                }
                // division "division"
                else if (requeteDecomp[2].equalsIgnoreCase(motsCles[8].getSyntaxe()) == true) {
                    if (requeteDecomp[3].equalsIgnoreCase(motsCles[0].getNext().getSyntaxe()) == true) { // ny
                        String table = requeteDecomp[4];
                        if (ec.isInDatabase(table) == true) {
                            if (requeteDecomp[5].equalsIgnoreCase(motsCles[7].getSyntaxe()) == true) { // @ : amin'ny
                                String table2 = requeteDecomp[6];
                                if (ec.isInDatabase(table2) == true) {
                                    FonctionV2 f2 = new FonctionV2();
                                    Object[] entete1 = ec.getEnteteTableInFile(table);
                                    Object[][] data1 = ec.lire(table);
                                    Table t1 = new Table(entete1, data1);
                                    t1.setNom(table);
                                    Object[] entete2 = ec.getEnteteTableInFile(table2);
                                    Object[][] data2 = ec.lire(table2);
                                    Table t2 = new Table(entete2, data2);
                                    t2.setNom(table2);
                                    Table division = f2.division(t1, t2);
                                    ////// condition
                                    if (requete.contains(where) == true) {
                                        String[][] condDecomp = getConditions(requete, where, and);
                                        division = traiterCondition(division, condDecomp);
                                    }
                                    //////
                                    return division;
                                } else
                                    throw new Exception("aucun database ne correspond a " + table2);
                            }

                            else
                                throw new Exception("requete invalide a partir de " + requeteDecomp[5]);
                        } else {
                            throw new Exception("aucun database ne correspond a " + table);
                        }
                    } else {
                        throw new Exception("requete invalide a partir de " + requeteDecomp[3]);
                    }
                }

                else {
                    throw new Exception("requete invalide a partir de " + requeteDecomp[2]);
                }
            }

            // bloc 2 == "colonnes" --> Projection
            else {
                System.out.println("bloc 2 == 'colonnes'");
                // gestion des colonnes
                String[] colonnes = requeteDecomp[1].split(",");
                // for (String string : colonnes) {
                // System.out.println(string);
                // }

                // bloc 3 tsy mitovy "ao"
                if (requeteDecomp[2].equalsIgnoreCase(grammPrincipaux[4].getSyntaxe()) == false)
                    throw new Exception("Requete invalide : -> " + requeteDecomp[3] + "... reste ignore");

                // bloc 3 == "ao" ---> projection
                else {
                    String table = requeteDecomp[3];
                    FonctionV2 f2 = new FonctionV2();
                    Object[] entete = ec.getEnteteTableInFile(table);
                    Object[][] data = ec.lire(table);
                    // relation = new Table(entete, data);
                    relation = f2.projection(entete, data, colonnes);
                    relation.setNom(table);
                }
            }

        }

        return relation;
    }

    public void setDatabaseToFile() {
        Elt elt1 = new Elt(1, "Elt1", "noir");
        Elt elt2 = new Elt(2, "Elt2", "blanc");
        Elt elt3 = new Elt(3, "Elt3", "rouge");
        Elt elt4 = new Elt(4, "Elt4", "vert");
        Elt elt5 = new Elt(5, "Elt5", "bleu");

        Elt elt6 = new Elt(6, "Elt6", "c6");
        Elt elt7 = new Elt(7, "Elt7", "c7");
        Elt elt8 = new Elt(8, "Elt8", "c8");
        Elt elt9 = new Elt(9, "Elt9", "c9");

        Elt[] tab1 = { elt1, elt2, elt3, elt7, elt5 };
        Elt[] tab2 = { elt6, elt7, elt1, elt9 };

        Employe e1 = new Employe(0, "Jean", 21, 0, 1);
        Employe e2 = new Employe(3, "Hare", 18, 2, 0);
        Employe e3 = new Employe(2, "Emp2", 69, 1, 0);

        Dept d1 = new Dept(0, "RH");
        Dept d2 = new Dept(1, "RDev");
        Dept d3 = new Dept(2, "Admin");

        Employe[] le = { e1, e2, e3 };
        Dept[] ld = { d1, d2, d3 };

        // ecriture fichier
        EcritLire e = new EcritLire();
        try {
            e.EcritDescr(tab1, "elt1");
            e.EcritDescr(tab2, "elt2");
            e.EcritDescr(le, "employe");
            e.EcritDescr(ld, "departement");

            e.ecritData(tab1, "elt1");
            e.ecritData(tab2, "elt2");
            e.ecritData(le, "employe");
            e.ecritData(ld, "departement");
        } catch (Exception ex) {
            System.out.println("something went wrong during ecriture fichier");
            System.out.println(ex.fillInStackTrace());
        }
    }

    public Vector getDatabase() {
        Elt elt1 = new Elt(1, "Elt1", "noir");
        Elt elt2 = new Elt(2, "Elt2", "blanc");
        Elt elt3 = new Elt(3, "Elt3", "rouge");
        Elt elt4 = new Elt(4, "Elt4", "vert");
        Elt elt5 = new Elt(5, "Elt5", "bleu");

        Elt elt6 = new Elt(6, "Elt6", "c6");
        Elt elt7 = new Elt(7, "Elt7", "c7");
        Elt elt8 = new Elt(8, "Elt8", "c8");
        Elt elt9 = new Elt(9, "Elt9", "c9");

        Elt[] tab1 = { elt1, elt2, elt3, elt7, elt5 };
        Elt[] tab2 = { elt6, elt7, elt1, elt9 };

        Employe e1 = new Employe(0, "Jean", 21, 0, 1);
        Employe e2 = new Employe(3, "Hare", 18, 2, 0);
        Employe e3 = new Employe(2, "Emp2", 69, 1, 0);

        Dept d1 = new Dept(0, "RH");
        Dept d2 = new Dept(1, "RDev");
        Dept d3 = new Dept(2, "Admin");

        Employe[] le = { e1, e2, e3 };
        Dept[] ld = { d1, d2, d3 };

        Vector db = new Vector();
        db.add(tab1);
        db.add(tab2);
        db.add(le);
        db.add(ld);

        return db;
    }

    public Object[] getInDatabase(String table) {
        Vector db = getDatabase();
        System.out.println("nom de la table demande " + table);
        for (int i = 0; i < db.size(); i++) {
            Object[] lo = (Object[]) db.get(i);
            Object o = lo[0];
            String nameObject = o.getClass().getSimpleName();
            // System.out.println("nom des objects "+nameObject);
            if (nameObject.equalsIgnoreCase(table) == true) {
                return lo;
            }
        }
        return null;
    }

    public Object[][] vectorToObj2Dim(Vector v) throws Exception {
        if (v.size() == 0)
            throw new Exception("Vector.size() = 0 -> vectorToObj2Dim");
        Object[] dataTemp = (Object[]) v.get(0);
        Object[][] rep = new Object[v.size()][dataTemp.length];
        for (int i = 0; i < v.size(); i++) {
            Object[] ligne = new Object[dataTemp.length];
            for (int j = 0; j < dataTemp.length; j++) {
                ligne[j] = ((Object[]) v.get(i))[j];
            }
            rep[i] = ligne;
        }
        return rep;
    }

    public boolean isInDatabase(String table) {
        Vector db = getDatabase();
        System.out.println("nom de la table demande " + table);
        for (int i = 0; i < db.size(); i++) {
            Object[] lo = (Object[]) db.get(i);
            Object o = lo[0];
            String nameObject = o.getClass().getSimpleName();
            // System.out.println("nom des objects "+nameObject);
            if (nameObject.equalsIgnoreCase(table) == true) {
                return true;
            }
        }
        return false;
    }

    public Grammaire[] getGrammairesPrincipaux() {
        Grammaire alaivo = new Grammaire("alaivo"); //0
        Grammaire etoile = new Grammaire("*"); //1
        Grammaire ny = new Grammaire("ny"); //2
        Grammaire sy = new Grammaire("sy"); //3
        Grammaire ao = new Grammaire("ao"); //4
        Grammaire create = new Grammaire("create"); //5
        Grammaire table = new Grammaire("table"); //6
        Grammaire values = new Grammaire("values"); //7
        Grammaire insert = new Grammaire("insert"); //8
        Grammaire update = new Grammaire("update"); //9

        Grammaire[] rep = { alaivo, etoile, ny, sy, ao, create, table ,values, insert, update };
        return rep;
    }

    public static String displayAllHTML(Table t) {
        Object[] entetes = t.getEntete();
        Object[][] data = t.getData();

        String contenu = "";
        String contenuTitre = "";

        for (Object titre : entetes) {
            String tdTitre = createTdWithClass("titre", titre.toString());
            contenuTitre = contenuTitre + tdTitre;
        }

        String trTitre = createTrWithClass("titres", contenuTitre);

        contenu = contenu + trTitre;

        for (Object[] ligne : data) {
            String donnees = "";
            for (Object donnee : ligne) {
                String d = createTdWithClass("donnee", donnee.toString());
                donnees = donnees + d;
            }
            String trData = createTrWithClass("ligne", donnees);
            contenu = contenu + trData;
        }

        String table = createTableHTML(contenu);
        return table;
    }

    public static String createTrWithClass(String id, String content) {
        return "<tr class='" + id + "''>" + content + "</tr>";
    }

    public static String createTdWithClass(String id, String content) {
        return "<td class='" + id + "''>" + content + "</td>";
    }

    public static String createTableHTML(String contenu) {
        return "<table>" + contenu + "</table>";
    }

    public static String createDivWithClass(String id, String content) {
        return "<div class='" + id + "''>" + content + "</div>";
    }

    public Table intersection(Object[] le1, Object[] le2) throws Exception {
        System.out.println("Intersection");
        System.out.println();

        Field[] lfTemp = le1[0].getClass().getDeclaredFields();
        int nbInter = getNbIntersection(le1, le2);
        int ind = 0;
        // System.out.println(nbInter);
        Object[][] rep = new Object[nbInter][lfTemp.length];
        for (int i = 0; i < le1.length; i++) {
            // System.out.println(ind);
            if (isIn(le1[i], le2)) {
                Field[] lf = le1[i].getClass().getDeclaredFields();
                for (int j = 0; j < lf.length; j++) {
                    lf[j].setAccessible(true);
                    rep[ind][j] = lf[j].get(le1[i]).toString();
                }
                ind++;
            }
        }

        Vector v = new Vector(); // insertion d'entete
        Field[] lf1 = le1[0].getClass().getDeclaredFields();

        for (Field field : lf1) { // satri de meme type le table de donc colonne ray iany donc tsisy le2
            v.add(field.getName());
        }

        Table t = new Table();
        t.setEntete(v.toArray());
        t.setData(rep);

        return t;
    }

    public Table join(Object[] lo1, Object[] lo2) throws Exception {
        System.out.println("Join");
        System.out.println();

        int ind = getIndCommonField(lo1, lo2);
        String nomCommun = lo1[0].getClass().getDeclaredFields()[ind].getName();

        int lenght = getTheLonger(lo1, lo2);

        Object[][] rep = new Object[lenght][];

        for (int i = 0; i < lenght; i++) {
            Object[] info1 = getInfoById(i, nomCommun, lo1);
            Object[] info2 = getInfoByIdDelDoublure(i, nomCommun, lo2, lo1);
            Object[] infos = fusionnerListObj(info1, info2);
            // String info2 = getInfoById(i, nomCommun, lo2);
            rep[i] = new Object[infos.length];
            for (int j = 0; j < infos.length; j++) {
                rep[i][j] = infos[j];
            }
        }

        int index = getIndCommonField(lo1, lo2);
        String nC = lo1[0].getClass().getDeclaredFields()[index].getName();

        Vector v = new Vector(); // insertion d'entete
        Field[] lf1 = lo1[0].getClass().getDeclaredFields();
        Field[] lf2 = lo2[0].getClass().getDeclaredFields();
        Field fCommun = lo2[0].getClass().getDeclaredField(nC);
        for (int i = 0; i < lf1.length; i++) {
            v.add(lf1[i].getName());
        }
        for (Field field : lf2) {
            if (field.getName() != fCommun.getName()) {
                v.add(field.getName());
            }
        }

        Table t = new Table();
        t.setEntete(v.toArray());
        t.setData(rep);

        return t;

    }

    public Table projection(Object[] lo, String[] selections) throws Exception {

        System.out.println("Projection");
        System.out.println();

        Object[][] rep = new Object[lo.length][selections.length];

        for (int i = 0; i < lo.length; i++) {
            for (int j = 0; j < selections.length; j++) {
                Field attr = lo[i].getClass().getDeclaredField(selections[j]);
                attr.setAccessible(true);
                Object attrValue = attr.get(lo[i]);
                rep[i][j] = attrValue.toString();
            }
            // System.out.println("");
        }

        Vector v = new Vector(); // insertion d'entete

        for (int i = 0; i < selections.length; i++) {
            Field f = lo[0].getClass().getDeclaredField(selections[i]);
            v.add(f.getName());
        }

        Table t = new Table();
        t.setEntete(v.toArray());
        t.setData(rep);

        return t;
    }

    public int getNbIntersection(Object[] le1, Object[] le2) throws Exception {
        System.out.println("Intersection");
        System.out.println();

        int nb = 0;

        Field[] lfTemp = le1[0].getClass().getDeclaredFields();
        Object[][] rep = new Object[le1.length][lfTemp.length];
        for (int i = 0; i < le1.length; i++) {
            if (isIn(le1[i], le2)) {
                nb++;
            }
        }
        return nb;
    }

    public Table produitCartesien(Object[] lo1, Object[] lo2) throws Exception {
        System.out.println("Produit Cartesien");
        System.out.println();

        Object[][] infos1 = getAllInfo(lo1, lo2); // lo2 c'est pour le tri
        Object[][] infos2 = getAllInfoDelDoublure(lo2, lo1);

        int nbLignes = infos1.length * infos2.length;
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

        int ind = getIndCommonField(lo1, lo2);
        String nomCommun = lo1[0].getClass().getDeclaredFields()[ind].getName();

        Vector v = new Vector(); // insertion d'entete
        Field[] lf1 = lo1[0].getClass().getDeclaredFields();
        Field[] lf2 = lo2[0].getClass().getDeclaredFields();
        Field fCommun = lo2[0].getClass().getDeclaredField(nomCommun);
        for (int i = 0; i < lf1.length; i++) {
            v.add(lf1[i].getName());
        }
        for (Field field : lf2) {
            if (field.getName() != fCommun.getName()) {
                v.add(field.getName());
            }
        }

        Table t = new Table();
        t.setEntete(v.toArray());
        t.setData(rep);

        return t;
    }

    public Object[][] getAllInfoDelDoublure(Object[] lo, Object[] lo2) throws Exception { // info rehetra tsy misy
                                                                                          // respect
        // id 0,1,2,...
        int ind = getIndCommonField(lo, lo2);

        String nomCommun = lo[0].getClass().getDeclaredFields()[ind].getName();
        Field[] lfTemp = lo[0].getClass().getDeclaredFields();

        Object[][] allInfo = new Object[lo.length][lfTemp.length - 1]; // satri miala le colonne itovizana
        Object[] loTri = trier(lo, nomCommun);

        int k = 0;

        for (int j = 0; j < loTri.length; j++) {
            Field[] lf = loTri[j].getClass().getDeclaredFields();
            for (int i = 0; i < lf.length; i++) {

                if (i != ind) { // tsy miditra valeur colonne itambarana am lo2
                    lf[i].setAccessible(true);
                    String val = lf[i].get(loTri[j]).toString();
                    // System.out.println(i);
                    allInfo[j][k] = val; // entete
                    k++;
                }
            }
            k = 0;
        }
        return allInfo;
    }

    public Object[][] getAllInfo(Object[] lo, Object[] lo2) throws Exception { // info rehetra tsy misy respect id
        // 0,1,2,...

        Field[] lfTemp = lo[0].getClass().getDeclaredFields();
        Object[][] allInfo = new Object[lo.length][lfTemp.length];

        int ind = getIndCommonField(lo, lo2);
        String nomCommun = lo[0].getClass().getDeclaredFields()[ind].getName();

        Object[] loTri = trier(lo, nomCommun);
        for (int j = 0; j < loTri.length; j++) {
            Field[] lf = loTri[j].getClass().getDeclaredFields();
            for (int i = 0; i < lf.length; i++) {
                lf[i].setAccessible(true);
                Object val = lf[i].get(loTri[j]).toString();
                allInfo[j][i] = val; // entete
                // System.out.print(val+" ");
            }
            // System.out.println();
        }

        return allInfo;
    }

    public Object[] fusionnerListObj(Object[] le1, Object[] le2) {

        int t1 = 0;
        int t2 = 0;

        if (le1 == null) {
            t1 = 0;
        } else {
            t1 = le1.length;
        }

        if (le2 == null) {
            t2 = 0;
        } else {
            t2 = le2.length;
        }

        Object[] rep = new Object[t1 + t2];
        int i = 0;
        if (le1 != null) {
            for (Object Object : le1) {
                rep[i] = Object;
                i++;
            }
        }

        if (le2 != null) {
            for (Object Object : le2) {
                rep[i] = Object;
                i++;
            }
        }

        return rep;
    }

    public Object[] getInfoByIdDelDoublure(int id, String attrId, Object[] lo, Object[] lo2) throws Exception {
        Vector info = new Vector();
        int ind = getIndCommonField(lo, lo2);
        String nomCommun = lo[0].getClass().getDeclaredFields()[ind].getName();

        Object[] loTri = trier(lo, attrId);

        for (Object object : loTri) {
            Field f = object.getClass().getDeclaredField(attrId);
            f.setAccessible(true);
            int num = f.getInt(object);
            Field[] lf1 = object.getClass().getDeclaredFields();
            if (num == id) {

                for (Field attribut : lf1) {
                    if (attribut.getName().compareTo(nomCommun) != 0) {
                        attribut.setAccessible(true);
                        String val = attribut.get(object).toString();
                        info.add(val); // entete
                    }
                }

                return info.toArray();
            }
        }

        Field[] lf1 = lo[0].getClass().getDeclaredFields();
        for (int i = 0; i < lf1.length; i++) {
            info.add("nan"); // entete
        }

        return info.toArray();
    }

    public Object[] getInfoById(int id, String attrId, Object[] lo) throws Exception {
        Vector info = new Vector();

        Object[] loTri = trier(lo, attrId);

        for (Object object : loTri) {
            Field f = object.getClass().getDeclaredField(attrId);
            f.setAccessible(true);
            int num = f.getInt(object);

            Field[] lf1 = object.getClass().getDeclaredFields();
            if (num == id) {
                for (Field attribut : lf1) {
                    attribut.setAccessible(true);
                    String val = attribut.get(object).toString();
                    info.add(val);
                }
                return info.toArray();
            }
        }

        Field[] lf1 = lo[0].getClass().getDeclaredFields();
        for (int i = 0; i < lf1.length; i++) {
            info.add("nan");
        }

        return info.toArray();
    }

    public Object[][] array1DimTo2Dim(Object[] lo) throws Exception {

        Field[] lfTemp = lo[0].getClass().getDeclaredFields();
        Object[][] rep = new Object[lo.length][lfTemp.length];

        for (int i = 0; i < lo.length; i++) {
            Field[] lf = lo[i].getClass().getDeclaredFields();
            for (int j = 0; j < lf.length; j++) {
                lf[j].setAccessible(true);
                rep[i][j] = lf[j].get(lo[i]).toString();
            }
        }

        return rep;
    }

    public Table selection(Object[] lo, String colonne, String comparaison, String strToCompareWith)
            throws Exception {
        System.out.println("SELECT * FROM " + lo[0].getClass().getSimpleName() + " WHERE " + colonne + " " + comparaison
                + " " + strToCompareWith);
        Vector rep = new Vector();
        for (Object o : lo) {
            Field f = o.getClass().getDeclaredField(colonne);
            f.setAccessible(true);
            String value = f.get(o).toString();
            if (comparaison.compareToIgnoreCase("like") == 0) {
                if (value.contains(strToCompareWith)) {
                    rep.add(o);
                }
            } else if (comparaison == "=") {
                if (value.compareTo(strToCompareWith) == 0) {
                    rep.add(o);
                }
            }
        }

        Vector v = new Vector(); // insertion d'entete
        Field[] lf1 = lo[0].getClass().getDeclaredFields();

        for (Field field : lf1) {
            v.add(field.getName());
        }

        Table t = new Table();
        t.setEntete(v.toArray());
        t.setData(array1DimTo2Dim(rep.toArray()));

        return t;
    }

    public void etablirEntete(Object[] lo1, Object[] lo2) throws Exception {
        // indice nom en commun d' attribut (attr tsy mitovy fa ny nom oui)
        int ind = getIndCommonField(lo1, lo2);
        String nomCommun = lo1[0].getClass().getDeclaredFields()[ind].getName();

        Object[] lo1Tri = trier(lo1, nomCommun);
        Object[] lo2Tri = trier(lo2, nomCommun);

        Field[] lf1 = lo1Tri[0].getClass().getDeclaredFields();
        Field[] lf2 = lo2Tri[0].getClass().getDeclaredFields();

        for (Field attribut : lf1) {
            System.out.print("|    " + attribut.getName() + "    |"); // entete
        }

        for (Field attribut : lf2) {
            if (compareToFieldsName(attribut.getName(), lf1) == false) { // non ajout des colonnes de memes noms
                System.out.print("|    " + attribut.getName() + "    |"); // entete
            }
        }

        System.out.println();
    }

    public static Object[] trier(Object[] lo, String attribut) throws Exception {
        for (int i = 0; i < lo.length; i++) {
            int indMin = min(lo, attribut, i);
            Object temp = lo[i];
            lo[i] = lo[indMin];
            lo[indMin] = temp;
        }
        return lo;
    }

    public static int min(Object[] lo, String attribut, int indInit) throws Exception {
        Object o1 = lo[indInit];
        int indMin = indInit;
        double min = 0;
        for (Method m : o1.getClass().getMethods()) {
            if (m.getName().compareToIgnoreCase("get" + attribut) == 0) {
                if (m.getReturnType() == int.class) {
                    min = (int) m.invoke(o1);
                } else {
                    min = (double) m.invoke(o1);
                }
            }
        }

        for (int i = indInit; i < lo.length; i++) {
            for (Method m : lo[i].getClass().getMethods()) {
                if (m.getName().compareToIgnoreCase("get" + attribut) == 0) {
                    if (m.getReturnType() == int.class) {
                        if ((int) m.invoke(lo[i]) < min) {
                            min = (int) m.invoke(o1);
                            indMin = i;
                        }
                    } else {
                        if ((double) m.invoke(lo[i]) < min) {
                            min = (double) m.invoke(lo[i]);
                            indMin = i;
                        }
                    }

                }
            }
        }

        return indMin;
    }

    public int getIndCommonField(Object[] lo1, Object[] lo2) {

        Field[] lf = lo1[0].getClass().getDeclaredFields();
        Field[] lf2 = lo2[0].getClass().getDeclaredFields();

        for (int i = 0; i < lf.length; i++) {
            for (int j = 0; j < lf2.length; j++) {
                if (lf[i].getName().compareTo(lf2[j].getName()) == 0) {
                    return i;
                }
            }

        }
        return -1;
    }

    public int getTheLonger(Object[] lo1, Object[] lo2) {
        if (lo1.length >= lo2.length)
            return lo1.length;
        return lo2.length;
    }

    public boolean compareToFieldsName(String name, Field[] lf) {
        for (Field field : lf) {
            if (field.getName().compareTo(name) == 0)
                return true;
        }
        return false;
    }

    public boolean isIn(Object o, Object[] lo) {
        for (Object object : lo) {
            if (o == object)
                return true;
        }
        return false;
    }

    public Table union(Object[] le1, Object[] le2) throws Exception {
        System.out.println("Union");
        System.out.println();

        int t1 = 0;
        int t2 = 0;

        if (le1 == null) {
            t1 = 0;
        } else {
            t1 = le1.length;
        }

        if (le2 == null) {
            t2 = 0;
        } else {
            t2 = le2.length;
        }

        // System.out.println(countDoublure(le1, le2));

        Object[] union = new Object[(t1 + t2) - countDoublure(le1, le2)];
        int i = 0;
        if (le1 != null) {
            for (Object Object : le1) {
                union[i] = Object;
                i++;
            }
        }

        if (le2 != null) {
            for (Object Object : le2) {
                if (!isIn(Object, le1)) {
                    union[i] = Object;
                    i++;
                }
            }
        }

        Vector v = new Vector(); // insertion d'entete
        Field[] lf1 = le1[0].getClass().getDeclaredFields();

        for (Field field : lf1) { // satri de meme type le table de donc colonne ray iany donc tsisy le2
            v.add(field.getName());
        }

        Table t = new Table();
        t.setEntete(v.toArray());
        t.setData(array1DimTo2Dim(union));

        return t;
    }

    public int countDoublure(Object[] lo1, Object[] lo2) {
        int count = 0;
        for (Object object : lo1) {
            if (isIn(object, lo2)) { // hita ao am lo2 le objet dans lo1
                count++;
            }
        }
        return count;
    }

    public String repeat(String regex, int repetition) {
        String rep = "";
        for (int i = 0; i < repetition ; i++) {
            rep += regex;
        }
        return rep;
    }

    public void displayAll(Table table) throws Exception {

        Object[] entete = table.getEntete();
        Object[][] data = table.getData();

        int marge = 4;
        String espace = repeat(" ", marge);

        if (data.length > 0) {

            for (Object attribut : entete) {
                int tailleCol = attribut.toString().length();
                System.out.print("|" + attribut.toString() + espace); // entete
            }
            System.out.println();

            int i = 0;
            for (Object[] lo : data) {
                for (Object o : lo) {
                    int nbEspaceTotal = entete[i].toString().length() + marge;
                    int nbEspace = nbEspaceTotal - String.valueOf(o).length(); 
                    String esp = repeat(" ", nbEspace); 
                    int nbEspaceForNull = nbEspaceTotal - "null".length();
                    if (o != null) {
                        System.out.print("|" + String.valueOf(o) + esp);
                    }
                    else {
                        System.out.println("|null"+repeat(" ", nbEspaceForNull));
                    }
                }
                i++;
                System.out.println("");
            }
        }

        else {
            System.out.println();
            System.out.println("No result fetch the request");
            System.out.println();
        }
    }

    public String generateSpaceBetweenCOl(String value,int nbSpace){
        String space = " ";
        for(int i=1;i<nbSpace;i++){
            space = space+" ";
        }
        String result = value+space;
        return result;
    }
    public String intoTableValue(String value){
        int size = 10;
        int valSize = value.length();
        String result = this.generateSpaceBetweenCOl(value,size-valSize);
        result = "|"+result;
        return result;
    }
    
    public void afficheNomCol(Table table){
        Object[] colonnes = table.getEntete();
        for(int i=0;i<colonnes.length;i++){
            System.out.print(this.intoTableValue(colonnes[i].toString()));
        }
        System.out.println();
    }
    public void afficheValue(Table table){
        Object[][] data = table.getData();
        for(int i=0;i<data.length;i++){
            for(int j=0;j<data[i].length;j++){
                System.out.print(this.intoTableValue(data[i][j].toString()));
            }
            System.out.println();
        }
    }

    public void display(Table table) {
        afficheNomCol(table);
        afficheValue(table);
    }

}