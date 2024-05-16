package node;

import eval.Environment;
import eval.EvalException;

public class NodeStmtWhile extends NodeStmt{
    private NodeBoolExpr conditional;
    private NodeStmt stmt;

    public NodeStmtWhile(int position, NodeBoolExpr conditional, NodeStmt stmt){
        this.position = position;
        this.conditional = conditional;
        this.stmt = stmt;
    }

    public double eval(Environment env) throws EvalException{
        double result = 0.0;
        while(conditional.eval(env) == 1.0){
            result = stmt.eval(env);
        }
        return result;
    }
}
