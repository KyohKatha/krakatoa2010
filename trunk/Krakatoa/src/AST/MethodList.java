/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
}
