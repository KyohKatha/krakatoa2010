/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

import java.io.*;

public class Variable {
    
    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() { return name; }
    
    public Type getType() {
        return type;
    }

    public void genKrakatoa(PW pw){

    }
    
    private String name;
    private Type type;
}