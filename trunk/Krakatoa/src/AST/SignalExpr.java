/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

import Lexer.*;

public class SignalExpr extends Expr {
    
    public SignalExpr( int oper, Expr expr ) {
       this.oper = oper;
       this.expr = expr;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
       if ( putParenthesis )
          pw.print("(");
       pw.print( oper == Symbol.PLUS ? " + " : " - " );
       expr.genC(pw, true);
       if ( putParenthesis )
          pw.print(")");
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
       if ( putParenthesis )
          pw.print("(");
       pw.print( oper == Symbol.PLUS ? " + " : " - " );
       expr.genKrakatoa(pw, true);
       if ( putParenthesis )
          pw.print(")");
    }

    public Type getType() {
       return expr.getType();
    }
    
    private Expr expr;
    private int oper;
}
