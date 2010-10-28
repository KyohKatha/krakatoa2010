/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;


public class MessageSendStatic extends MessageSend {

    public Type getType() {
        return null;
    }

    public void genC( PW pw, boolean putParenthesis ) {
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){

    }


    public MessageSendStatic(Method met, ClassDec cl, ExprList exprList) {
        this.met = met;
        this.cl = cl;
        this.exprList = exprList;
    }

    private Method met;
    private ExprList exprList;
    private ClassDec cl;

}