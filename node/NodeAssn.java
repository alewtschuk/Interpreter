package node;

import eval.Environment;
import eval.EvalException;
import syntax.Token;

public class NodeAssn extends NodeStmt {

    private Token id;
    private NodeExpr expr;

    public NodeAssn(Token id, NodeExpr expr) {
        this.id = id;
        this.expr = expr;
    }

    public double eval(Environment env) throws EvalException {

        double value = expr.eval(env);
        String variable = id.getLexeme();
        env.put(variable, value);

        return value;
    }
}