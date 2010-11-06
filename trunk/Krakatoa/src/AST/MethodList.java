/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */

package AST;

import java.util.ArrayList;

/**
 *
 * @author Katha
 */
public class MethodList {
    public MethodList() {
       methods = new ArrayList();
    }

    public void addMethod(Method method){
        methods.add(method);
    }

    public ArrayList getMethods() {
        return methods;
    }

    public void setMethods(ArrayList methods) {
        this.methods = methods;
    }

    public void genKrakatoa(PW pw){
        for( int i = 0; i < methods.size(); i++){
            Method m = methods.get(i);
            m.genKrakatoa(pw);
            pw.print("\n");
        }
    }

    private ArrayList<Method> methods;
}
