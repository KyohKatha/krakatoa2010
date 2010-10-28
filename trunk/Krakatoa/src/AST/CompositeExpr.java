/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

import Lexer.*;
import java.io.*;

public class CompositeExpr extends Expr {
    
    public CompositeExpr( Expr pleft, int poper, Expr pright ) {
        left = pleft;
        oper = poper;
        right = pright;
    }
    public void genC( PW pw, boolean putParenthesis ) {
        if ( putParenthesis )
          pw.print("(");
        left.genC(pw, true);
        pw.print(" " + arrayOper[oper] + " ");
        right.genC(pw, true);
        if ( putParenthesis )
          pw.print(")");
    }
    
    public Type getType() {
          // left and right must be the same type
       if ( oper == Symbol.EQ || oper == Symbol.NEQ || oper == Symbol.LE || oper == Symbol.LT ||
            oper == Symbol.GE || oper == Symbol.GT )
            return Type.booleanType;
       else if ( oper == Symbol.AND || oper == Symbol.OR ) 
            return Type.booleanType;
       else
            return Type.intType;
    }

    public void genKrakatoa(PW pw, boolean putParenthesis){
        left.genKrakatoa(pw, putParenthesis);
        pw.print(arrayOper[oper]);
        right.genKrakatoa(pw, putParenthesis);
    }

    private Expr left, right;
    private int oper;
    private static String []arrayOper;
    static { 
        arrayOper = new String[Symbol.LastSymbol];
        int i;
        for( i = 0; i < arrayOper.length; i++ )
          arrayOper[i] = "";
        arrayOper[Symbol.PLUS] = "+";
        arrayOper[Symbol.MINUS] = "-";
        arrayOper[Symbol.MULT] = "*";
        arrayOper[Symbol.DIV] = "/";
        arrayOper[Symbol.LT] = "<";
        arrayOper[Symbol.LE] = "<=";
        arrayOper[Symbol.GT] = ">";
        arrayOper[Symbol.GE] = ">=";
        arrayOper[Symbol.NEQ] = "!=";
        arrayOper[Symbol.EQ] = "==";
        arrayOper[Symbol.ASSIGN] = "=";
        arrayOper[Symbol.AND] = "&&";
        arrayOper[Symbol.OR] = "||";
    }
}
