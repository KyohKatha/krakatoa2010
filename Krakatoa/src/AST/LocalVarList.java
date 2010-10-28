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

    public void genKrakatoa(PW pw){
        int size = localList.size();
        Enumeration e = localList.elements();
        Variable v = ((Variable)e.nextElement());
        pw.printIdent(v.getType().getKrakatoaName() + " ");
        v.genKrakatoa(pw);
        size--;
        while ( e.hasMoreElements() ) {
            if ( --size > 0 )
              pw.print(", ");
            ((Expr ) e.nextElement()).genKrakatoa(pw, false);
        }
        pw.print(";\n");
    }

    private Vector localList;

}
