package AST;

public class IntType extends Type {
    
    public IntType() {
        super("int");
    }
    
   public String getCname() {
      return "int";
   }

}