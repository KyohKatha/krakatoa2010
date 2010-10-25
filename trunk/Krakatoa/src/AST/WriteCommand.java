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
public class WriteCommand extends Statement{

    public WriteCommand(ExprList exprList) {
        this.exprList = exprList;
    }


    public void genC(PW pw){

    }

    private ExprList exprList;
}
