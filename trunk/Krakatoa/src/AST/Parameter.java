/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;


public class Parameter extends Variable {
    
    public Parameter( String name, Type type ) {
        super(name, type);
    }

    public void genKrakatoa(PW pw){
        super.genKrakatoa(pw);
    }
}