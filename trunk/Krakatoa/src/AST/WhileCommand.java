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
public class WhileCommand extends Statement{

    public WhileCommand(Expr expr, Statement whileStatement) {
        this.expr = expr;
        this.whileStatement = whileStatement;
    }


    public void genC(PW pw){

    }

    public void genKrakatoa(PW pw){

    }

    private Expr expr;
    private Statement whileStatement;
}
