/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author Katha
 */
public class CreateObject extends Expr{

    public CreateObject(ClassDec classe) {
        this.classe = classe;
    }

    public ClassDec getClasse() {
        return classe;
    }

    public void setClasse(ClassDec classe) {
        this.classe = classe;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void genKrakatoa(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Type getType() {
        return classe;
    }



    private ClassDec classe;
}
