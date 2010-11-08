/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */
package AST;

import Lexer.Symbol;

public class InstanceVariable extends Variable {

    public InstanceVariable(int q, String name, Type type) {
        super(name, type);
        this.qualifier = q;
    }

    @Override
    public void genKrakatoa(PW pw) {
        pw.print(super.getName());
    }
    
    int qualifier;
}
