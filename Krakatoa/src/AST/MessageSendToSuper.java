/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class MessageSendToSuper extends MessageSend {

    public MessageSendToSuper(ClassDec classe, Method met, ExprList exprList) {
        this.classe = classe;
        this.met = met;
        this.exprList = exprList;
    }

    public Type getType() { 
        return met.getType();
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        //pw.print("super.");
        pw.print(met.getIdent() + "(");
        if(exprList != null)
            exprList.genKrakatoa(pw);
        pw.print(")");
    }

    private ClassDec classe;
    private Method met;
    private ExprList exprList;
}