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

    private Expr expr;

}
