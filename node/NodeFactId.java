package node;

import eval.Environment;
import eval.EvalException;
import syntax.Token;

public class NodeFactId extends NodeFact {
    protected Token id;

    public NodeFactId(int position, Token id) {
        this.position = position;
        this.id = id;
    }

    public String getLexeme() {
        return id.getLexeme();
    }

    public double eval(Environment env) throws EvalException {
        return env.get(this.position, id.getLexeme());
    }
}
