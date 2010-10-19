package AST;

public class MessageSendStatement extends Statement { 


   public void genC( PW pw ) {
      pw.printIdent("");
      // messageSend.genC(pw);
      pw.println(";");
   }

    public MessageSendStatement(MessageSend messageSend) {
        this.messageSend = messageSend;
    }

   private MessageSend  messageSend;

}


