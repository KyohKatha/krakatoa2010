package AST;

import java.io.*;
import java.util.*;

public class ExprList {
    
    public ExprList() {
        v = new Vector();
    }
    
    public void addElement( Expr expr ) {
        v.addElement(expr);
    }
    
    public void genC( PW pw ) {
        
        int size = v.size();
        Enumeration e = v.elements();
        while ( e.hasMoreElements() ) {
            ((Expr ) e.nextElement()).genC(pw, false);
            if ( --size > 0 ) 
              pw.print(", ");
        }
    }
    
    private Vector v;
    
}
            