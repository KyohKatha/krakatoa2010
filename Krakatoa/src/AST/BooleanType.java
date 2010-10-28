/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

  
public class BooleanType extends Type {
    
   public BooleanType() { super("bool"); }
   
   public String getCname() {
      return "bool";
   }

   public String getKrakatoaName(){
       return "boolean";
   }
}
