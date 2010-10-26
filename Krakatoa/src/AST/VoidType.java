/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class VoidType extends Type {
    
    public VoidType() {
        super("void");
    }
    
   public String getCname() {
      return "void";
   }

   public void genKrakatoa(){
        System.out.print("void");
   }
}