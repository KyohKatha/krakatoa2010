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
public class IfCommand extends Statement {

    public IfCommand(Expr expr, Statement ifStatement, Statement elseStatement) {
        this.expr = expr;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    public void genC(PW pw){

    }
    
    private Expr expr;
    private Statement ifStatement, elseStatement;
}
