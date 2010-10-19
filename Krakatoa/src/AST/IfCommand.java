/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
