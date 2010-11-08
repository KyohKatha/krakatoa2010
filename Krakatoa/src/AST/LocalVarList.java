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
       localList = new ArrayList();
    }

    public void addElement(Variable v) {
       localList.add(v);
    }

    public int getSize() {
        return localList.size();
    }

    public void genC(PW pw){

    }

    public void genKrakatoa(PW pw){
        pw.printIdent(localList.get(0).getType().getKrakatoaName() + " ");
        localList.get(0).genKrakatoa(pw);

        for(int i=1; i < localList.size();i++){
            pw.print(", ");
            localList.get(i).genKrakatoa(pw);
        }
        pw.print(";\n");
    }

    private ArrayList<Variable> localList;

}
