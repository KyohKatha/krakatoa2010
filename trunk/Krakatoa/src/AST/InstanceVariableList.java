package AST;

import java.util.*;

public class InstanceVariableList {

    public InstanceVariableList() {
       instanceVariableList = new ArrayList();
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

    private ArrayList instanceVariableList;

}
