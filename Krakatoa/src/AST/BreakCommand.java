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
public class BreakCommand extends Statement{

    public BreakCommand() {
    }
    
    public void genC(PW pw){
        
    }

    public void genKrakatoa(PW pw){
        pw.print("break;\n");
    }
}
