package objets;

import java.util.Vector;

public class Employe {
    int numDept;
    int idEmp;
    String nomEmp;
    double salaire;
    int manager;

    public Employe() {
    }

    public Employe(int n, String nm, double s, int nd, int m) {
        idEmp = n;
        nomEmp = nm;
        salaire = s;
        numDept = nd;
        manager = m;
    }

    public int getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(int idEmp) {
        this.idEmp = idEmp;
    }

    public String getnomEmp() {
        return nomEmp;
    }

    public void setnomEmp(String nomEmp) {
        this.nomEmp = nomEmp;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public int getNumDept() {
        return numDept;
    }

    public void setNumDept(int numDept) {
        this.numDept = numDept;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public boolean isManager(Employe[] le) {
        for (Employe employe : le) {
            if (employe.getManager() == this.idEmp) {
                return true;
            }
        }
        return false;
    }

    public Employe[] vectorToEmp(Vector v) {

        Employe[] le = new Employe[v.size()];
        Object[] lo = v.toArray();
        int i = 0;
        for (Object object : lo) {
            le[i] = (Employe) object;
            i++;
        }
        return le;
    }
}