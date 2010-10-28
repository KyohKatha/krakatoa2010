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
public class CompositeCommand extends Statement{

    public CompositeCommand(ArrayList<Statement> commands) {
        this.commands = commands;
    }


    public void genC(PW pw){

    }

    public void genKrakatoa(PW pw){
        int i;
        for (i=0; i<commands.size(); i++){
            commands.get(i).genKrakatoa(pw);
        }
    }

    ArrayList<Statement> commands;
}
