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
public class Method {
    private String ident;
    private Type type;
    private int qualifier;
    private ArrayList parameters;
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

    public ArrayList getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList parameters) {
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

}
