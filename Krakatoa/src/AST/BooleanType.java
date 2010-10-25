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
  
public class BooleanType extends Type {
    
   public BooleanType() { super("bool"); }
   
   public String getCname() {
      return "int";
   }
   
}
