/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author BRUNO
 */
public class AssignCommand extends Statement{

    public AssignCommand(Variable var, Expr expr) {
        this.var = var;
        this.expr = expr;
    }

    public void genC(PW pw){

    }

    private Variable var;
    private Expr expr;
}
