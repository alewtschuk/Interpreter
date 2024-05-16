package node;

import eval.Environment;
import eval.EvalException;

public class NodeFactNeg extends NodeFact {

    protected NodeFact fact;

    public NodeFactNeg(NodeFact fact) {
        this.fact = fact;
    }

    public double eval(Environment env) throws EvalException {
        return - fact.eval(env);
    }
}