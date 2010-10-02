package AST;

import java.util.*;

public class LocalVarList {

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

    private Vector localList;

}
