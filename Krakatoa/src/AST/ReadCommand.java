/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

import java.util.ArrayList;

/**
 *
 * @author BRUNO
 */
public class ReadCommand extends Statement{

    public ReadCommand() {
    }

    public void addVariable(Variable v){
        variables.add(v);
    }
    
    public void genC(PW pw){

    }

    public void genKrakatoa(PW pw){
        pw.printIdent("read (");
        for(int i = 0; i < variables.size(); i++){
            pw.print(variables.get(i).getName());
            if(i+1<variables.size()) pw.print(", ");
        }
        pw.print(");\n");
    }

    private ArrayList<Variable> variables;
}
