package AST;

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

    public ClassDec getSuperclass() {
        return superclass;
    }

    public void setSuperclass(ClassDec superclass) {
        this.superclass = superclass;
    }


    private String name;
    private ClassDec superclass;
    private InstanceVariableList instanceVariableList;
    private MethodList publicMethodList, privateMethodList;
    // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
    // entre outros m�todos
}
