package node;

public class NodeRelop extends Node{
    private String relop;

    public NodeRelop(int position, String relop){
        this.position = position;
        this.relop = relop;
    }

    public double compute(double valOne, double valTwo){
        switch(relop){
            case "<":
                return (valOne < valTwo) ? 1.0 : 0.0;
            case "<=":
                return (valOne <= valTwo) ? 1.0 : 0.0;
            case ">":
                return (valOne > valTwo) ? 1.0 : 0.0;
            case ">=":
                return (valOne >= valTwo) ? 1.0 : 0.0;
            case "<>":
                return (valOne != valTwo) ? 1.0 : 0.0;
            case "==":
                return (valOne < valTwo) ? 1.0 : 0.0;
            default:
                return 0.0;
        }
    }
}
