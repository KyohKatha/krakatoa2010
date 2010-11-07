/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;


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
        pw.print(" new ");
        pw.print(classe.getKrakatoaName() + "()");
    }

    @Override
    public Type getType() {
        return classe;
    }



    private ClassDec classe;
}
