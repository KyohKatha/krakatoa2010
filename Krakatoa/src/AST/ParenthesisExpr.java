/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class ParenthesisExpr extends Expr {
    
    public ParenthesisExpr( Expr expr ) {
        this.expr = expr;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print("(");
        expr.genC(pw, false);
        pw.printIdent(")");
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){

    }

    public Type getType() {
        return expr.getType();
    }
    
    private Expr expr;
}