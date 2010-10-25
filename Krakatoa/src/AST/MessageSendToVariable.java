/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class MessageSendToVariable extends MessageSend {

    public MessageSendToVariable(ExprList exprList, Variable v, Method met) {
        this.exprList = exprList;
        this.v = v;
        this.met = met;
    }

    public Type getType() {
        return null;
    }

    public void genC(PW pw, boolean putParenthesis) {

    }
    
    private ExprList exprList;
    private Variable v;
    private Method met;
}
