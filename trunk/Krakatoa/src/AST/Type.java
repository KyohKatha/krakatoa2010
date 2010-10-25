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

abstract public class Type {
    
    public Type( String name ) {
        this.name = name;
    }
    
    public static Type booleanType = new BooleanType();
    public static Type intType = new IntType();
    public static Type stringType = new StringType();
    public static Type voidType = new VoidType();
    public static Type undefinedType = new UndefinedType();
    
    public String getName() {
        return name;
    }
    
    abstract public String getCname();
    
    private String name;
}
