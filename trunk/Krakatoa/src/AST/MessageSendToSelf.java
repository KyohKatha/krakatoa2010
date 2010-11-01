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
        return met.getType();
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        pw.printIdent("this.");
        pw.print(met.getIdent() + "(");
        exprList.genKrakatoa(pw);
        pw.print(");\n");
    }


    public MessageSendToSelf(Method met, ExprList exprList) {
        this.met = met;
        this.exprList = exprList;
    }

    private Method met;
    private ExprList exprList;
    
}