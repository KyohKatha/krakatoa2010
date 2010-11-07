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
        return met.getType();
    }

    public void genC(PW pw, boolean putParenthesis) {

    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        if(v==null)
            System.out.print("\nMessageSendToVariable com variavel NULL\n");
        else
            pw.print(v.getName() + ".");
        pw.print(met.getIdent() + "(");
        if(exprList != null)
            exprList.genKrakatoa(pw);
        pw.print(")");
    }

    private ExprList exprList;
    private Variable v;
    private Method met;
}
