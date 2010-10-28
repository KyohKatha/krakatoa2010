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

    public void genKrakatoa(PW pw){
        pw.printIdent("if( ");
        expr.genKrakatoa(pw, true);
        pw.print(" ){\n");
        pw.add();
        ifStatement.genKrakatoa(pw);
        pw.sub();
        pw.printIdent("}\n");
        if(elseStatement != null){
            pw.printIdent("else {\n");
            pw.add();
            elseStatement.genKrakatoa(pw);
            pw.sub();
            pw.printIdent("}\n");
        }
    }

    private Expr expr;
    private Statement ifStatement, elseStatement;
}
