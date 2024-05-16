package node;

import eval.Environment;
import eval.EvalException;

public class NodeStmtBegin extends NodeStmt{
    private NodeBlock block;

    public NodeStmtBegin(int position, NodeBlock block){
        this.position = position;
        this.block = block;
    }

    public double eval(Environment env) throws EvalException{
        return block.eval(env);
    }
}
