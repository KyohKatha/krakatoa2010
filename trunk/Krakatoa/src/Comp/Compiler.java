/*
 *
 *Integrantes
 *
 *      Bruno Antunes da Silva           317292
 *      Katharina Carrapatoso Garcia     317144
 *
 */
package Comp;

/*
 *
 */
import AST.MessageSendStatic;
import AST.*;
import Lexer.*;

import java.io.*;
import java.util.*;

public class Compiler {

    // compile must receive an input with an character less than
    // p_input.lenght
    public Program compile(char[] input, PrintWriter outError) {

        error = new CompilerError(lexer, new PrintWriter(outError));
        symbolTable = new SymbolTable();
        lexer = new Lexer(input, error);
        error.setLexer(lexer);


        Program p = null;
        try {
            lexer.nextToken();
            if (lexer.token == Symbol.EOF) {
                error.show("Unexpected EOF");
            }

            p = program();

            if (lexer.token != Symbol.EOF) {
                p = null;
                error.show("EOF expected");
            }

            ArrayList<ClassDec> verificarProgram = p.getClassList();
            boolean ok = false;
            int i;
            for (i = 0; i < verificarProgram.size(); i++) {
                if (verificarProgram.get(i).getCname().equals("Program")) {
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                error.show("Class Program not found!");
            }

            Method m = verificarProgram.get(i).searchPublicMethod("run");
            if (m == null) {
                error.show("Public method run not found!");
            } else {
                if (!(m.getParameters().getSize() == 0)) {
                    error.show("Method run should not have any parameters");
                }
            }


        } catch (Exception e) {
            // the below statement prints the stack of called methods.
            // of course, it should be removed if the compiler were
            // a production compiler.

            //e.printStackTrace();
            p = null;
        }

        return p;
    }

    private Program program() {
        // Program ::=  ClassDec { ClassDec }
        ArrayList<ClassDec> classes = new ArrayList<ClassDec>();
        classes.add(classDec());
        while (lexer.token == Symbol.CLASS) {
            classes.add(classDec());
        }
        return new Program(classes);
    }

    private ClassDec classDec() {
        // Note que os m�todos desta classe n�o correspondem exatamente �s regras
        // da gram�tica. Este m�todo classDec, por exemplo, implementa
        // a produ��o ClassDec (veja abaixo) e partes de outras produ��es.

        /* ClassDec ::=   ''class'' Id [ ''extends''  Id ]
        "{"   MemberList "}"
        MemberList ::= { Member }
        Member ::= InstVarDec | MethodDec
        InstVarDec ::= [ "static"  ] "private"  Type  IdList  ";"
        MethodDec ::= Qualifier ReturnType Id "("[ FormalParamDec ]  ")"
        "{"  StatementList "}"
        ReturnType ::= Type | "void"
        Qualifier ::=  [ "static"  ] ( "private" |  "public" )
         */
        if (lexer.token != Symbol.CLASS) {
            error.show("'class' expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT) {
            error.show(CompilerError.identifier_expected);
        }
        String className = lexer.getStringValue();
        //incluir classe na tabela de simbolos
        if (symbolTable.getInGlobal(className) != null) {
            error.show("class redeclaration");
        }
        ClassDec nvClasse = new ClassDec(className);
        classeCorrente = nvClasse;
        symbolTable.putInGlobal(className, nvClasse);

        lexer.nextToken();
        if (lexer.token == Symbol.EXTENDS) {
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT) {
                error.show(CompilerError.identifier_expected);
            }
            String superclassName = lexer.getStringValue();
            //verificar se a superclasse eh igual a classe corrente e se ela existe
            ClassDec superClass = symbolTable.getInGlobal(superclassName);
            if (superClass == null) {
                error.show("undeclared superclass");
            }
            if (superClass.getCname().equals(className)) {
                error.show("current class is equal to the superclass");
            }

            nvClasse.setSuperclass(superClass);
            classeCorrente.setSuperclass(superClass);

            lexer.nextToken();
        }
        if (lexer.token != Symbol.LEFTCURBRACKET) {
            error.show("{ expected", true);
        }
        lexer.nextToken();


        //memberslist
        while (lexer.token == Symbol.STATIC || lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {
            int qualifier = 0;
            if(lexer.token == Symbol.STATIC){
                qualifier = 100;
                lexer.nextToken();
            }
            switch (lexer.token) {
                case Symbol.PRIVATE:
                    lexer.nextToken();
                    qualifier += Symbol.PRIVATE;
                    break;
                case Symbol.PUBLIC:
                    lexer.nextToken();
                    qualifier += Symbol.PUBLIC;
                    break;
                default:
                    error.show("static, private, or public expected");
                    qualifier = Symbol.PUBLIC;
            }
            qualifierCorrente = qualifier;
            Type t;
            if (lexer.token == Symbol.VOID) {
                t = Type.voidType;
                lexer.nextToken();
            } else {
                t = type();
            }

            //IdList ::= Id { "," Id }
            // ou MethodDec ::= Qualifier ReturnType Id "(" [ FormalParamDec ] ")" "{" StatementList "}"

            if (lexer.token != Symbol.IDENT) {
                error.show("Identifier expected");
            }
            String name = lexer.getStringValue();

            lexer.nextToken();
            if (lexer.token == Symbol.LEFTPAR) {

                if (qualifier == Symbol.PUBLIC || qualifier == Symbol.PRIVATE) {
                    if (nvClasse.searchMethod(name) != null) {
                        error.show("Method redeclaration");
                    }
                }
                if (qualifier == Symbol.STATICPUBLIC || qualifier == Symbol.STATICPRIVATE) {
                    if (nvClasse.searchStaticMethod(name) != null) {
                        error.show("Method redeclaration");
                    }
                }
                if (symbolTable.getInLocal(name) != null) {
                    error.show("Member redeclaration");
                }
                Method met = new Method(name, t, qualifier);

                if (qualifier == Symbol.PUBLIC) {
                    nvClasse.setPublicMethod(met);
                }

                if (qualifier == Symbol.PRIVATE) {
                    nvClasse.setPrivateMethod(met);
                }

                if (qualifier == Symbol.STATICPRIVATE) {
                    nvClasse.setPrivateStaticMethod(met);
                }

                if (qualifier == Symbol.STATICPUBLIC) {
                    nvClasse.setPublicStaticMethod(met);
                }

                met = methodDec(met);
                metodoCorrente = met;
            } else if (qualifier != Symbol.PRIVATE && qualifier != Symbol.STATICPRIVATE) {
                error.show("Attempt to declare a public instance variable");
            } else {
                InstanceVariableList varList = null;
                varList = instanceVarDec(t, name);
                if (qualifier == Symbol.PRIVATE) {
                    nvClasse.setInstanceVariableList(varList);
                } else {
                    nvClasse.setStaticInstanceVariableList(varList);
                }
            }
        }
        if (lexer.token != Symbol.RIGHTCURBRACKET) {
            error.show("public/private or \"}\" expected");
        }
        lexer.nextToken();

        return nvClasse;
    }

    private InstanceVariableList instanceVarDec(Type type, String name) {
        //   InstVarDec ::= [ "static"  ] "private"  Type  IdList  ";"
        InstanceVariableList variables = null;
        if (symbolTable.getInLocal(name) != null) {
            error.show("variable redeclaration");
        }
        InstanceVariable v = new InstanceVariable(name, type);
        symbolTable.putInLocal(name, v);
        variables.addElement(v);

        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT) {
                error.show("Identifier expected");
            }
            String variableName = lexer.getStringValue();
            if (symbolTable.getInLocal(name) != null) {
                error.show("variable redeclaration");
            }
            v = new InstanceVariable(variableName, type);
            symbolTable.putInLocal(variableName, v);
            lexer.nextToken();
            variables.addElement(v);
        }
        if (lexer.token != Symbol.SEMICOLON) {
            error.show(CompilerError.semicolon_expected);
        }
        lexer.nextToken();
        return variables;
    }

    private Method methodDec(Method met) {
        /*   MethodDec ::= Qualifier ReturnType Id "("[ FormalParamDec ]  ")"
        "{"  StatementList "}"
         */

        lexer.nextToken();
        ParamList parametros = null;
        if (lexer.token != Symbol.RIGHTPAR) {
            parametros = formalParamDec();
        }
        if (lexer.token != Symbol.RIGHTPAR) {
            error.show(") expected");
        }

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTCURBRACKET) {
            error.show("{ expected");
        }

        lexer.nextToken();
        ArrayList<Statement> corpo = statementList();
        if (lexer.token != Symbol.RIGHTCURBRACKET) {
            error.show("} expected");
        }

        met.setCorpo(corpo);

        met.setParameters(parametros);

        lexer.nextToken();
        return met;
    }

    private Statement localDec(Type type) {
        // LocalDec ::= Type IdList ";"


        if (lexer.token != Symbol.IDENT) {
            error.show("Identifier expected");
        }
        if (symbolTable.getInLocal(lexer.getStringValue()) != null) {
            error.show("variable redeclaration");
        }
        LocalVarList vars = new LocalVarList();

        Variable v = new Variable(lexer.getStringValue(), type);
        vars.addElement(v);
        lexer.nextToken();
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT) {
                error.show("Identifier expected");
            }

            if (symbolTable.getInLocal(lexer.getStringValue()) != null) {
                error.show("variable redeclaration");
            }
            v = new Variable(lexer.getStringValue(), type);
            vars.addElement(v);
            lexer.nextToken();
        }

        return vars;
    }

    private ParamList formalParamDec() {
        //  FormalParamDec ::= ParamDec { "," ParamDec }
        ParamList parameters = new ParamList();

        parameters.addElement(paramDec());

        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            parameters.addElement(paramDec());
        }

        return parameters;
    }

    private Variable paramDec() {
        // ParamDec ::= Type Id

        Type t = type();
        if (lexer.token != Symbol.IDENT) {
            error.show("Identifier expected");
        }
        String name = lexer.getStringValue();
        if (symbolTable.getInLocal(name) != null) {
            error.show("parameter redeclaration");
        }
        Variable v = new Variable(name, t);
        symbolTable.putInLocal(name, v);

        lexer.nextToken();

        return v;
    }

    private Type type() {
        // Type ::= BasicType | Id
        Type result;

        switch (lexer.token) {
            case Symbol.INT:
                result = Type.intType;
                break;
            case Symbol.BOOLEAN:
                result = Type.booleanType;
                break;
            case Symbol.STRING:
                result = Type.stringType;
                break;
            //acho que void nao tem
            /*case Symbol.VOID:
            result = Type.voidType;
            break;*/
            case Symbol.IDENT:
                //# corrija: fa�a uma busca na TS para buscar a classe
                // IDENT deve ser uma classe.
                if (symbolTable.getInGlobal(lexer.getStringValue()) == null) {
                    error.show("inexistent class");
                }

                result = symbolTable.getInGlobal(lexer.getStringValue());
                break;
            default:
                error.show("Type expected");
                result = Type.undefinedType;
        }
        lexer.nextToken();
        return result;
    }

    private Statement compositeStatement() {

        lexer.nextToken();
        ArrayList<Statement> stList = statementList();
        if (lexer.token != Symbol.RIGHTCURBRACKET) {
            error.show("} expected");
        } else {
            lexer.nextToken();
        }

        return new CompositeCommand(stList);
    }

    private ArrayList<Statement> statementList() {
        // CompStatement ::= "{" { Statement } "}"
        int tk;
        ArrayList<Statement> statements = new ArrayList();
        // statements always begin with an identifier, if, read, write, ...
        while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET && tk != Symbol.ELSE) {
            statements.add(statement());
        }

        if (tk == Symbol.ELSE) {
            error.show("invalid statement");
        }

        return statements;
    }

    private Statement statement() {
        /*
        Statement ::= Assignment ``;'' | IfStat |WhileStat 
        |  MessageSend ``;''  | ReturnStat ``;''
        |  ReadStat ``;'' | WriteStat ``;'' | ``break'' ``;''
        | ``;'' | CompStatement | LocalDec
         */

        Statement statement = null;

        switch (lexer.token) {
            case Symbol.THIS:
                if (qualifierCorrente == Symbol.STATICPRIVATE || qualifierCorrente == Symbol.STATICPUBLIC) {
                    error.show("'this' can not be used on a static method");
                }
                /*lexer.nextToken();
                if (lexer.token != Symbol.DOT) {
                error.show("Dot expected");
                }
                lexer.nextToken();
                if(lexer.token != Symbol.IDENT)
                error.show("Identifier expected");
                String id = lexer.getStringValue();
                lexer.nextToken();
                if(lexer.token == Symbol.ASSIGN)
                statement = assignment(id);
                else*/
                statement = assignmentMessageSendLocalVarDecStatement();
                break;
            case Symbol.IDENT:
                /*String id = lexer.getStringValue();
                if (symbolTable.getInGlobal(id) != null) {
                statement = localDec(symbolTable.getInGlobal(lexer.getStringValue()));
                } else if (symbolTable.getInLocal(id) == null) {
                error.show("undeclared statement");
                } else {
                lexer.nextToken();
                if(lexer.token == Symbol.ASSIGN)
                statement = assignment(id);
                else
                statement = assignmentMessageSendLocalVarDecStatement(id);
                 *
                 */
                statement = assignmentMessageSendLocalVarDecStatement();
                //}
                break;
            case Symbol.SUPER:
                if (qualifierCorrente == Symbol.STATICPRIVATE || qualifierCorrente == Symbol.STATICPUBLIC) {
                    error.show("'super' can not be used on a static method");
                }
                statement = assignmentMessageSendLocalVarDecStatement();
                break;
            case Symbol.INT:
                lexer.nextToken();
                statement = localDec(Type.intType);
                break;
            case Symbol.BOOLEAN:
                lexer.nextToken();
                statement = localDec(Type.booleanType);
                break;
            case Symbol.STRING:
                lexer.nextToken();
                statement = localDec(Type.stringType);
                break;
            case Symbol.VOID:
                lexer.nextToken();
                statement = localDec(Type.voidType);
                break;
            case Symbol.RETURN:
                statement = returnStatement();
                break;
            case Symbol.READ:
                statement = readStatement();
                break;
            case Symbol.WRITE:
                statement = writeStatement();
                break;
            case Symbol.IF:
                statement = ifStatement();
                break;
            case Symbol.BREAK:
                statement = breakStatement();
                break;
            case Symbol.WHILE:
                statement = whileStatement();
                break;
            case Symbol.SEMICOLON:
                statement = nullStatement();
                break;
            case Symbol.LEFTCURBRACKET:
                statement = compositeStatement();
                break;
            default:
                error.show("Statement expected");
        }
        return statement;
    }

    /*private Statement assignment(String id) {
    Variable v = symbolTable.getInLocal(id);

    if (v == null) {
    error.show("undeclared variable");
    }
    lexer.nextToken();

    if (lexer.token != Symbol.ASSIGN) {
    error.show("assign expected");
    }
    lexer.nextToken();

    Expr expr = expr();
    AssignCommand assign = new AssignCommand(v, expr);
    if (lexer.token != Symbol.SEMICOLON) {
    error.show(CompilerError.semicolon_expected);
    }
    lexer.nextToken();
    return assign;
    }*/
    private Statement assignmentMessageSendLocalVarDecStatement() {
        /*
        Assignment ::= LeftValue "=" Expression
        LeftValue ::= [ "this" "." ] Id
        MessageSend ::= ReceiverMessage "." Id "("  [ ExpressionList ] ")"
        ReceiverMessage ::=  "super" | Id | "this" | "this" "."  Id
        LocalDec ::= Type IdList ";"
         */


        // an assignment, a message send or a local variable declaration
      /*
        ##########################################################################
        ##########################################################################
        IMPORTANTE:
        a implementa��o deste m�todo � muit�ssimo parecido com o do m�todo
        factor. Neste m�todo, factor, coloquei **muito** mais partes implementadas
        do que neste m�todo assignmentMessageSendLocalVarDecStatement. A grande
        diferen�a entre os dois m�todos � que factor analisa uma express�o e
        assignmentMessageSendLocalVarDecStatement analisa uma instru��o. Isto �, um
        envio de mensagem "x.m()" em factor deve retornar um valor e em
        assignmentMessageSendLocalVarDecStatement n�o deve retornar nada.
        Resumindo: fa�a factor primeiro e depois copie e cole grande parte do
        que voc� fez para este m�todo.

        ##########################################################################
        ##########################################################################

         */


        String methodName, variableName;
        ExprList exprList;
        Statement result = null;
        /*
        there are eight possibilities:
        this.id()
        this.id = expr
        this.id.id()
        super.id()
        id = expr
        id.id()

        Id IdList  // Id is a type
         */

        switch (lexer.token) {
            //this
            case Symbol.THIS:
                lexer.nextToken();
                //this.
                if (lexer.token != Symbol.DOT) {
                    error.show(". expected");
                }
                lexer.nextToken();
                //this.id
                if (lexer.token != Symbol.IDENT) {
                    error.show(CompilerError.identifier_expected);
                }
                String ident = lexer.getStringValue();
                lexer.nextToken();
                switch (lexer.token) {
                    //this.id =
                    case Symbol.ASSIGN:
                        lexer.nextToken();
                        //this.id = expr
                        Expr anExpr = expr();
                        Variable v = symbolTable.getInLocal(ident);
                        if (v == null) {
                            error.show("undeclared variable");
                        }
                        result = new AssignCommand(v, anExpr);
                        break;
                    case Symbol.DOT:
                        // this.id.id
                        lexer.nextToken();
                        if (lexer.token != Symbol.IDENT) {
                            error.show(CompilerError.identifier_expected);
                        }
                        Variable v2 = symbolTable.getInLocal(ident);
                        if (v2 == null) {
                            error.show("undeclared variable");
                        }
                        methodName = lexer.getStringValue();
                        if (classeCorrente.searchMethod(methodName) == null) {
                            error.show("method undeclared");
                        }
                        lexer.nextToken();
                        exprList = getRealParameters();
                        boolean flag = true;
                        for (int i = 0; i < metodoCorrente.getParameters().getSize(); i++) {
                            if (metodoCorrente.getParameters().get(i).getType() != exprList.getV().get(i).getType()) {
                                flag = false;
                                break;
                            }
                        }
                        if (!flag) {
                            error.show("Wrong parameters");
                        }
                        result = new MessageSendStatement(new MessageSendToVariable(exprList, v2, metodoCorrente));
                        break;
                    case Symbol.LEFTPAR:
                        // this.id()

                        Method m = classeCorrente.searchMethod(ident);
                        if (m == null) {
                            ClassDec aux = classeCorrente;
                            //procura um metodo nas superclasses da classe corrente
                            while ((aux = aux.getSuperclass()) != null) {
                                m = aux.searchPublicMethod(ident);
                                if (m != null) {
                                    break;
                                }
                                aux = aux.getSuperclass();
                            }
                        }
                        if (m == null) {
                            error.show("undeclared method");
                        }
                        exprList = getRealParameters();
                        boolean flag2 = true;
                        for (int i = 0; i < m.getParameters().getSize(); i++) {
                            if (m.getParameters().get(i).getType() != exprList.getV().get(i).getType()) {
                                flag = false;
                                break;
                            }
                        }
                        if (!flag2) {
                            error.show("Wrong parameters");
                        }
                        result = new MessageSendStatement(
                                new MessageSendToSelf(m, exprList));
                        break;
                    default:
                        error.show(CompilerError.identifier_expected);
                }
                break;
            case Symbol.SUPER:
                // super.id()
                lexer.nextToken();
                if (lexer.token != Symbol.DOT) {
                    error.show(". expected");
                }
                lexer.nextToken();
                if (lexer.token != Symbol.IDENT) {
                    error.show(CompilerError.identifier_expected);
                }
                methodName = lexer.getStringValue();
                ClassDec aux = classeCorrente;
                Method m = null;
                //procura um metodo nas superclasses
                while ((aux = aux.getSuperclass()) != null) {
                    m = aux.searchPublicMethod(methodName);
                    if (m != null) {
                        break;
                    }
                    aux = aux.getSuperclass();
                }

                if (m == null) {
                    error.show("undeclared method");
                }
                lexer.nextToken();
                exprList = getRealParameters();
                boolean flag = true;
                for (int i = 0; i < m.getParameters().getSize(); i++) {
                    if (m.getParameters().get(i).getType() != exprList.getV().get(i).getType()) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    error.show("Wrong parameters");
                }
                result = new MessageSendStatement(new MessageSendToSuper(classeCorrente.getSuperclass(), m, exprList));
                break;
            case Symbol.IDENT:
                //id
                variableName = lexer.getStringValue();

                lexer.nextToken();
                switch (lexer.token) {
                    case Symbol.ASSIGN:
                        // id = expr
                        Variable v = symbolTable.getInLocal(variableName);
                        if (v == null) {
                            error.show("undeclared variable");
                        }
                        lexer.nextToken();
                        Expr anExpr = expr();
                        //# corrija
                        result = new AssignCommand(v, anExpr);
                        break;
                    case Symbol.IDENT:
                        // id id;
                        // variableName id
                        ClassDec cl = symbolTable.getInGlobal(variableName);
                        if (cl == null) {
                            error.show("undeclared class");
                        }
                        // variableName must be the name of a class
                        // replace null in the statement below by
                        // a point to the class named variableName.
                        // A search in the symbol table is necessary.
                        localDec(cl);
                        break;
                    case Symbol.DOT:
                        // id.id()
                        //Verifica se o primeiro 'id' eh uma variavel ou Classe
                        Variable v2 = symbolTable.getInLocal(variableName);
                        ClassDec clInit = symbolTable.getInGlobal(variableName);
                        ClassDec cl2 = null;
                        if (v2 == null && clInit == null) {
                            error.show("undeclared variable/class");
                        }
                        lexer.nextToken();
                        methodName = lexer.getStringValue();
                        Method m2 = null;
                        //vai procurar se o metodo existe na classe da variavel, ou em suas superclasses
                        if (v2 != null) {
                            cl2 = (ClassDec) v2.getType();
                            m2 = cl2.searchPublicMethod(methodName);
                            if (m2 == null) {
                                ClassDec aux2 = cl2;
                                while ((aux2 = aux2.getSuperclass()) != null) {
                                    m = aux2.searchPublicMethod(methodName);
                                    if (m != null) {
                                        break;
                                    }
                                    //aux = aux2.getSuperclass();
                                }
                            }
                        }
                        //vai procurar se o metodo existe na classe (static), ou em suas superclasses
                        if (clInit != null) {
                            cl2 = clInit;
                            //se o metodo chamado esta dentro de um metodo da classe corrente entao pode ser um metodo static public ou private
                            if (classeCorrente == clInit) {
                                m2 = cl2.searchStaticMethod(methodName);
                            } else {
                                m2 = cl2.searchPublicStaticMethod(methodName);
                            }
                            if (m2 == null) {
                                ClassDec aux2 = cl2;
                                while ((aux2 = aux2.getSuperclass()) != null) {
                                    m = aux2.searchPublicStaticMethod(methodName);
                                    if (m != null) {
                                        break;
                                    }
                                    //aux = aux2.getSuperclass();
                                }
                            }
                        }
                        if (m2 == null) {
                            error.show("undeclared Method");
                        }
                        lexer.nextToken();
                        exprList = getRealParameters();
                        boolean flag2 = true;
                        for (int i = 0; i < m2.getParameters().getSize(); i++) {
                            if (m2.getParameters().get(i).getType() != exprList.getV().get(i).getType()) {
                                flag2 = false;
                                break;
                            }
                        }
                        if (!flag2) {
                            error.show("Wrong parameters");
                        }
                        result = new MessageSendStatement(new MessageSendToVariable(exprList, v2, m2));
                        break;
                    default:
                        error.show(". or = expected");
                }
                break;
            default:
                error.show("'this', 'super', basic type or identifier expected");
        }
        if (lexer.token != Symbol.SEMICOLON) {
            error.show(CompilerError.semicolon_expected);
        }
        lexer.nextToken();
        return result;
    }

    private ExprList getRealParameters() {
        ExprList anExprList = null;

        if (lexer.token != Symbol.LEFTPAR) {
            error.show("( expected");
        }
        lexer.nextToken();
        if (startExpr(lexer.token)) {
            anExprList = exprList();
        }
        if (lexer.token != Symbol.RIGHTPAR) {
            error.show(") expected");
        }
        lexer.nextToken();
        return anExprList;
    }

    private Statement whileStatement() {
        Statement whileSt = null;
        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
            error.show("( expected");
        }
        lexer.nextToken();
        Expr expr = expr();
        if (lexer.token != Symbol.RIGHTPAR) {
            error.show(") expected");
        }
        lexer.nextToken();
        whileSt = statement();

        WhileCommand whileCom = new WhileCommand(expr, whileSt);
        return whileCom;
    }

    private Statement ifStatement() {
        Statement ifSt = null, elseSt = null;
        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
            error.show("( expected");
        }
        lexer.nextToken();
        Expr expr = expr();
        if (lexer.token != Symbol.RIGHTPAR) {
            error.show(") expected");
        }
        lexer.nextToken();
        ifSt = statement();
        if (lexer.token == Symbol.ELSE) {
            lexer.nextToken();
            elseSt = statement();
        }
        IfCommand ifCom = new IfCommand(expr, ifSt, elseSt);
        return ifCom;
    }

    private Statement returnStatement() {
        lexer.nextToken();
        Expr expr = expr();
        if (lexer.token != Symbol.SEMICOLON) {
            error.show(CompilerError.semicolon_expected);
        }
        lexer.nextToken();

        ReturnCommand retunrCom = new ReturnCommand(expr);
        return retunrCom;
    }

    private Statement readStatement() {
        lexer.nextToken();
        ReadCommand readCom = new ReadCommand();
        if (lexer.token != Symbol.LEFTPAR) {
            error.show("( expected");
        }
        lexer.nextToken();
        while (true) {
            if (lexer.token == Symbol.THIS) {
                lexer.nextToken();
                if (lexer.token != Symbol.DOT) {
                    error.show(". expected");
                }
                lexer.nextToken();
            }
            if (lexer.token != Symbol.IDENT) {
                error.show(CompilerError.identifier_expected);
            }

            String name = (String) lexer.getStringValue();

            Variable v = symbolTable.getInLocal(name);

            if (v == null) {
                error.show("undeclared variable");
            }

            readCom.addVariable(v);

            lexer.nextToken();
            if (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
            } else {
                break;
            }
        }

        if (lexer.token != Symbol.RIGHTPAR) {
            error.show(") expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.SEMICOLON) {
            error.show(CompilerError.semicolon_expected);
        }
        lexer.nextToken();
        return readCom;
    }

    private Statement writeStatement() {

        lexer.nextToken();
        if (lexer.token != Symbol.LEFTPAR) {
            error.show("( expected");
        }
        lexer.nextToken();
        ExprList exprList = exprList();
        if (lexer.token != Symbol.RIGHTPAR) {
            error.show(") expected");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.SEMICOLON) {
            error.show(CompilerError.semicolon_expected);
        }
        lexer.nextToken();
        return new WriteCommand(exprList);
    }

    private Statement breakStatement() {
        lexer.nextToken();
        if (lexer.token != Symbol.SEMICOLON) {
            error.show(CompilerError.semicolon_expected);
        }
        lexer.nextToken();
        return new BreakCommand();
    }

    private Statement nullStatement() {
        lexer.nextToken();
        return new NullCommand();
    }

    private ExprList exprList() {
        // ExpressionList ::= Expression { "," Expression }


        ExprList anExprList = new ExprList();
        anExprList.addElement(expr());
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            anExprList.addElement(expr());
        }
        return anExprList;
    }

    private Expr expr() {

        Expr left = simpleExpr();
        int op = lexer.token;
        if (op == Symbol.EQ || op == Symbol.NEQ
                || op == Symbol.LE || op == Symbol.LT
                || op == Symbol.GE || op == Symbol.GT) {
            lexer.nextToken();
            Expr right = simpleExpr();
            left = new CompositeExpr(left, op, right);
        }
        return left;
    }

    private Expr simpleExpr() {
        int op;

        Expr left = term();
        while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
                || op == Symbol.OR) {
            lexer.nextToken();
            Expr right = term();
            left = new CompositeExpr(left, op, right);
        }
        return left;
    }

    private Expr term() {
        int op;

        Expr left = signalFactor();
        while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
                || op == Symbol.AND) {
            lexer.nextToken();
            Expr right = signalFactor();
            left = new CompositeExpr(left, op, right);
        }
        return left;
    }

    private Expr signalFactor() {
        int op;
        if ((op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS) {
            lexer.nextToken();
            return new SignalExpr(op, factor());
        } else {
            return factor();
        }
    }

    private Expr factor() {
        /*
        Factor ::= BasicValue | RightValue | MessageSend  | "(" Expression ")" 
        | "!" Factor | "null" | ObjectCreation
        BasicValue ::= IntValue | BooleanValue | StringValue
        BooleanValue ::= "true" | "false"
        RightValue ::= "this" [ "." Id ] | Id
        MessageSend ::= ReceiverMessage "." Id "("  [ ExpressionList ] ")"
        ReceiverMessage ::=  "super" | Id | "this" | "this" "."  Id
        ObjectCreation ::= ``new" Id ``("  ``)"
         */

        Expr e;
        Variable aVariable;
        ClassDec aClass;
        //MethodDec aMethod;
        InstanceVariable anInstanceVariable;

        switch (lexer.token) {
            case Symbol.LITERALSTRING:
                String literalString = lexer.getLiteralStringValue();
                lexer.nextToken();
                return new LiteralStringExpr(literalString);
            case Symbol.NOT:
                lexer.nextToken();
                e = expr();
                return new UnaryExpr(e, Symbol.NOT);
            case Symbol.TRUE:
                lexer.nextToken();
                return BooleanExpr.True;
            case Symbol.FALSE:
                lexer.nextToken();
                return BooleanExpr.False;
            case Symbol.LEFTPAR:
                lexer.nextToken();
                e = expr();
                if (lexer.token != Symbol.RIGHTPAR) {
                    error.show(") expected");
                }
                lexer.nextToken();
                return new ParenthesisExpr(e);
            case Symbol.NULL:
                lexer.nextToken();
                return new NullExpr();
            case Symbol.NUMBER:
                return number();
            case Symbol.NEW:
                lexer.nextToken();
                if (lexer.token != Symbol.IDENT) {
                    error.show("Identifier expected");
                }

                String className = lexer.getStringValue();
                /*
                // encontre a classe className in symbol table
                ClassDec aClass = symbolTable.getInGlobal(className);
                if ( aClass == null ) ...
                 */


                lexer.nextToken();
                if (lexer.token != Symbol.LEFTPAR) {
                    error.show("( expected");
                }
                lexer.nextToken();
                if (lexer.token != Symbol.RIGHTPAR) {
                    error.show(") expected");
                }
                lexer.nextToken();
                /* return an object representing the creation of an object
                something as
                return new Cria_um_objeto(aClass);
                � importante n�o utilizar className, uma string e sim aClass, um objeto.
                 */
                return null;
            default:
                String variableName,
                 methodName;
                ExprList exprList;
                /* there are seven cases to consider :
                super.m()
                this.x
                this
                this.m()
                this.x.m();
                x
                x.m()

                in which x is either a variable or
                an instance variable and m is a method
                 */
                switch (lexer.token) {
                    case Symbol.SUPER:
                        // expression of the kind "super.m()"
                        lexer.nextToken();
                        if (lexer.token != Symbol.DOT) {
                            error.show(". expected");
                        }
                        lexer.nextToken();
                        if (lexer.token != Symbol.IDENT) {
                            error.show(CompilerError.identifier_expected);
                        }
                        methodName = lexer.getStringValue();
                        ClassDec aux = classeCorrente;
                        Method m = null;
                        while ((aux = aux.getSuperclass()) != null) {
                            m = aux.searchPublicMethod(methodName);
                            if (m != null) {
                                break;
                            }
                        }
                        if (m == null) {
                            error.show("Undefined method in super class");
                        }
                        lexer.nextToken();
                        exprList = getRealParameters();
                        boolean flag = true;
                        for (int i = 0; i < m.getParameters().getSize(); i++) {
                            if (m.getParameters().get(i).getType() != exprList.getV().get(i).getType()) {
                                flag = false;
                                break;
                            }
                        }
                        if (!flag) {
                            error.show("Wrong parameters");
                        }
                        //#  corrija
                  /* 
                        deve existir uma vari�vel de inst�ncia currentClass.
                        aClass = currentClass.getSuperclass();
                        if ( aClass == null )
                        ...
                        aMethod = aClass.getMethod(methodName);
                        if ( aMethod == null )
                        ...
                         */
                        return new MessageSendToSuper(classeCorrente.getSuperclass(), m, exprList);

                    case Symbol.THIS:
                        lexer.nextToken();
                        if (lexer.token != Symbol.DOT) {
                            // expression of the kind "this"
                            //# corrija
                     /* 
                            Verifique se n�o estamos em um m�todo est�tico
                            o construtor da classe ThisExpr deve tomar a classe corrente
                            como par�metro. Por qu� ?
                            return new ThisExpr(currentClass);
                             */
                            ThisExpr thisExpr = new ThisExpr(classeCorrente);
                            return thisExpr;
                        } else {
                            lexer.nextToken();
                            if (lexer.token != Symbol.IDENT) {
                                error.show(CompilerError.identifier_expected);
                            }
                            // it may be method name or an instance variable
                            String ident = lexer.getStringValue();
                            lexer.nextToken();
                            switch (lexer.token) {
                                case Symbol.LEFTPAR:
                                    // expression of the kind "this.m()"
                                    Method m2 = classeCorrente.searchMethod(ident);
                                    if (m2 == null) {
                                        ClassDec aux2 = classeCorrente;

                                        while ((aux2 = aux2.getSuperclass()) != null) {
                                            m2 = aux2.searchPublicMethod(ident);
                                            if (m2 != null) {
                                                break;
                                            }
                                        }
                                    }
                                    if (m2 == null) {
                                        error.show("Undeclared method");
                                    }
                                    //# corrija
                                    exprList = getRealParameters();
                                    boolean flag2 = true;
                                    for (int i = 0; i < m2.getParameters().getSize(); i++) {
                                        if (m2.getParameters().get(i).getType() != exprList.getV().get(i).getType()) {
                                            flag2 = false;
                                            break;
                                        }
                                    }
                                    if (!flag2) {
                                        error.show("Wrong parameters");
                                    }
                                    /*
                                    procure o m�todo ident na classe corrente:
                                    aMethod = currentClass.searchMethod(ident);
                                    if ( aMethod == null )
                                    ...
                                    confira se aMethod pode aceitar os par�metros de exprList.
                                     */
                                    return new MessageSendToSelf(m2, exprList);

                                case Symbol.DOT:
                                    // expression of the kind "this.x.m()"
                                    //# corrija
                                    Variable var = null;
                                    ArrayList<Variable> varList = classeCorrente.getInstanceVariableList().getInstanceVariableList();
                                    for (int i = 0; i < varList.size(); i++) {
                                        if (varList.get(i).getName().equals(ident)) {
                                            var = varList.get(i);
                                            break;
                                        }
                                    }

                                    if (var == null) {
                                        error.show("Undeclared instance varible");
                                    }

                                    lexer.nextToken();
                                    if (lexer.token != Symbol.IDENT) {
                                        error.show(CompilerError.identifier_expected);
                                    }
                                    methodName = lexer.getStringValue();
                                    if (!(var.getType() instanceof ClassDec)) {
                                        error.show("Instance variable is not of a Class type");
                                    }
                                    ClassDec cl = (ClassDec) var.getType();
                                    Method m3 = cl.searchPublicMethod(ident);
                                    if (m3 == null) {
                                        ClassDec aux2 = cl;
                                        while ((aux2 = aux2.getSuperclass()) != null) {
                                            m3 = aux2.searchPublicMethod(ident);
                                            if (m3 != null) {
                                                break;
                                            }
                                        }
                                    }
                                    if (m3 == null) {
                                        error.show("Undeclared method");
                                    }
                                    lexer.nextToken();
                                    exprList = getRealParameters();
                                    boolean flag3 = true;
                                    for (int i = 0; i < m3.getParameters().getSize(); i++) {
                                        if (m3.getParameters().get(i).getType() != exprList.getV().get(i).getType()) {
                                            flag3 = false;
                                            break;
                                        }
                                    }
                                    if (!flag3) {
                                        error.show("Wrong parameters");
                                    }
                                    /*
                                    em this.x.m(), x est� em ident e m em methodName
                                    procure por x na lista de vari�veis de inst�ncia da classe corrente:
                                    anInstanceVariable = currentClass.searchInstanceVariable(ident);
                                    if ( anInstanceVariable == null )
                                    ...
                                    pegue a classe declarada de x, o tipo de x:
                                    if ( ! (anInstanceVariable.getType() instanceof ClassDec) )
                                    ... // tipo de x n�o � uma classe, erro
                                    confira se a classe de x possui m�todo m:
                                    aClass = (ClassDec ) anInstanceVariable.getType();
                                    aMethod = aClass.searchMethod(methodName);
                                    if ( aMethod == null )
                                    ...
                                     */
                                    return new MessageSendToVariable(exprList, var, m3);
                                default:
                                    // expression of the kind "this.x"
                                    //# corrija
                                    Variable var2 = null;
                                    ArrayList<Variable> varList2 = classeCorrente.getInstanceVariableList().getInstanceVariableList();
                                    for (int i = 0; i < varList2.size(); i++) {
                                        if (varList2.get(i).getName().equals(ident)) {
                                            var2 = varList2.get(i);
                                            break;
                                        }
                                    }
                                    if (var2 == null) {
                                        error.show("Undeclared instance varible");
                                    }
                                    /*
                                    procure x na lista de vari�veis de inst�ncia da classe corrente:
                                    anInstanceVariable = currentClass.searchInstanceVariable(ident);
                                    if ( anInstanceVariable == null )
                                    ...*/
                                    return new VariableExpr(var2);

                            }

                        }
                    case Symbol.IDENT:
                        variableName = lexer.getStringValue();
                        lexer.nextToken();
                        if (lexer.token != Symbol.DOT) {
                            // expression of the kind "x"
                            //# corrija
                            Variable var = symbolTable.getInLocal(variableName);
                            if (var == null) {
                                error.show("undeclared variable");
                            }
                            /*
                            if ( (aVariable = symbolTable.get...(variableName)) == null )
                            ...*/
                            return new VariableExpr(var);

                        } else {
                            // expression of the kind "x.m()"
                            lexer.nextToken();  // eat the dot
                            switch (lexer.token) {
                                case Symbol.IDENT:
                                    ClassDec cl = null;
                                    Method m3 = null;
                                    methodName = lexer.getStringValue();
                                    lexer.nextToken();
                                    exprList = getRealParameters();
                                    Variable var = symbolTable.getInLocal(variableName);
                                    if (var != null) {
                                        error.show("undeclared variable");

                                        if (!(var.getType() instanceof ClassDec)) {
                                            error.show("Instance variable is not of a Class type");
                                        }
                                        cl = (ClassDec) var.getType();
                                        m3 = cl.searchPublicMethod(methodName);
                                        if (m3 == null) {
                                            ClassDec aux2 = cl;
                                            while ((aux2 = aux2.getSuperclass()) != null) {
                                                m3 = aux2.searchPublicMethod(methodName);
                                                if (m3 != null) {
                                                    break;
                                                }
                                            }
                                        }
                                        if (m3 == null) {
                                            error.show("Undeclared method");
                                        }
                                        boolean flag2 = true;
                                        for (int i = 0; i < m3.getParameters().getSize(); i++) {
                                            if (m3.getParameters().get(i).getType() != exprList.getV().get(i).getType()) {
                                                flag2 = false;
                                                break;
                                            }
                                        }
                                        if (!flag2) {
                                            error.show("Wrong parameters");
                                        }
                                    } else {

                                        if ((cl = symbolTable.getInGlobal(variableName)) == null) {
                                            error.show("Undeclared class");
                                        }
                                        //Method m3 = cl.searchStaticMethod(methodName);
                                        m3 = cl.searchMethod(methodName);
                                        if (m3 == null) {
                                            ClassDec aux2 = cl;
                                            while ((aux2 = aux2.getSuperclass()) != null) {
                                                m3 = aux2.searchPublicMethod(methodName);
                                                if (m3 != null) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    return new MessageSendStatic(m3, cl, exprList);

                                default:
                                    error.show(CompilerError.identifier_expected);
                            }
                        }
                        break;
                    default:
                        error.show(CompilerError.identifier_expected);
                }
                return null;
        }

    }

    private NumberExpr number() {

        NumberExpr e = null;

        // the number value is stored in lexer.getToken().value as an object of Integer.
        // Method intValue returns that value as an value of type int.
        int value = lexer.getNumberValue();
        lexer.nextToken();
        return new NumberExpr(value);
    }

    private boolean startExpr(int aToken) {

        return lexer.token == Symbol.FALSE
                || lexer.token == Symbol.TRUE
                || lexer.token == Symbol.NOT
                || lexer.token == Symbol.THIS
                || lexer.token == Symbol.NUMBER
                || lexer.token == Symbol.SUPER
                || lexer.token == Symbol.LEFTPAR
                || lexer.token == Symbol.NULL
                || lexer.token == Symbol.IDENT
                || lexer.token == Symbol.LITERALSTRING;

    }
    private SymbolTable symbolTable;
    private Lexer lexer;
    private CompilerError error;
    private ClassDec classeCorrente;
    private Method metodoCorrente;
    private int qualifierCorrente;
}
