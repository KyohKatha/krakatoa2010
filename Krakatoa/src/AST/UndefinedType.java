package AST;

public class UndefinedType extends Type {
    // variables that are not declared have this type
    
   public UndefinedType() { super("undefined"); }
   
   public String getCname() {
      return "int";
   }
   
}
