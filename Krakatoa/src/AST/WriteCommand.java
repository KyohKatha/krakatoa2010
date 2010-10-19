/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author BRUNO
 */
public class WriteCommand extends Statement{

    public WriteCommand(ExprList exprList) {
        this.exprList = exprList;
    }


    public void genC(PW pw){

    }

    private ExprList exprList;
}
