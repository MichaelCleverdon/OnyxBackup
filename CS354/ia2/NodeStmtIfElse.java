public class NodeStmtIfElse extends NodeStmt {

    private NodeBoolExpr bexpr;
    private NodeStmt stmt;
    private NodeStmt stmtElse;

    public NodeStmtIfElse(NodeBoolExpr bexpr, NodeStmt stmt, NodeStmt stmtElse) {
	    this.bexpr=bexpr;
        this.stmt = stmt;
        this.stmtElse = stmtElse;
    }

    public double eval(Environment env) throws EvalException {
        //Only valuate the code if the bexpr returns "true" which is 7
	    return bexpr.eval(env) == 7.0 ? stmt.eval(env) : stmtElse.eval(env);
    }

}
