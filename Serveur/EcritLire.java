package inputOutput;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Scanner;
import java.util.Vector;
import objets.*;

import inc.Fonction;

public class EcritLire {

    // static String location = "Home";
    // static String descRepertoire = "E:/DOSSIERS/ITU/JAVA/S3/SOCKET/SGBD/Serveur//descr/descr.";
    // static String tableRepertoire = "E:/DOSSIERS/ITU/JAVA/S3/SOCKET/SGBD/Serveur//database/table.";
    static String location = "ITU";
    static String descRepertoire = "C:/Users/ITU/Documents/GitHub/SGBD/Serveur/descr/descr.";
    static String tableRepertoire = "C:/Users/ITU/Documents/GitHub/SGBD/Serveur/database/table.";
    static String tableDir = "C:/Users/ITU/Documents/GitHub/SGBD/Serveur/database";

    public static String [] listTables() throws Exception {
        File f = new File(tableDir);
        File [] lf = f.listFiles();
        String [] listNameFile = new String[lf.length];
        
        int i = 0;
        for (File file : lf) {
            String fileName = file.getName().split("\\.")[1]; //table.[nomtable]
            listNameFile[i] = fileName;
            i++;
        }
        
        return listNameFile;
    }

    public void vider(String nomTable) throws Exception {
        File file = new File("database/table." + nomTable);
        file.delete();
        file.createNewFile();
    }

    public void ecraser(String nomTable, Table table) throws Exception {
        File file = new File("database/table." + nomTable);
        file.delete();
        Object[][] data = table.getData();
        for (Object[] o : data) {
            insert(o, nomTable);
        } 
    }

    public void insert(Object [] o, String nomTable) throws Exception {
        File file = new File("database/table." + nomTable);
        if(file.exists() == false) file.createNewFile();
        FileWriter write = new FileWriter((file), true);
        BufferedWriter buff = new BufferedWriter(write);

        for (Object f : o) {
            String w = f.toString() + "::";
            if (w.equals("") || w.equals(" ")) {
                w = "0";
            }
            buff.write(w);
        }
        buff.newLine();
        buff.write(";;");
        buff.newLine();

        buff.close();
        System.out.println("insertion du nouveau element dans " + nomTable + " a reussie");
    }

    public void insert(String nomTable, String [] values) throws Exception {
        File file = new File("database");
        file.mkdir();
        FileWriter write = new FileWriter(new File("database/table." + nomTable), true);
        BufferedWriter buff = new BufferedWriter(write);

        for (String f : values) {
            String w = f + "::";
            if (w.equals("") || w.equals(" ")) {
                w = "0";
            }
            buff.write(w);
        }
        buff.newLine();
        buff.write(";;");
        buff.newLine();

        buff.close();
        System.out.println("insertion du nouveau element dans " + nomTable + " a reussie");
    }

    public void EcritDescr(String nomTable, String[][] colType) throws Exception {
        try {
            if (isInDatabase(nomTable) == true)
                throw new Exception(nomTable + " existe deja dans la base de donnees");
        } catch (Exception e) {
            File file = new File("descr");
            file.mkdir();
            FileWriter write = new FileWriter(new File("descr/descr." + nomTable), false);
            BufferedWriter buff = new BufferedWriter(write);

            for (String[] colonnes : colType) {
                String w = colonnes[0] + "::" + colonnes[1];
                buff.write(w);
                buff.newLine();
                buff.write(";;");
                buff.newLine();
            }
            buff.close();
        }
    }

    public void insert(Object o, String nomTable) throws Exception {
        File file = new File("database");
        file.mkdir();
        FileWriter write = new FileWriter(new File("database/table." + nomTable), true);
        BufferedWriter buff = new BufferedWriter(write);

        Field[] lf = o.getClass().getDeclaredFields();
        for (Field f : lf) {
            f.setAccessible(true);
            String w = f.get(o).toString() + "::";
            if (w.equals("") || w.equals(" ")) {
                w = "0";
            }
            buff.write(w);
        }
        buff.newLine();
        buff.write(";;");
        buff.newLine();

        buff.close();
        System.out.println("insertion du nouveau element dans " + nomTable + " a reussie");
    }

    public void create(Object[] lo, String nomTable) throws Exception {
        EcritDescr(lo, nomTable);
        System.out.println("Creation description " + nomTable + " reussie");
        ecritData(lo, nomTable);
        System.out.println("Creation table " + nomTable + " reussie");
    }

    public Object[] getTypesTableInFile(String nomTable) throws Exception {
        // String location = "Home";
        // String repertoire = "E:/JAVA/S3/BD Relationnelle multiCondition
        // multiTable/descr/descr." + nomTable;
        String repertoire = descRepertoire + nomTable;
        File f = new File(repertoire);
        Vector enteteV = new Vector();
        if (f.exists()) {
            Scanner scan = new Scanner(f);
            Object[] colonne = new Object[2];
            while (scan.hasNext() == true) {
                while (scan.hasNext(";;") == false) {
                    String bloc = scan.next();
                    String[] data = bloc.split("::");
                    colonne[0] = data[0]; // indice 0 nom ; 1 type
                    colonne[1] = data[1];
                    enteteV.add(colonne[1]);
                }
                colonne = new Object[2];
                scan.next();
            }
        } else {
            throw new Exception("Desc Repertoire not found : " + repertoire + " ( " + location + " )");
        }

        return enteteV.toArray();
    }

    public boolean isColonneExist(String colonne, String nomTable) throws Exception {
        Object[] colonnes = getEnteteTableInFile(nomTable);
        for (Object colonneObj : colonnes) {
            if (colonne.equalsIgnoreCase(colonneObj.toString()) == true) {
                return true;
            }
        }
        throw new Exception("Colonne : " + colonne + " n'existe pas dans la table " + nomTable);
    }

    public boolean isInDatabase(String nomTable) throws Exception {
        // String repertoire = "database/table." + nomTable;
        // String location = "Home";
        // String repertoire = "E:/JAVA/S3/BD Relationnelle multiCondition
        // multiTable/database/table." + nomTable;
        String repertoire = descRepertoire + nomTable;
        File f = new File(repertoire);
        if (f.exists() == true)
            return true;
        throw new Exception("Data Repertoire not found : " + repertoire + " ( " + location + " )");
        // return false;
    }

    public Object[] getEnteteTableInFile(String nomTable) throws Exception {
        // String location = "Home";
        // String repertoire = "E:/JAVA/S3/BD Relationnelle multiCondition
        // multiTable/descr/descr." + nomTable;
        String repertoire = descRepertoire + nomTable;
        File f = new File(repertoire);
        Vector enteteV = new Vector();
        if (f.exists()) {
            Scanner scan = new Scanner(f);
            Object[] colonne = new Object[2];
            while (scan.hasNext() == true) {
                while (scan.hasNext(";;") == false) {
                    String bloc = scan.next();
                    String[] data = bloc.split("::");
                    colonne[0] = data[0]; // indice 0 nom ; 1 type
                    colonne[1] = data[1];
                    enteteV.add(colonne[0]);
                }
                colonne = new Object[2];
                scan.next();
            }
        } else {
            throw new Exception("Desc Repertoire not found : " + repertoire + " ( " + location + " )");
        }

        return enteteV.toArray();
    }

    public void EcritDescr(Object[] lo, String nomTable) throws Exception {
        File file = new File("descr");
        file.mkdir();
        FileWriter write = new FileWriter(new File("descr/descr." + nomTable), true);
        BufferedWriter buff = new BufferedWriter(write);

        Field[] lf = lo[0].getClass().getDeclaredFields();
        for (Field f : lf) {
            f.setAccessible(true);
            String w = f.getName() + "::" + f.getType().getSimpleName();
            buff.write(w);
            buff.newLine();
            buff.write(";;");
            buff.newLine();
        }

        buff.close();
    }

    public void ecritData(Object[] lo, String nomTable) throws Exception {
        File file = new File("database");
        file.mkdir();
        FileWriter write = new FileWriter(new File("database/table." + nomTable), true);
        BufferedWriter buff = new BufferedWriter(write);

        int i = 0;
        for (Object o : lo) {
            Field[] lf = o.getClass().getDeclaredFields();
            for (Field f : lf) {
                f.setAccessible(true);
                String w = f.get(o).toString() + "::";
                if (w.equals("") || w.equals(" ")) {
                    w = "0";
                }
                buff.write(w);
            }
            buff.newLine();
            buff.write(";;");
            buff.newLine();
        }
        buff.close();
    }

    public Object[][] lire(String nomTable) throws Exception {
        Fonction f = new Fonction();
        Vector data = new Vector();
        Vector bigData = new Vector();
        String location = "Home";
        // String repertoire = "E:/JAVA/S3/BD Relationnelle multiCondition
        // multiTable/database/table." + nomTable;
        String repertoire = tableRepertoire + nomTable;
        Scanner scan = new Scanner(new File(repertoire));
        while (scan.hasNext() == true) {
            while (scan.hasNext(";;") == false) {
                String bloc = scan.next();
                String[] d = bloc.split("::");
                for (String string : d) {
                    // System.out.print(" hee "+ string);
                    data.add(string);
                }
            }
            // System.out.println();
            bigData.add(data.toArray());
            // System.out.println("length "+data.toArray().length);
            data = new Vector();
            scan.next();
        }
        Object[][] bigDataVeryBig = f.vectorToObj2Dim(bigData);
        // System.out.println("phase vector -> object[][] ended ");
        scan.close();
        return bigDataVeryBig;
    }

    public int countData(Object o) throws Exception {
        int n = 0;
        Scanner scan = new Scanner(new File("save." + o.getClass().getSimpleName()));
        while (scan.hasNext() == true) {
            while (scan.hasNext(";;") == false) {
                scan.next();
            }
            n++;
            scan.next();
        }
        scan.close();

        return n;
    }

    public int countAttr(Object o) throws Exception {
        int n = 0;
        Scanner scan = new Scanner(new File("save." + o.getClass().getSimpleName()));
        while (scan.hasNext(";;") == false) {
            n++;
            scan.next();
        }
        scan.close();

        return n;
    }

    public boolean isSaveExist(Object o) throws Exception {
        File f = new File("save." + o.getClass().getSimpleName());
        if (f.exists())
            return true;
        return false;
    }
}