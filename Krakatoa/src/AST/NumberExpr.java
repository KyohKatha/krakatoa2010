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
    
    public Type getType() {
        return Type.intType;
    }
    
    private int value;
}
