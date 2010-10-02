package AST;

import java.io.*;
  
public class BooleanType extends Type {
    
   public BooleanType() { super("bool"); }
   
   public String getCname() {
      return "int";
   }
   
}
