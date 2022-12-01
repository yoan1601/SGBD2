package inc;

import java.util.Vector;
import objets.*;

import java.lang.reflect.*;

public class Fonction {

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

    public static String[] decompose(String str) {
        String [] strSplit = str.split(" ");
        String [] strTrim = trimAll(strSplit);
        return strTrim;
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
    
    public void displayAll(Table table) throws Exception {

        Object[] entete = table.getEntete();
        Object[][] data = table.getData();

        if (data.length > 0) {

            for (Object attribut : entete) {
                System.out.print("|    " + attribut.toString() + "    |   "); // entete
            }
            System.out.println();

            for (Object[] lo : data) {
                for (Object o : lo) {
                    if (o != null)
                        System.out.print("   |   " + String.valueOf(o) + "   |   ");
                    else {
                        System.out.println("null");
                    }
                }
                System.out.println("");
            }
        }

        else {
            System.out.println();
            System.out.println("No result fetch the request");
            System.out.println();
        }
    }
}