/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class NumberExpr extends Expr {
    
    public NumberExpr( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genC( PW pw, boolean putParenthesis ) {
        pw.printIdent(value + "");
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){

    }

    public Type getType() {
        return Type.intType;
    }
    
    private int value;
}
