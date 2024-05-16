package node;

import eval.EvalException;
import syntax.Token;

public class NodeMulop extends Node {

    protected Token mulop;

    public NodeMulop(int position, Token mulop) {
        this.position = position;
        this.mulop = mulop;
    }

    public double compute(double op1, double op2) throws EvalException{
        return switch (mulop.getToken()) {
            case "*" -> op1 * op2;
            case "/" -> op1 / op2;
            default -> throw new EvalException(this.position, "mulop is strange: " + mulop);
        };
    }
}