/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class StringType extends Type {
    
    public StringType() {
        super("String");
    }
    
   public String getCname() {
      return "char *";
   }

}