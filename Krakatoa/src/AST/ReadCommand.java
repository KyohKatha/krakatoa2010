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
import java.util.Vector;

/**
 *
 * @author BRUNO
 */
public class ReadCommand extends Statement{

    public ReadCommand() {
        variables = new ArrayList<Variable>();
        thiss = new boolean[100];
    }

    public void setThis(int in){
        this.thiss[in] = true;
    }

    public void addVariable(Variable v){
        variables.add(v);
    }
    
    public void genC(PW pw){

    }

    public void genKrakatoa(PW pw){
        pw.printIdent("read(");
        for(int i = 0; i < variables.size(); i++){
            if(thiss[i]) pw.print("this.");
            pw.print(variables.get(i).getName());
            if(i+1<variables.size()) pw.print(", ");
        }
        pw.print(");\n");
    }

    private ArrayList<Variable> variables;
    boolean[] thiss;
}
