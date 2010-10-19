package Comp;

/*
 *      Até onde já foi feito:
 *      Program
 *          ClassDec
 *              MethodDec
 *
 *              InstanceVarDec
 *
 *      Obs:
 *          Falta descer nos métodos chamados por MethodDec (FormalParamDec e StatementList)
 *
 */
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

            lexer.nextToken();
        }
        if (lexer.token != Symbol.LEFTCURBRACKET) {
            error.show("{ expected", true);
        }
        lexer.nextToken();


        //memberslist
        while (lexer.token == Symbol.STATICPUBLIC || lexer.token == Symbol.STATICPRIVATE
                || lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {

            int qualifier;
            switch (lexer.token) {
                case Symbol.PRIVATE:
                    lexer.nextToken();
                    qualifier = Symbol.PRIVATE;
                    break;
                case Symbol.PUBLIC:
                    lexer.nextToken();
                    qualifier = Symbol.PUBLIC;
                    break;
                case Symbol.STATICPUBLIC:
                    lexer.nextToken();
                    qualifier = Symbol.STATICPUBLIC;
                case Symbol.STATICPRIVATE:
                    lexer.nextToken();
                    qualifier = Symbol.STATICPRIVATE;
                default:
                    error.show("static, private, or public expected");
                    qualifier = Symbol.PUBLIC;
            }
            Type t;
            if(lexer.token == Symbol.VOID){
                t = Type.voidType;
            }
            else
                t = type();

            //IdList ::= Id { "," Id }
            // ou MethodDec ::= Qualifier ReturnType Id "(" [ FormalParamDec ] ")" "{" StatementList "}"
            if (lexer.token != Symbol.IDENT) {
                error.show("Identifier expected");
            }
            String name = lexer.getStringValue();

            lexer.nextToken();
            if (lexer.token == Symbol.LEFTPAR) {

                if (nvClasse.searchMethod(name)) {
                    error.show("Method redeclaration");
                }

                Method met = new Method(name, t, qualifier);

                if (qualifier == Symbol.PUBLIC || qualifier == Symbol.STATICPUBLIC) {
                    nvClasse.setPublicMethod(met);
                }

                if (qualifier == Symbol.PRIVATE || qualifier == Symbol.STATICPRIVATE) {
                    nvClasse.setPrivateMethod(met);
                }

                met = methodDec(met);
                metodoCorrente = met;
            } else if (qualifier != Symbol.PRIVATE && qualifier != Symbol.STATICPRIVATE) {
                error.show("Attempt to declare a public instance variable");
            } else {
                InstanceVariableList varList = null;
                varList = instanceVarDec(t, name);
                nvClasse.setInstanceVariableList(varList);
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
        ArrayList parametros = null;
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

    private ArrayList formalParamDec() {
        //  FormalParamDec ::= ParamDec { "," ParamDec }
        ArrayList<Variable> parameters = new ArrayList();

        parameters.add(paramDec());

        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            parameters.add(paramDec());
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
                lexer.nextToken();
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
                else
                    statement = assignmentMessageSendLocalVarDecStatement(id);
                break;
            case Symbol.IDENT:
                String id2 = lexer.getStringValue();
                if (symbolTable.getInGlobal(id2) != null) {
                    statement = localDec(symbolTable.getInGlobal(lexer.getStringValue()));
                } else if (symbolTable.getInLocal(id2) == null) {
                    error.show("undeclared statement");
                } else {
                    lexer.nextToken();
                    if(lexer.token == Symbol.ASSIGN)
                        statement = assignment(id2);
                    else
                        statement = assignmentMessageSendLocalVarDecStatement(id2);
                }
                break;
            case Symbol.SUPER:
                lexer.nextToken();
                if (lexer.token != Symbol.DOT) {
                    error.show("Dot expected");
                }
                lexer.nextToken();
                if(lexer.token != Symbol.IDENT)
                    error.show("Identifier expected");
                String id3 = lexer.getStringValue();
                statement = assignmentMessageSendLocalVarDecStatement(id3);
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

    private Statement assignment(String id) {
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
    }

    private Statement assignmentMessageSendLocalVarDecStatement(String id) {
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
                Variable v = symbolTable.getInLocal(ident);
                if(v == null)
                    error.show("undeclared variable");
                lexer.nextToken();
                switch (lexer.token) {
                    //this.id =
                    case Symbol.ASSIGN:
                        lexer.nextToken();
                        //this.id = expr
                        Expr anExpr = expr();
                        result = new AssignCommand(v,anExpr);
                        break;
                    case Symbol.DOT:
                        // this.id.id
                        lexer.nextToken();
                        if (lexer.token != Symbol.IDENT) {
                            error.show(CompilerError.identifier_expected);
                        }
                        methodName = lexer.getStringValue();
                        lexer.nextToken();
                        exprList = getRealParameters();
                        //# corrija
                        result = new MessageSendStatement(new MessageSendToVariable(exprList,v, metodoCorrente) );
                        break;
                    case Symbol.LEFTPAR:
                        // this.id()
                        exprList = getRealParameters();
                        //# corrija
                        result = new MessageSendStatement(
                        new MessageSendToSelf( metodoCorrente, exprList ) );
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
                lexer.nextToken();
                exprList = getRealParameters();
                //# corrija
                // result = new MessageSendStatement(
                //     new MessageSendToSuper( pointer to class, pointer to method, exprList) );
                break;
            case Symbol.IDENT:
                variableName = lexer.getStringValue();
                lexer.nextToken();
                switch (lexer.token) {
                    case Symbol.ASSIGN:
                        // id = expr
                        lexer.nextToken();
                        Expr anExpr = expr();
                        //# corrija
                  /* result = new AssignmentStatement( pointer to variable,
                        anExpr ); */
                        break;
                    case Symbol.IDENT:
                        // id id;
                        // variableName id

                        // variableName must be the name of a class
                        // replace null in the statement below by
                        // a point to the class named variableName.
                        // A search in the symbol table is necessary.
                        localDec(null);
                        break;
                    case Symbol.DOT:
                        // id.id()
                        lexer.nextToken();
                        methodName = lexer.getStringValue();
                        lexer.nextToken();
                        exprList = getRealParameters();
                        //# corrija
                  /* result = new MessageSendStatement( 
                        new MessageSendToVariable( pointer to variable,
                        pointer to method, exprList ) ); */
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
        IfCommand ifCom = new IfCommand(expr,ifSt, elseSt);
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

            if(v == null)
                error.show("undeclared variable");

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
                        lexer.nextToken();
                        exprList = getRealParameters();
                        //#  corrija
                  /* 
                        deve existir uma vari�vel de inst�ncia currentClass.
                        aClass = currentClass.getSuperclass();
                        if ( aClass == null )
                        ...
                        aMethod = aClass.getMethod(methodName);
                        if ( aMethod == null )
                        ...

                        return new MessageSendToSuper(
                        aClass, aMethod, exprList);
                         */
                        break;
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
                            ThisExpr thisExpr = new ThisExpr(symbolTable.getLastClassInsertedOnTheTable());
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
                                    //# corrija
                                    exprList = getRealParameters();
                                    /*
                                    procure o m�todo ident na classe corrente:
                                    aMethod = currentClass.searchMethod(ident);
                                    if ( aMethod == null )
                                    ...
                                    confira se aMethod pode aceitar os par�metros de exprList.
                                    return new MessageSendToSelf( aMethod, exprList );
                                     */
                                    break;
                                case Symbol.DOT:
                                    // expression of the kind "this.x.m()"
                                    //# corrija
                                    lexer.nextToken();
                                    if (lexer.token != Symbol.IDENT) {
                                        error.show(CompilerError.identifier_expected);
                                    }
                                    methodName = lexer.getStringValue();
                                    lexer.nextToken();
                                    exprList = getRealParameters();
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
                                
                                return new MessageSendToVariable( anInstanceVariable, aMethod, exprList );

                                 */
                                default:
                                // expression of the kind "this.x"
                                //# corrija
                           /*
                                procure x na lista de vari�veis de inst�ncia da classe corrente:
                                anInstanceVariable = currentClass.searchInstanceVariable(ident);
                                if ( anInstanceVariable == null )
                                ...
                                return new VariableExpr( anInstanceVariable )
                                 */
                            }

                        }
                        break;
                    case Symbol.IDENT:
                        variableName = lexer.getStringValue();
                        lexer.nextToken();
                        if (lexer.token != Symbol.DOT) {
                            // expression of the kind "x"
                            //# corrija
                     /* 
                            if ( (aVariable = symbolTable.get...(variableName)) == null )
                            ...
                            return new VariableExpr(aVariable);
                             */
                        } else {
                            // expression of the kind "x.m()"
                            lexer.nextToken();  // eat the dot
                            switch (lexer.token) {
                                case Symbol.IDENT:
                                    methodName = lexer.getStringValue();
                                    lexer.nextToken();
                                    exprList = getRealParameters();
                                    //#  corrija
                           /* 

                                    if ( (aVariable = symbolTable.getInLocal(variableName)) != null ) {
                                    // x is a variable
                                    Type t = aVariable.getType();
                                    teste se t � do tipo ClassDec nesta linha
                                    aClass = (ClassDec ) t;
                                    verifique se a classe aClass possui um m�todo chamado methodName
                                    que pode receber como par�metros as express�es de exprList.
                                    Algo como (apenas o in�cio):
                                    aMethod = aClass.searchMethod(methodName);
                                    ...
                                    return new MessageSendToVariable(
                                    aVariable, aMethod, exprList);

                                    }
                                    else {
                                    // em "x.m()", x is not a variable. Should be a class name
                                    if ( (aClass = symbolTable.getInGlobal(variableName)) == null )
                                    ...
                                    nesta linha, verifique se methodName � um m�todo est�tico da
                                    classe aClass que pode aceitar como par�metros as express�es de exprList.
                                    Algo como (apenas o in�cio):
                                    aStaticMethod = aClass.searchStaticMethod(methodName);
                                    ...
                                    return new MessageSendStatic(aClass, aStaticMethod, exprList);
                                    }


                                     */

                                    break;
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
}
