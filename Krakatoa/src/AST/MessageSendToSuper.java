package AST;

public class MessageSendToSuper extends MessageSend {

    public MessageSendToSuper(ClassDec classe, Method met, ExprList exprList) {
        this.classe = classe;
        this.met = met;
        this.exprList = exprList;
    }

    public Type getType() { 
        return null;
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
    }

    private ClassDec classe;
    private Method met;
    private ExprList exprList;
}