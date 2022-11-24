package inc;

import java.util.Vector;
import objets.*;

import java.lang.reflect.*;

public class Fonction {
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