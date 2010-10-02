package AST;

public class StringType extends Type {
    
    public StringType() {
        super("String");
    }
    
   public String getCname() {
      return "char *";
   }

}