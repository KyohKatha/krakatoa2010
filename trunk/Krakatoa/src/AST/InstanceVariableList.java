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

public class InstanceVariableList {

    public InstanceVariableList() {
       instanceVariableList = new ArrayList<Variable>();
    }

    public void addElement(InstanceVariable instanceVariable) {
       instanceVariableList.add( instanceVariable );
    }

    /*public Enumeration elements() {
        return instanceVariableList.elements();
    }*/

    public int getSize() {
        return instanceVariableList.size();
    }

    public ArrayList<Variable> getInstanceVariableList() {
        return instanceVariableList;
    }

    public void setInstanceVariableList(ArrayList instanceVariableList) {
        this.instanceVariableList = instanceVariableList;
    }


    private ArrayList<Variable> instanceVariableList;

    public void genKrakatoa(){

    }

}
