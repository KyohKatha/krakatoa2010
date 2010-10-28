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

public class UnaryExpr extends Expr {
    
    public UnaryExpr( Expr expr, int op ) {
        this.expr = expr;
        this.op = op;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        switch ( op ) {
            case Symbol.PLUS : 
              pw.print("+");
              break;
            case Symbol.MINUS :
              pw.print("-");
              break;
            case Symbol.NOT :
              pw.print("!");
              break;
        }
        expr.genC(pw, false);
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        switch ( op ) {
            case Symbol.PLUS :
              pw.print("+");
              break;
            case Symbol.MINUS :
              pw.print("-");
              break;
            case Symbol.NOT :
              pw.print("!");
              break;
        }
        expr.genKrakatoa(pw, false);
    }

    public Type getType() {
        return expr.getType();
    }
    
    private Expr expr;
    private int op;
}
              