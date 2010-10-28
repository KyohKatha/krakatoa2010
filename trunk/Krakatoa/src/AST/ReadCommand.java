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
        eita... como fas?
    }

    private ArrayList<Variable> variables;
}
