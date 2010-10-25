/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

import java.util.*;

public class LocalVarList extends Statement{

    public LocalVarList() {
       localList = new Vector();
    }

    public void addElement(Variable v) {
       localList.addElement(v);
    }

    public Enumeration elements() {
        return localList.elements();
    }

    public int getSize() {
        return localList.size();
    }

    public void genC(PW pw){

    }

    private Vector localList;

}
