/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */
package AST;

import Lexer.Symbol;
import java.util.*;

public class InstanceVariableList {

    public InstanceVariableList() {
        instanceVariableList = new ArrayList<InstanceVariable>();
    }

    public void addElement(InstanceVariable instanceVariable) {
        instanceVariableList.add(instanceVariable);
    }

    /*public Enumeration elements() {
    return instanceVariableList.elements();
    }*/
    public int getSize() {
        return instanceVariableList.size();
    }

    public ArrayList<InstanceVariable> getInstanceVariableList() {
        return instanceVariableList;
    }

    public void setInstanceVariableList(ArrayList instanceVariableList) {
        this.instanceVariableList = instanceVariableList;
    }

    public void genKrakatoa(PW pw) {
        Type tp = null;
        //pw.printIdent(tp.getKrakatoaName() + " ");

        for (int i = 0; i < instanceVariableList.size(); i++) {
            switch (instanceVariableList.get(i).qualifier) {
                case Symbol.PRIVATE:
                    pw.printIdent("private ");
                    break;
                case Symbol.PUBLIC:
                    pw.printIdent("public ");
                    break;
                case Symbol.STATICPRIVATE:
                    pw.printIdent("static private ");
                    break;
                case Symbol.STATICPUBLIC:
                    pw.printIdent("static public ");
                    break;
            }
            tp = instanceVariableList.get(i).getType();
            pw.print(tp.getKrakatoaName() + " ");
            instanceVariableList.get(i).genKrakatoa(pw);
            pw.print(";\n");
        }
    }
    private ArrayList<InstanceVariable> instanceVariableList;
}
