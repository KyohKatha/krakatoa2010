/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class MessageSendStatement extends Statement { 


   public void genC( PW pw ) {
      pw.printIdent("");
      messageSend.genC(pw,true);
      pw.println(";");
   }

   public void genKrakatoa(PW pw){
      pw.printIdent("");
      if(messageSend != null)
          messageSend.genKrakatoa(pw,true);
      pw.println(";\n");
   }

    public MessageSendStatement(MessageSend messageSend) {
        this.messageSend = messageSend;
    }

   private MessageSend  messageSend;

}


