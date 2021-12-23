public class NodeFactExpr extends NodeFact {

    private NodeExpr expr;
    private NodeUnary negative;

    public NodeFactExpr(NodeExpr expr) {
	this.expr=expr;
    }

    public NodeFactExpr(NodeExpr expr, NodeUnary negative){
        this.expr = expr;
        this.negative = negative;
    }

    public double eval(Environment env) throws EvalException {
        //If it is negative, multiply the expression by -1
	    return negative != null ? -1*expr.eval(env) : expr.eval(env);
    }

}
