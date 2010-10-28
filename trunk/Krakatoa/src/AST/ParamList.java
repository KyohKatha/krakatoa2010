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

public class ParamList {

    public ParamList() {
       paramList = new Vector();
    }

    public void addElement(Variable v) {
       paramList.addElement(v);
    }

    public Enumeration elements() {
        return paramList.elements();
    }

    public int getSize() {
        return paramList.size();
    }

    public void genKrakatoa(PW pw){
        Enumeration e = paramList.elements();
        for(int i = 0; i < paramList.size(); i++){
            (( Variable )e.nextElement()).genKrakatoa(pw);
            if(i+1 < paramList.size())
                pw.print(", ");
        }
    }

    private Vector paramList;

}
