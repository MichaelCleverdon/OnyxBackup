public class NodeStmtIf extends NodeStmt {

    private NodeBoolExpr bexpr;
    private NodeStmt stmt;

    public NodeStmtIf(NodeBoolExpr bexpr, NodeStmt stmt) {
	    this.bexpr=bexpr;
        this.stmt = stmt;
    }

    public double eval(Environment env) throws EvalException {
        //Only valuate the code if the bexpr returns "true" which is 7
	    return bexpr.eval(env) == 7.0 ? stmt.eval(env) : Double.MAX_VALUE;
    }

}
