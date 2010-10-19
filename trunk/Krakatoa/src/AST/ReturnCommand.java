/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
