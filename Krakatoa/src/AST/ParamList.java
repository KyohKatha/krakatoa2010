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
       paramList = new ArrayList<Variable>();
    }

    public void addElement(Variable v) {
       paramList.add(v);
    }

    /*public Enumeration elements() {
        return paramList.elements();
    }*/

    public int getSize() {
        return paramList.size();
    }

    public Variable get(int i){
        return paramList.get(i);
    }

    public void genKrakatoa(PW pw){
        //Enumeration e = paramList.elements();
        for(int i = 0; i < paramList.size(); i++){
            pw.print(paramList.get(i).getType().getKrakatoaName() + " ");
            paramList.get(i).genKrakatoa(pw);
            if(i+1 < paramList.size())
                pw.print(", ");
        }
    }

    private ArrayList<Variable> paramList;

}
