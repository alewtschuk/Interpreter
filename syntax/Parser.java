package syntax;

import node.*;

/**
 * A recursive descent parser
 */
public class Parser {

    private Scanner scanner;

    /**
     * Parse an input program and return a Node that is the root of the
     * resulting parse tree.
     *
     * @param program String to be scanned and parsed
     * @return the Root Node of a parse tree
     * @throws SyntaxException - If there is a syntax error
     */
    public Node parse(String program) throws SyntaxException {
        scanner = new Scanner(program);
        scanner.next(); //"prime the pump"
        NodeBlock block = parseBlock();
        if(scanner.getPosition() != program.length()){
            throw new SyntaxException(scanner.getPosition(), "Program still reading after end");
        }
        return block;
    }

    private NodeBlock parseBlock() throws SyntaxException {
       NodeStmt stmt = parseStmt();
       NodeBlock block = null;
       if(scanner.getCurrent().getLexeme().equals(";")){
        scanner.next();
        block = parseBlock();
        return new NodeBlock(stmt,block);
       } else{
        return new NodeBlock(stmt);
       }
    }

    private NodeAssn parseAssn() throws SyntaxException {
        Token id = scanner.getCurrent();
        match("id");
        match("=");
        NodeExpr expr = parseExpr();
        return new NodeAssn(id, expr);
    }
    


    private NodeStmt parseStmt() throws SyntaxException {
        Token current = scanner.getCurrent();

        switch (current.getToken()) {
            case "wr":
                match("wr");
                NodeExpr expr = parseExpr();
                return new NodeWr(expr);
            case "rd":
                match("rd");
                Token id = scanner.getCurrent();
                match("id");
                return new NodeStmtRd(scanner.getPosition(), id);
            case "while":
                match("while");
                NodeBoolExpr whileBool = parseBoolExpr();
                match("do");
                NodeStmt stmt = parseStmt();
                return new NodeStmtWhile(scanner.getPosition(), whileBool, stmt);
            case "begin":
                match("begin");
                NodeBlock block = parseBlock();
                match("end");
                return new NodeStmtBegin(scanner.getPosition(), block);
            case "if":
                match("if");
                NodeBoolExpr ifBool = parseBoolExpr();
                match("then");
                NodeStmt stmtThen = parseStmt();
                if(scanner.getCurrent().equalType(new Token("else"))){
                    match("else");
                    NodeStmt stmtElse = parseStmt();
                    return new NodeStmtIf(scanner.getPosition(), ifBool, stmtThen, stmtElse);
                }
                return new NodeStmtIf(scanner.getPosition(), ifBool, stmtThen);
            case "id":
                return parseAssn();
            default:
                throw new SyntaxException(scanner.getPosition(), "BIG OL ERROR IN THE PARSESTMT");
        }
    }

    /**
     * TODO
     *
     * @return
     * @throws SyntaxException
     */
    private NodeExpr parseExpr() throws SyntaxException {
        NodeTerm term = parseTerm();
        NodeAddop addop = parseAddop();
        if (addop == null) {
            return new NodeExpr(term);
        } else {
            NodeExpr expr = parseExpr();
            expr.append(new NodeExpr(term, addop, null));
            return expr;
        }
    }
    
    
    

    /**
     * TODO
     * @return
     * @throws SyntaxException
     */
    private NodeTerm parseTerm() throws SyntaxException {
        NodeFact fact = parseFact();
        NodeMulop mulop = parseMulop();

        if (mulop == null) {
            return new NodeTerm(fact);
        } else {
            NodeTerm term=parseTerm();
            term.append(new NodeTerm(fact,mulop,null));
            return term;
        }
    }

    /**
     * TODO
     * @return
     * @throws SyntaxException
     */
    private NodeFact parseFact() throws SyntaxException {
        Token current = scanner.getCurrent();
    
        if (current.equalType(new Token("id"))) {
            match("id");
            return new NodeFactId(scanner.getPosition(), current);
    
        } else if (current.equalType(new Token("num"))) {
            match("num");
            return new NodeFactNum(current);
    
        } else if (current.equalType(new Token("-"))) {
            match("-");
            NodeFact fact = parseFact();
            return new NodeFactNeg(fact);
            
        } else if (current.equalType(new Token("("))) {
            match("(");
            NodeExpr expr = parseExpr();
            match(")");
            return new NodeFactExpr(expr);
    
        } else {
            throw new SyntaxException(scanner.getPosition(), "Invalid factor");
        }
    }
    

    /**
     * Parses an addop nonterminal and returns it.
     * @return a Node that represent an addop
     * @throws SyntaxException if an invalid terminal is discovered
     */
    private NodeAddop parseAddop() throws SyntaxException {
        Token addop = scanner.getCurrent();
        if (addop.equalType(new Token("+"))) {
            match("+");
            return new NodeAddop(scanner.getPosition(), addop);

        } else if (addop.equalType(new Token("-"))) {
            match("-");
            return new NodeAddop(scanner.getPosition(), addop);

        } else {
            return null;
        }
    }

    /**
     * TODO
     * @return
     * @throws SyntaxException
     */
    private NodeMulop parseMulop() throws SyntaxException {
        Token mulop = scanner.getCurrent();
        if (mulop.equalType(new Token("*"))) {
            match("*");
            return new NodeMulop(scanner.getPosition(), mulop);

        } else if (mulop.equalType(new Token("/"))) {
            match("/");
            return new NodeMulop(scanner.getPosition(), mulop);
        } else {
            return null;
        }
    }

    /**
     * Parses relational operator
     * @return
     * @throws SyntaxException
     */
    private NodeRelop parseRelop() throws SyntaxException{
        Token current = scanner.getCurrent();
        String relop;

        switch(current.getToken()){
            case "<":
                relop = current.getLexeme();
                scanner.next();
                return new NodeRelop(scanner.getPosition(), relop);
            case "<=":
                relop = current.getLexeme();
                scanner.next();
                return new NodeRelop(scanner.getPosition(), relop);
            case ">":
                relop = current.getLexeme();
                scanner.next();
                return new NodeRelop(scanner.getPosition(), relop);
            case ">=":
                relop = current.getLexeme();
                scanner.next();
                return new NodeRelop(scanner.getPosition(), relop);
            case "<>":
                relop = current.getLexeme();
                scanner.next();
                return new NodeRelop(scanner.getPosition(), relop);
            case "==":
                relop = current.getLexeme();
                scanner.next();
                return new NodeRelop(scanner.getPosition(), relop);
            default:
                throw new SyntaxException(scanner.getPosition(), new Token("Relop Incorrect"), scanner.getCurrent());
        }
    }

    private NodeBoolExpr parseBoolExpr() throws SyntaxException{
        NodeExpr expressionOne = parseExpr();
        NodeRelop relop = parseRelop();
        NodeExpr expressionTwo = parseExpr();
        return new NodeBoolExpr(expressionOne, expressionTwo, relop);
    }

    private void match(String s) throws SyntaxException {
        scanner.match(new Token(s));
    }
}
