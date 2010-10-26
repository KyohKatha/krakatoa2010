/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;


public class MessageSendToSelf extends MessageSend {
    
    public Type getType() { 
        return null;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){

    }


    public MessageSendToSelf(Method met, ExprList exprList) {
        this.met = met;
        this.exprList = exprList;
    }

    private Method met;
    private ExprList exprList;
    
}