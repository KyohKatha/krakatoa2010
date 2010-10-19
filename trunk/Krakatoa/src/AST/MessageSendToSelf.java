package AST;


public class MessageSendToSelf extends MessageSend {
    
    public Type getType() { 
        return null;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
    }

    public MessageSendToSelf(Method met, ExprList exprList) {
        this.met = met;
        this.exprList = exprList;
    }

    private Method met;
    private ExprList exprList;
    
}