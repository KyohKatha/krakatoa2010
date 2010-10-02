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

    private Vector paramList;

}
