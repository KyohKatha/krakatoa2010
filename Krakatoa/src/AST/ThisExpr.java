/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

/**
 *
 * @author BRUNO
 */
public class ThisExpr extends Expr{

    public ThisExpr(ClassDec currentClass) {
        this.currentClass = currentClass;
    }

    public void genC(PW pw, boolean putParenthesis) {

    }

    public Type getType(){
        return null;
    }

    public ClassDec getCurrentClass() {
        return currentClass;
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        pw.print("this");
   }

    private ClassDec currentClass;
}
