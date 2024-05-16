package node;

import eval.EvalException;
import syntax.Token;

public class NodeAddop extends Node {

    protected Token addop;

    public NodeAddop(int position, Token addop) {

        this.position = position;
        this.addop = addop;
    }

    public double compute(double op1, double op2) throws EvalException {
        return switch (addop.getToken()) {
            case "+" -> op1 + op2;
            case "-" -> op1 - op2;
            default -> throw new EvalException(this.position, "addop is strange: " + addop);
        };
    }
}