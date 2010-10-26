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

    private ArrayList methods;

    public ArrayList getMethods() {
        return methods;
    }

    public void setMethods(ArrayList methods) {
        this.methods = methods;
    }

    public void genKrakatoa(){

    }

}
