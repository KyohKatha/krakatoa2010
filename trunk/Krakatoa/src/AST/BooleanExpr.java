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

public class BooleanExpr extends Expr {
    
    public BooleanExpr( boolean value ) {
        this.value = value;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
       pw.print( value ? "1" : "0" );
    }
    
    public Type getType() {
        return Type.booleanType;
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        pw.print( value ? " true " : " false " );
    }
    
    public static BooleanExpr True  = new BooleanExpr(true);
    public static BooleanExpr False = new BooleanExpr(false);
    
    private boolean value;
}
