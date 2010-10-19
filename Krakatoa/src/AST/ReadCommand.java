/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    private ArrayList<Variable> variables;
}
