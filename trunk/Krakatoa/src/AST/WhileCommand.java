/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    private Expr expr;
    private Statement whileStatement;
}
