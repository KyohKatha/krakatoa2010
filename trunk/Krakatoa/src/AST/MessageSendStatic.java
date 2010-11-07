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


    public void genC( PW pw, boolean putParenthesis ) {
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        pw.print(cl.getKrakatoaName() + "." + met.getIdent() + "(");
        if(exprList != null)
            exprList.genKrakatoa(pw);
        pw.print(")");
    }


    public MessageSendStatic(Method met, ClassDec cl, ExprList exprList) {
        this.met = met;
        this.cl = cl;
        this.exprList = exprList;
    }

    public Type getType(){
        System.out.println(met);
        return met.getType();
    }

    private Method met;
    private ExprList exprList;
    private ClassDec cl;

}