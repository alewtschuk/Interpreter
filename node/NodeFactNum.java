package node;

import eval.Environment;
import eval.EvalException;
import syntax.Token;

public class NodeFactNum extends NodeFact {

    protected Token num;

    public NodeFactNum(Token num) {
        this.num = num;
    }


    public double eval(Environment env) throws EvalException {
        try {
            return Double.parseDouble(num.getLexeme());
        } catch (NumberFormatException e) {
            throw new EvalException(this.position, "Invalid number " + num);
        }
    }
}