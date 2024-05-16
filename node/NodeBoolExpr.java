package node;

import eval.Environment;
import eval.EvalException;

public class NodeBoolExpr extends Node{

    private NodeExpr expressionOne;
    private NodeExpr expressionTwo;
    private NodeRelop relop;

    public NodeBoolExpr(NodeExpr expressionOne, NodeExpr expressionTwo, NodeRelop relop){
        this.expressionOne = expressionOne;
        this.expressionTwo = expressionTwo;
        this.relop = relop;
    }
    
    public double eval(Environment env) throws EvalException{
        double valOne = expressionOne.eval(env);
        double valTwo = expressionTwo.eval(env);
        double result = relop.compute(valOne, valTwo);
        return result;
    }
}
