/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;


public class MessageSendStaticVariable extends MessageSend {


    public void genC( PW pw, boolean putParenthesis ) {
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        pw.print(cl.getKrakatoaName() + "." + v.getName());
    }


    public MessageSendStaticVariable(Variable v, ClassDec cl) {
        this.v = v;
        this.cl = cl;
    }

    public Type getType(){

        return v.getType();
    }

    private Variable v;
    private ClassDec cl;

}