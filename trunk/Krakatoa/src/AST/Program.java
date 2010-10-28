/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

import java.util.*;

public class Program {
   public Program( ArrayList classList ) {
      this.classList = classList;
   }
   

    public void genC(PW pw) {
    }

    public void genKrakatoa(PW pw){
        for(int i = 0; i < classList.size(); i++){
            classList.get(i).genKrakatoa();
        }
    }

    public ArrayList<ClassDec> getClassList() {
        return classList;
    }

    public void setClassList(ArrayList<ClassDec> classList) {
        this.classList = classList;
    }
   
   private ArrayList<ClassDec> classList;
}