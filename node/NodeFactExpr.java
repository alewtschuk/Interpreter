package node;

import eval.Environment;
import eval.EvalException;

public class NodeFactExpr extends NodeFact {
    protected NodeExpr expr;

    public NodeFactExpr(NodeExpr expr) {
        this.expr = expr;
    }

    @Override
    public double eval(Environment env) throws EvalException {
        return expr.eval(env);
    }
}
