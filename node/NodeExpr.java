package node;

import eval.Environment;
import eval.EvalException;

public class NodeExpr extends Node {

    protected NodeTerm term;
    protected NodeAddop addop;
    protected NodeExpr expr;

    public NodeExpr(NodeTerm term, NodeAddop addop, NodeExpr expr) {
        this.term = term;
        this.addop = addop;
        this.expr = expr;
    }

    public NodeExpr(NodeTerm term) {
        this.term = term;
        this.addop = null;
        this.expr = null;
    }


    public void append(NodeExpr expr) {
        if (this.expr==null)
        {
            this.addop=expr.addop;
            this.expr=expr;
            expr.addop=null;
        }
        else{
            this.expr.append(expr);
        }
    }

    @Override
    public double eval(Environment env) throws EvalException {
        double termValue = term.eval(env);
        if (expr != null) {
            double exprValue = expr.eval(env);
            return addop.compute(exprValue, termValue);
        } else {
            return termValue;
        }
    }
}
