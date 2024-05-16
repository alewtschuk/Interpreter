package syntax;
import java.util.*;

/**
 * Scanner for programming language
 */
public class Scanner {

    private String program;      // source program being interpreted
    private int position;        // index of next char in program
    private Token currentToken;       // current (most recently scanned token)


    private Set<String> whitespace = new HashSet<>();
    private Set<String> letters = new HashSet<>();
    private Set<String> keywords = new HashSet<>();
    private Set<String> operators = new HashSet<>();


    /**
     * Creates a new scanner
     *
     * @param program - the program text to scan
     */
    public Scanner(String program) {
        this.program = program;
        position = 0;
        currentToken = null;
        initWhitespace(whitespace);
        initLetters(letters);
        initKeywords(keywords);
        initOperators(operators);
    }

    private void initKeywords(Set<String> keywords2) {
        keywords.add("wr");
        keywords.add("rw");
        keywords.add("then");
        keywords.add("else");
        keywords.add("while");
        keywords.add("if");
        keywords.add("begin");
        keywords.add("end");
        keywords.add("do");
        keywords.add("rd");
    }

    private void initLetters(Set<String> s) {
        fill(s, 'A', 'Z');
        fill(s, 'a', 'z');
    }

    private void fill(Set<String> s, char lo, char hi) {
        for (char c = lo; c <= hi; c++) {
            s.add(c + "");
        }
    }

    private void initWhitespace(Set<String> s) {
        s.add(" ");
        s.add("\n");
        s.add("\t");
    }

    private void initOperators(Set<String> s){
        s.add("+");
        s.add("-");
        s.add("*");
        s.add("/");
        s.add(";");
        s.add("=");
        s.add("(");
        s.add(")");
        s.add("<");
        s.add("<=");
        s.add(">");
        s.add(">=");
        s.add("<>");
        s.add("==");
    }

    private void advance() {
        this.position++;
    }

    private String posAsString() {
        return program.charAt(position) + "";
    }

    /**
     * Determines the kind of the next token (e.g., "id") and calls the
     * appropriate method to scan the token's lexeme (e.g., "foo").
     *
     * @return boolean indicating if there are more tokens to scan.
     */
    public boolean next() {

        while (hasChar() && whitespace.contains(posAsString())) {
            advance();
        }

        if (!hasChar())
            return false;

        String c = posAsString();

        //	rest here!

        switch (c) {
            case ".":
                // Handle case where character is a dot
                this.currentToken = nextNum();
                break;
    
            case "!":
                // Handle comment
                commentHandler();
                return next();
    
            default:
                // Handle other cases which require more complex conditions
                if (letters.contains(c)) {
                    this.currentToken = nextKwID();
                } else if (Character.isDigit(c.charAt(0))) {
                    this.currentToken = nextNum();
                } else if (operators.contains(c)) {
                    this.currentToken = nextOperator();
                } else {
                    // Handle illegal character
                    System.err.println("illegal character at position " + position);
                    position++;
                    return next();
                }
                break;
        }
        
        return this.currentToken != null;
    }

    /**
     * Handles comment logic. 
     * Checks for an ! one space ahead of any ! in the program.
     * If found will skip to the end of the comment and advance.
     */
    private void commentHandler() {
        if (hasCharComment(position + 1) && posAsStringComment(position + 1).equals("!")) {
            // Skip the initial "!!"
            advance();
            advance();

            // Loop until the end of the comment "!!"
            while (hasCharComment(position + 1)) {
                if (posAsStringComment(position).equals("!") && posAsStringComment(position + 1).equals("!")) {
                    // Skip the ending "!!"
                    advance();
                    advance();
                    return;
                }
                advance();
            }
        }
    }

    /**
     * Comment handler logic to allow passing in index to look 1 ahead
     * @param index
     * @return String
     */
    private String posAsStringComment(int index) {
        return program.charAt(index) + "";
    }

    /**
     * Comment handler logic to allow passing in index to look 1 ahead
     * @param index
     * @return boolean
     */
    public boolean hasCharComment(int index) {
        return index < program.length();
    }


    /**
     * Checks if a token is a KwID and if there is a number in the Identifier
     * @return
     */
    private Token nextKwID() {

        int old = this.position;
        advance();

        while (hasChar() && (letters.contains(posAsString()) || Character.isDigit(posAsString().charAt(0)))) {
            advance();
        }

        String lexeme = program.substring(old, position);

        if (keywords.contains(lexeme))
            return new Token(lexeme, lexeme);
        else
            return new Token("id", lexeme);
    }

    /**
     * Checks if the token is a number and if there is a . in the number 
     * @return new Token
     */
    private Token nextNum() {

        int old = this.position;
        boolean isThereADecimal = false;
        advance();

        while (hasChar() && (Character.isDigit(posAsString().charAt(0)) || posAsString().equals("."))) {
            if(posAsString().equals(".")){
                if(isThereADecimal){
                    break;
                } else isThereADecimal = true;
            }
            advance();
        }

        String lexeme = program.substring(old, position);

            return new Token("num", lexeme);
    }

    /**
     * Checks for operators
     * @return new Token
     */
    private Token nextOperator() {
        String lexeme = posAsString();
        int start = position;
        advance();
        if(hasChar()){
            advance();

            lexeme = program.substring(start, position);
            //Does a check for to see if there is multiple characters for the operator
            if(!operators.contains(lexeme)){
                position --;
                lexeme = program.substring(start, start + 1); //Character found is the single character operator
            }
        } else{
            lexeme = program.substring(start, position); //First character found is the complete opperator
        }
        return new Token(lexeme, lexeme);
    }

    /**
     * Determines if the current position of the scanner is in the bounds of the
     * program
     * @return true if there are more characters in program
     */
    public boolean hasChar() {
        return position < program.length();
    }


    /**
     * Scan's the next lexeme if the current token is the expected token.
     * @param t - the expected token
     * @throws SyntaxException - if current token is not the expected token
     */
    public void match(Token t) throws SyntaxException {
        if (!t.equalType(getCurrent())) {
            throw new SyntaxException(position, t, getCurrent());
        }
        next();
    }

    public Token getCurrent() throws SyntaxException {
        if (currentToken == null) {
            throw new SyntaxException(position, new Token("ANY"), new Token("EMPTY"));
        }
        return currentToken;
    }

    public int getPosition() {
        return position;
    }
}