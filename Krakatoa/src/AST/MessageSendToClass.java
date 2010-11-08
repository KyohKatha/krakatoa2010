/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class MessageSendToClass extends MessageSend {

    public MessageSendToClass(ExprList exprList, ClassDec c, Method met) {
        this.exprList = exprList;
        this.c = c;
        this.met = met;
    }

    public Type getType() {
        return met.getType();
    }

    public void genC(PW pw, boolean putParenthesis) {

    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        if(c==null)
            System.out.print("\nMessageSendToClass com variavel NULL\n");
        else{
            //if(v.getThiss())pw.print("this.");
            pw.print(c.getName() + ".");
        }
        pw.print(met.getIdent() + "(");
        if(exprList != null)
            exprList.genKrakatoa(pw);
        pw.print(")");
    }

    private ExprList exprList;
    private ClassDec c;
    private Method met;
}
