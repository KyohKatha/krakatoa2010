/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package Comp;

import java.util.*;
import AST.*;

public class SymbolTable {
    
    public SymbolTable() {
        globalTable = new Hashtable();
        localTable  = new Hashtable();
    }
    
    //# corrija: value deve ter o tipo usado para representar classes
    // o mesmo se aplica a getInGlobal
    public Object putInGlobal( String key, ClassDec value ) {
       return globalTable.put(key, value);
    }

    public ClassDec getInGlobal( String key ) {
       return (ClassDec ) globalTable.get(key);
    }
    
    public Variable putInLocal( String key, Variable value ) {
       return (Variable ) localTable.put(key, value);
    }
    
    public Variable getInLocal( String key ) {
       return (Variable ) localTable.get(key);
    }

    public ClassDec getLastClassInsertedOnTheTable(){
        return (ClassDec) globalTable.get(globalTable.size());
    }

    public Object get( String key ) {
        // returns the object corresponding to the key. 
        Object result;
        if ( (result = localTable.get(key)) != null ) {
              // found local identifier
            return result;
        }
        else {
              // global identifier, if it is in globalTable
            return globalTable.get(key);
        }
    }

    public void removeLocalIdent() {
           // remove all local identifiers from the table
         localTable.clear();
    }
      
        
    private Hashtable globalTable, localTable;
}
            