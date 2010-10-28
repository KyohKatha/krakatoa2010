/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */


package AST;

import java.io.*;
import java.util.*;

public class ExprList {
    
    public ExprList() {
        v = new ArrayList<Expr>();
    }
    
    public void addElement( Expr expr ) {
        v.add(expr);
    }
    
    public void genC( PW pw ) {
        
        int size = v.size();
        //Enumeration e = v.();
        int i = 0;
        while ( i < v.size() ) {
            ((Expr ) v.get(i)).genC(pw, false);
            i++;
            if ( --size > 0 ) 
              pw.print(", ");
        }
    }

    public void genKrakatoa(PW pw){
        int size = v.size();
        for(int i = 0; i < v.size(); i++){
            v.get(i).genKrakatoa(pw, false);
            if ( --size > 0 )
              pw.print(", ");
        }
    }

    public ArrayList<Expr> getV() {
        return v;
    }

    public void setV(ArrayList v) {
        this.v = v;
    }

    private ArrayList<Expr> v;
    
}
            