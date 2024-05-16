package node;

import eval.Environment;
import eval.EvalException;

public class NodeBlock extends Node {

    protected NodeStmt statement;
    protected NodeBlock block;

    public NodeBlock(NodeStmt statement) {
        this.statement = statement;
        this.block = null;
    }

    public NodeBlock(NodeStmt statement, NodeBlock block) {
        this.statement = statement;
        this.block = block;
    }

    @Override
    public double eval(Environment env) throws EvalException {
        double result = 0;
        if(block == null){
            result = statement.eval(env);
        } else{
            result = statement.eval(env);
            block.eval(env);
        }
        return result;
    }
}
