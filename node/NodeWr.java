package node;

import eval.Environment;
import eval.EvalException;

public class NodeWr extends NodeStmt {
    protected NodeExpr expr;

    public NodeWr(NodeExpr expr) {
        this.expr = expr;
    }

    public double eval(Environment env) throws EvalException {
        double result = expr.eval(env);
        System.out.println(result);
        return result;
    }
}