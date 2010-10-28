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
import java.util.Enumeration;

/**
 *
 * @author Katha
 */
public class Method {
    private String ident;
    private Type type;
    private int qualifier;
    private ParamList parameters;
    private ArrayList<Statement> corpo;

    public Method(String ident, Type type, int qualifier){
        this.ident = ident;
        this.type = type;
        this.qualifier = qualifier;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public ParamList getParameters() {
        return parameters;
    }

    public void setParameters(ParamList parameters) {
        this.parameters = parameters;
    }

    public int getQualifier() {
        return qualifier;
    }

    public void setQualifier(int qualifier) {
        this.qualifier = qualifier;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<Statement> getCorpo() {
        return corpo;
    }

    public void setCorpo(ArrayList<Statement> corpo) {
        this.corpo = corpo;
    }

    public void genKrakatoa(PW pw){
        switch(qualifier){
            case 38:
                pw.printIdent("public ");
                break;
            case 39:
                pw.printIdent("private ");
                break;
            case 138:
                pw.printIdent("static public ");
                break;
            case 139:
                pw.printIdent("static public ");
                break;
        }
        pw.print(type + " " + ident + "( ");
        parameters.genKrakatoa(null);
        pw.print("){\n");
        for(int i = 0; i < corpo.size(); i++){
            corpo.get(i).genKrakatoa(pw);
        }
        pw.printIdent("}\n");
    }
}
