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
        this.isThis = false;
        this.classRef = null;
    }

    public AssignCommand(Variable var, Expr expr, ClassDec cl) {
        this.var = var;
        this.expr = expr;
        this.isThis = false;
        this.classRef = cl;
    }

    public AssignCommand(Variable var, Expr expr, boolean isThis) {
        this.var = var;
        this.expr = expr;
        this.isThis = true;
    }

    public void genC(PW pw){

    }

    public void genKrakatoa(PW pw){
        pw.printIdent("");
        if(isThis)
            pw.print("this.");
        else if(classRef!=null)
            pw.print(classRef.getKrakatoaName()+".");
        var.genKrakatoa(pw);
        pw.print(" = ");
        expr.genKrakatoa(pw, false);
        pw.print(";\n");
    }

    private Variable var;
    boolean isThis;
    private Expr expr;
    ClassDec classRef;
}
