/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

public class InstanceVariable extends Variable {
    
    public InstanceVariable( String name, Type type ) {
        super(name, type);
    }

    public void genKrakatoa(PW pw){
        pw.print( super.getName() );
    }
}