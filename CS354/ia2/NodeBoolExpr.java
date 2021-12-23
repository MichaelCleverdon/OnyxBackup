public class NodeBoolExpr extends Node {

    private NodeExpr expr1, expr2;
    private NodeRelop relop;

    public NodeBoolExpr(NodeExpr expr1, NodeRelop relop, NodeExpr expr2) {
        this.expr1=expr1;
        this.relop=relop;
        this.expr2=expr2;
    }

    public double eval(Environment env) throws EvalException {
		//Evaluate the expression. 7.0 is true 0.0 is false
	    return relop.op(expr1.eval(env),expr2.eval(env));
    }

}
