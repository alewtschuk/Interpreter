package node;

import eval.Environment;
import eval.EvalException;

public class NodeStmtIf extends NodeStmt{
    private NodeBoolExpr bool;
    private NodeStmt stmtThen;
    private NodeStmt stmtElse; //This is optional depending on then else
    private double defaultVal = 0.0; //Default value

    public NodeStmtIf(int position, NodeBoolExpr bool, NodeStmt stmtThen, NodeStmt stmtElse){
        this.position = position;
        this.bool = bool;
        this.stmtThen = stmtThen;
        this.stmtElse = stmtElse;
    }

    public NodeStmtIf(int position, NodeBoolExpr bool, NodeStmt stmtThen){
        this.position = position;
        this.bool = bool;
        this.stmtThen = stmtThen;
        this.stmtElse = null;
    }

    public double eval(Environment env) throws EvalException{
        double value = bool.eval(env);
        if(value == 1.0){
            return stmtThen.eval(env);
        } else if(stmtElse != null){
            return stmtElse.eval(env);
        } else{
            return defaultVal;
        }
    }
}
