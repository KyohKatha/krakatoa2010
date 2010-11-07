/*
 * Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 *
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

    public void genKrakatoa(PW pw){
        pw.printIdent("");
        var.genKrakatoa(pw);
        pw.print(" = ");
        expr.genKrakatoa(pw, false);
        pw.print(";\n");
    }

    private Variable var;
    private Expr expr;
}
