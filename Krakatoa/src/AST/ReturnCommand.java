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
public class ReturnCommand extends Statement {

    public ReturnCommand(Expr expr) {
        this.expr = expr;
    }


    public void genC(PW pw){

    }

    public void genKrakatoa(PW pw){
        pw.printIdent("return ");
        expr.genKrakatoa(pw, true);
        pw.print(";\n");
    }

    private Expr expr;

}
