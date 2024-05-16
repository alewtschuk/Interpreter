package node;

import java.util.Scanner;

import eval.Environment;
import eval.EvalException;
import syntax.Token;

public class NodeStmtRd extends NodeStmt{
    
    private Token id;
    public static Scanner scanner = new Scanner(System.in);

    public NodeStmtRd(int position, Token id){
        this.position = position;
        this.id = id;
    }

    public double eval(Environment env) throws EvalException{
        if(!scanner.hasNext()){
            scanner = new Scanner(System.in);
        }
        double val = scanner.nextDouble();
        env.put(id.getLexeme(), val);
        return val;
    }
}
