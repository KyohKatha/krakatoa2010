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
public class CompositeCommand extends Statement{

    public CompositeCommand(ArrayList<Statement> commands) {
        this.commands = commands;
    }


    public void genC(PW pw){

    }

    ArrayList<Statement> commands;
}
