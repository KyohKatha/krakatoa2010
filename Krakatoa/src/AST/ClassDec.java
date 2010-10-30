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

public class ClassDec extends Type {

    public ClassDec(String name) {
        super(name);
    }

    public String getCname() {
        return getName();
    }

    public InstanceVariableList getInstanceVariableList() {
        return instanceVariableList;
    }

    public void setInstanceVariableList(InstanceVariableList instanceVariableList) {
        this.instanceVariableList = instanceVariableList;
    }

    public InstanceVariableList getStaticInstanceVariableList() {
        return staticInstanceVariableList;
    }

    public void setStaticInstanceVariableList(InstanceVariableList staticInstanceVariableList) {
        this.staticInstanceVariableList = staticInstanceVariableList;
    }



    public ClassDec getSuperclass() {
        return superclass;
    }

    public void setSuperclass(ClassDec superclass) {
        this.superclass = superclass;
    }

    public void setPublicMethod(Method method) {
        publicMethodList.addMethod(method);
    }

    public void setPrivateMethod(Method method) {
        privateMethodList.addMethod(method);
    }

    public void setPublicStaticMethod(Method method) {
        staticPublicMethodList.addMethod(method);
    }

    public void setPrivateStaticMethod(Method method) {
        staticPrivateMethodList.addMethod(method);
    }

    public Method searchMethod(String nome) {
        ArrayList<Method> puMet = publicMethodList.getMethods();
        ArrayList<Method> prMet = privateMethodList.getMethods();
        for (int i = 0; i < puMet.size(); i++) {
            if (puMet.get(i).getIdent().equals(nome)) {
                return puMet.get(i);
            }
        }

        for (int i = 0; i < prMet.size(); i++) {
            if (prMet.get(i).getIdent().equals(nome)) {
                return prMet.get(i);
            }
        }

        return null;
    }

    public Method searchPublicMethod(String nome) {
        ArrayList<Method> puMet = publicMethodList.getMethods();
        for (int i = 0; i < puMet.size(); i++) {
            if (puMet.get(i).getIdent().equals(nome)) {
                return puMet.get(i);
            }
        }

        return null;
    }

    public Method searchPublicStaticMethod(String nome) {
        ArrayList<Method> stMet = staticPublicMethodList.getMethods();
        for (int i = 0; i < stMet.size(); i++) {
            if (stMet.get(i).getIdent().equals(nome)) {
                return stMet.get(i);
            }
        }
		   return null;
	}
		
    public void genKrakatoa(PW pw){
        pw.printIdent("public class "+ name);
        if(superclass != null){
            pw.print(" extends "+ superclass.getKrakatoaName());
        }
        pw.print(" {\n");
        pw.add();
        instanceVariableList.genKrakatoa(pw);
        pw.printIdent("\n");
        publicMethodList.genKrakatoa(pw);
        pw.printIdent("\n");
        privateMethodList.genKrakatoa(pw);
        pw.sub();
        pw.printIdent("}\n");
    }

    public String getKrakatoaName(){
        return name;
    }

     public Method searchPrivateStaticMethod(String nome) {
        ArrayList<Method> stMet = staticPrivateMethodList.getMethods();
        for (int i = 0; i < stMet.size(); i++) {
            if (stMet.get(i).getIdent().equals(nome)) {
                return stMet.get(i);
            }
        }

        return null;
    }

    public Method searchStaticMethod(String nome) {
        ArrayList<Method> puMet = staticPublicMethodList.getMethods();
        ArrayList<Method> prMet = staticPrivateMethodList.getMethods();
        for (int i = 0; i < puMet.size(); i++) {
            if (puMet.get(i).getIdent().equals(nome)) {
                return puMet.get(i);
            }
        }

        for (int i = 0; i < prMet.size(); i++) {
            if (prMet.get(i).getIdent().equals(nome)) {
                return prMet.get(i);
            }
        }

        return null;
    }


    public void genKrakatoa() {
        
    }
    private String name;
    private ClassDec superclass;
    private InstanceVariableList instanceVariableList, staticInstanceVariableList;
    private MethodList publicMethodList, privateMethodList, staticPublicMethodList, staticPrivateMethodList;
    // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
    // entre outros m�todos
}
