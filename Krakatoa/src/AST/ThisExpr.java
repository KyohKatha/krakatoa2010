/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    private ClassDec currentClass;
}
