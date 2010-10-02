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
    
    public Type getType() {
        return expr.getType();
    }
    
    private Expr expr;
    private int op;
}
              