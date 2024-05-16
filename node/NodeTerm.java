package node;

import eval.Environment;
import eval.EvalException;

public class NodeTerm extends Node {

    protected NodeFact fact;
    protected NodeMulop mulop;
    protected NodeTerm term;

    public NodeTerm(NodeFact fact, NodeMulop mulop, NodeTerm term) {
        this.fact = fact;
        this.mulop = mulop;
        this.term = term;
    }

    public NodeTerm(NodeFact fact) {
        this.fact = fact;
        this.mulop = null;
        this.term = null;
    }

    public void append(NodeTerm term) {
        if (this.term==null)
        {
            this.mulop=term.mulop;
            this.term=term;
            term.mulop=null;
        }
        else
        {
            this.term.append(term);
        }
    }


    public double eval(Environment env) throws EvalException {
        if (mulop == null) {
            return fact.eval(env);
        } else {
            double factVal = fact.eval(env);
            double termVal = term.eval(env);

            return mulop.compute(termVal, factVal);
        }
    }
}