/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class UndefinedType extends Type {
    // variables that are not declared have this type
    
   public UndefinedType() { super("undefined"); }
   
   public String getCname() {
      return "int";
   }

   public String getKrakatoaName(){
        return "int";
   }
}
